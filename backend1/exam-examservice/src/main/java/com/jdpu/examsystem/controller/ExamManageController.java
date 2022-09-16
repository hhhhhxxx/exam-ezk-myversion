package com.jdpu.examsystem.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jdpu.examsystem.entity.*;
import com.jdpu.examsystem.service.*;
import com.jdpu.examsystem.feign.exampaper.ExamPaperFeignService;
import com.jdpu.examsystem.vo.ExamWithStatusVM;
import com.jdpu.examsystem.vo.PlanAndRoomVo;
import com.jdpu.examsystem.vo.StudentExamListVo;
import com.jdpu.common.entity.vo.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 考试管理模块
 */
@Slf4j
@RestController
@RequestMapping("/api/examsystem/manage")
public class ExamManageController {
    @Autowired
    private TExamArgumentsService tExamArgumentsService;
    @Autowired
    private ExamPaperFeignService examPaperFeignService;
    @Autowired
    private TExamInfoService tExamInfoService;
    @Autowired
    private TExamNoticeService tExamNoticeService;
    @Autowired
    private TExamPlanService tExamPlanService;
    @Autowired
    private TExamRoomService tExamRoomService;
    @Autowired
    private TExamArguPlanRoomRelationService examArguPlanRoomRelationService;
    @Autowired
    private UserClassService userClassService;

    /**
     * 发布考试
     * 参数：exam_paper_id
     */
    @PostMapping("/publishExam")
    public RestResponse publishExam(@RequestBody String jsonStr) {
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        String paperId = jsonObject.getStr("paperId");
        String argumentsEntityJsonStr = jsonObject.getStr("argumentsEntity");
        String kaowuEntityJsonStr = jsonObject.getStr("kaowuEntity");
        //String userIdList = jsonObject.getStr("noticeUserIdList");
        String classId = jsonObject.getStr("classId");

        TExamArgumentsEntity argumentsEntity = JSONUtil.toBean(argumentsEntityJsonStr, TExamArgumentsEntity.class);
        PlanAndRoomVo vo = JSONUtil.toBean(kaowuEntityJsonStr, PlanAndRoomVo.class);
        argumentsEntity.setExamPaperId(Integer.valueOf(paperId));
        // 创建考试参数前检查是否存在记录
//        boolean exist = tExamArgumentsService.isExist(paperId);
//        if (exist) {
//            return RestResponse.fail(500, "已存在该试卷相关考试");
//        }
        // 创建考试参数
        boolean save = tExamArgumentsService.save(argumentsEntity);

        // 创建考务管理
        TExamPlanEntity examPlanEntity = new TExamPlanEntity();
        BeanUtils.copyProperties(vo,examPlanEntity);
        boolean planSave = tExamPlanService.save(examPlanEntity);
        if (!planSave) {
            log.error("插入examPlan异常");
        }
        TExamRoomEntity examRoomEntity = new TExamRoomEntity();
        BeanUtils.copyProperties(vo,examRoomEntity);
        boolean roomSave = tExamRoomService.save(examRoomEntity);
        if (!roomSave) {
            log.error("插入examRoom异常");
        }
        TExamArguPlanRoomRelationEntity arguPlanRoomRelationEntity = new TExamArguPlanRoomRelationEntity();
        arguPlanRoomRelationEntity.setExamPlanId(examPlanEntity.getId());
        arguPlanRoomRelationEntity.setExamRoomId(examRoomEntity.getId());
        arguPlanRoomRelationEntity.setExamArgumentsId(argumentsEntity.getId());
        boolean relationSave = examArguPlanRoomRelationService.save(arguPlanRoomRelationEntity);
        if (!relationSave) {
            log.error("插入examArguPlanRoomRelation异常");
        }

        // 考试通知创建
        // 根据classId查所有userId
        JSONArray classIdArrayJson = JSONUtil.parseArray(classId);
        List<Integer> classIdList = classIdArrayJson.toList(Integer.class);

        // 发布通知给每个班级的每个学生
        List<Integer> userIdList = new ArrayList<>();
        classIdList.forEach(class_id -> {
            List<UserClass> userClassList = userClassService.list(
                    new QueryWrapper<UserClass>().eq("class_id", class_id));
            userClassList.forEach(userClass -> {
                userIdList.add(userClass.getUserId());
            });
        });

        // 去重
        List<Integer> ids = userIdList.stream().distinct().collect(Collectors.toList());

        boolean notice = tExamNoticeService.publishToUser(argumentsEntity.getId(), ids);

        return save && notice ? RestResponse.ok() : RestResponse.fail(500, "发布考试失败");
    }

    /**
     * 学生端获取考试列表
     * examStatusCode 考试状态码
     */
    @GetMapping("/getExamPaperList/{examStatusCode}")
    public RestResponse getExamPaperList(@PathVariable("examStatusCode") Integer examStatusCode) {
        List<StudentExamListVo> vos = tExamArgumentsService.getStudentExamList(examStatusCode);
        return RestResponse.ok(vos);
    }

    /**
     * 删除考试
     */
    @PostMapping("/delExam")
    public RestResponse delExam(@RequestBody String jsonStr) {
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        String argumentsId = jsonObject.getStr("argumentsId");

        // 异步执行任务
        ExecutorService executorService = Executors.newCachedThreadPool();

        Future<Boolean> deleteArgumentTask = executorService.submit(() -> {
            // 删除arguments
            return tExamArgumentsService.removeById(argumentsId);
        });

        Future<Boolean> deleteInfoTask = executorService.submit(() -> {
            // 删除info
            return tExamInfoService.remove(new QueryWrapper<TExamInfoEntity>().eq("exam_arguments_id", argumentsId));
        });

        Future<Boolean> deleteNotice = executorService.submit(() -> {
            // 删除notice
            return tExamNoticeService.remove(new QueryWrapper<TExamNoticeEntity>().eq("exam_arguments_id", argumentsId));
        });


        try {
            Boolean flag1 = deleteArgumentTask.get();
            Boolean flag2 = deleteInfoTask.get();
            Boolean flag3 = deleteNotice.get();
            if (!flag1) {
                throw new RuntimeException("删除argument表数据异常");
            }else if (!flag2) {
                throw new RuntimeException("删除info表数据异常");
            } else if (!flag3) {
                throw new RuntimeException("删除notice表数据异常");
            }
            return RestResponse.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RestResponse.fail(500, "删除考试异常");
    }


    /**
     * 获取未开始、进行中、已结束的所有考试
     * 0:未开始，1：进行中，2：已结束
     */
    @GetMapping("/list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        String examStatusCodeStr = (String) params.get("examStatusCode");
        if (StringUtils.isEmpty(examStatusCodeStr)) {
            // 获取全部考试
            List<ExamWithStatusVM> examWithStatusVMS = tExamArgumentsService.getExamListByStatus(null);
            return new RestResponse(200, "获取所有考试列表", examWithStatusVMS);
        }

        Integer examStatusCode = Integer.valueOf(examStatusCodeStr);

        // 未开始的考试列表
        List<ExamWithStatusVM> examWithStatusVMS = tExamArgumentsService.getExamListByStatus(examStatusCode);

        return new RestResponse(200, "获取成功", examWithStatusVMS);
    }
}
