package com.jdpu.examsystem.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jdpu.examsystem.entity.TExamInfoEntity;
import com.jdpu.examsystem.service.TExamInfoService;
import com.jdpu.examsystem.util.ExcelUtil;
import com.jdpu.common.entity.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 考试监考记录
 *
 * @author zuck
 * @date 2022-03-23 16:43:28
 */
@RestController
@RequestMapping("/api/examsystem/texaminfo")
public class TExamInfoController {
    @Autowired
    private TExamInfoService tExamInfoService;


    /**
     * 根据studentName和argumentsId获取监控数据
     */
    @PostMapping("/getExamInfoData")
    public RestResponse getExamInfoData(@RequestBody String json) {
        JSONObject jsonObject = JSONUtil.parseObj(json);
        String studentName = jsonObject.getStr("studentName");
        String argumentsId = jsonObject.getStr("argumentsId");
        TExamInfoEntity tExamInfoEntity = this.tExamInfoService.getOne(
                new QueryWrapper<TExamInfoEntity>().eq("student_name", studentName)
                        .eq("exam_arguments_id",argumentsId));
        return RestResponse.ok(tExamInfoEntity);
    }

    /**
     * 导出examInfo表数据
     */
    @GetMapping("/downloadExcel/{argumentsId}")
    public void download(@PathVariable("argumentsId") Integer argumentsId, HttpServletResponse response) throws IOException {
        //时间戳
        long time = System.currentTimeMillis();
        //路径
        String fileName ="考试"+ argumentsId + "监考数据" + time + ".xlsx";
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = ExcelUtil.defaultTenantStyles();
        ExcelWriterSheetBuilder writerSheetBuilder = EasyExcel.write(fileName, TExamInfoEntity.class).sheet(time + "").registerWriteHandler(horizontalCellStyleStrategy);
        writerSheetBuilder.doWrite(getData(argumentsId));
        //下载
        ExcelUtil.export(fileName,response);
        //删除原本放在服务器上的数据
        ExcelUtil.deleteFile(fileName);

    }

    /**
     * 查询paperId的相关examInfo记录
     * @return
     */
    public List<TExamInfoEntity> getData(Integer argumentsId){
        List<TExamInfoEntity> examInfoEntities = tExamInfoService.list(new QueryWrapper<TExamInfoEntity>().eq("exam_arguments_id", argumentsId));
        return examInfoEntities;
    }

    /**
     * 获取考生是否进入考过（解除考试限制进入时间）
     *
     */
    @GetMapping("/getIsEnterExam/{argumentId}/{stuName}")
    public boolean getIsEnterExam(@PathVariable("argumentId") Integer argumentId,@PathVariable("stuName")String stuName) {
        boolean isEnter = tExamInfoService.getIsEnterExam(argumentId,stuName);
        return isEnter;
    }

    /**
     * 更新检测性别
     * 接收参数：
     * {
     *     "examInfoId": "",
     *     "gender": ""
     * }
     */
    @PostMapping("/updateCheckSex")
    public R updateCheckSex(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        boolean res = tExamInfoService.updateCheckSex(jsonObject);
        if (res) {
            return R.ok();
        }else{
            return R.error(500,"更新检测性别失败");
        }
    }

    /**
     * 根据exam_arguments_id,查exam_info列表
     * 无分页
     */
    @PostMapping("/getExamInfoList")
    public R getExamInfoList(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        List<TExamInfoEntity> entityList = tExamInfoService.getExamInfoList(jsonObject);
        if (entityList != null) {
            return R.ok().put("entityList",entityList);
        }else{
            return R.error(500,"查exam_info列表失败");
        }
    }

    /**
     * 学生进入考试，插入对应一条考试监考记录
     * 接收参数：
     * {
     *     "examPaperId": "",
     *     "studentName": ""
     * }
     */
    @PostMapping("/insertByStudent")
    public R insertByStudent(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        Integer examInfoId = tExamInfoService.insertByStudent(jsonObject);
        if (examInfoId != null) {
            return R.ok().put("examInfoId",examInfoId);
        }else{
            return R.error(500,"学生监考记录生成失败");
        }
    }

    /**
     * 更新exam_info表中，退出全屏次数
     * 接收参数：
     * {
     *     "examPaperId": "",
     *     "studentName": "",
     *     "exitCount": "",
     *     "examInfoId": ""
     * }
     */
    @PostMapping("/updateExitFull")
    public R updateExitFull(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        boolean res = tExamInfoService.updateExitFull(jsonObject);
        if (res) {
            return R.ok();
        }else{
            return R.error(500,"更新exam_info表中退出全屏次数失败");
        }
    }

    /**
     * 更新exam_info表中，检测到人物大于1人情况的次数
     * 接收参数：
     * {
     *     "examInfoId": "",
     *     "peopleNumCount": ""
     * }
     */
    @PostMapping("/updateOverNum")
    public R updateOverNum(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);

        boolean res = tExamInfoService.updateOverNum(jsonObject);
        if (res) {
            return R.ok();
        }else{
            return R.error(500,"更新exam_info表中检测到人物大于1人情况的次数失败");
        }
    }

    /**
     * 更新exam_info表中，检测到人物非活体次数
     * 接收参数：
     * {
     *     "examInfoId": "",
     *     "nonLiveCount": ""
     * }
     */
    @PostMapping("/updateNonLiveCount")
    public R updateNonLiveCount(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        boolean res = tExamInfoService.updateNonLiveCount(jsonObject);
        if (res) {
            return R.ok();
        }else{
            return R.error(500,"更新exam_info表中检测到人物非活体次数失败");
        }
    }

    /**
     * 更新exam_info表，before_exam_img 考前拍摄图片
     */
    @PostMapping("/updateBeforeImg")
    public R updateBeforeImg(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        boolean res = tExamInfoService.updateBeforeImg(jsonObject);
        if (res) {
            return R.ok();
        }else{
            return R.error(500,"更新exam_info表中考前拍摄图片失败");
        }
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("examsystem:texaminfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tExamInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("examsystem:texaminfo:info")
    public R info(@PathVariable("id") Integer id){
		TExamInfoEntity tExamInfo = tExamInfoService.getById(id);

        return R.ok().put("tExamInfo", tExamInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("examsystem:texaminfo:save")
    public R save(@RequestBody TExamInfoEntity tExamInfo){
		tExamInfoService.save(tExamInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("examsystem:texaminfo:update")
    public R update(@RequestBody TExamInfoEntity tExamInfo){
		tExamInfoService.updateById(tExamInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("examsystem:texaminfo:delete")
    public R delete(@RequestBody Integer[] ids){
		tExamInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
