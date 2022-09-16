package com.jdpu.examsystem.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdpu.examsystem.constant.ExamPaperStatus;
import com.jdpu.examsystem.service.TExamArgumentsService;
import com.jdpu.examsystem.vo.dto.admin.exam.ExamPaperEditRequestVM;
import com.jdpu.examsystem.feign.exampaper.ExamPaperFeignService;
import com.jdpu.examsystem.vo.ExamWithStatusVM;
import com.jdpu.examsystem.vo.StudentExamListVo;

import com.jdpu.common.entity.vo.RestResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.Query;

import com.jdpu.examsystem.dao.TExamArgumentsDao;
import com.jdpu.examsystem.entity.TExamArgumentsEntity;


@Service("tExamArgumentsService")
public class TExamArgumentsServiceImpl extends ServiceImpl<TExamArgumentsDao, TExamArgumentsEntity> implements TExamArgumentsService {

    @Autowired
    private ExamPaperFeignService examPaperFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TExamArgumentsEntity> page = this.page(
                new Query<TExamArgumentsEntity>().getPage(params),
                new QueryWrapper<TExamArgumentsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public TExamArgumentsEntity getArgumentsByPaperId(Integer paperId) {
        QueryWrapper<TExamArgumentsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_paper_id",paperId);
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public ExamPaperStatus getExamStatusByArgumentId(Integer argumentId) {
        TExamArgumentsEntity argumentsEntity = this.baseMapper.selectById(argumentId);

        // 考试异常
        if (argumentsEntity == null) {
            return ExamPaperStatus.EXAM_EXCEPTION;
        }

        Date nowTime = new Date();

        // 1. 检测考试是否开始
        Date startTime = argumentsEntity.getExamStartTime();
        // 返回-1和0，考试开始，返回1，考试未开始
        int dateResult = startTime.compareTo(nowTime);
        if (dateResult > 0) {
            // 考试未开始
            return ExamPaperStatus.EXAM_NOT_STARTED;
        }

        // 2. 检测考试是否结束
        Date endTime = argumentsEntity.getExamEndTime();
        int endResult = endTime.compareTo(nowTime);
        if (endResult < 0) {
            // 考试结束
            return ExamPaperStatus.EXAM_END;
        }

        // 3. 检测考试是否超过限制进入时间
        Integer limitMinute = argumentsEntity.getLimitEnterTime();
        DateTime limitDate = DateUtil.offsetMinute(startTime, limitMinute);
        // 返回1和0表示可以进入考试
        int limitResult = limitDate.compareTo(nowTime);
        if (limitResult < 0) {
            // 考试进行中，限制进入
            return ExamPaperStatus.EXAM_STARTED_NOT_ENTER;
        }

        return ExamPaperStatus.EXAM_STARTED_CAN_ENTER;
    }


    @Override
    public ExamPaperStatus getExamStatusByPaperId(Integer paperId) {
        QueryWrapper<TExamArgumentsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_paper_id",paperId);
        TExamArgumentsEntity argumentsEntity = this.baseMapper.selectOne(wrapper);

        // 考试异常
        if (argumentsEntity == null) {
            return ExamPaperStatus.EXAM_EXCEPTION;
        }

        Date nowTime = new Date();

        // 1. 检测考试是否开始
        Date startTime = argumentsEntity.getExamStartTime();
        // 返回-1和0，考试开始，返回1，考试未开始
        int dateResult = startTime.compareTo(nowTime);
        if (dateResult > 0) {
            // 考试未开始
            return ExamPaperStatus.EXAM_NOT_STARTED;
        }

        // 2. 检测考试是否结束
        Date endTime = argumentsEntity.getExamEndTime();
        int endResult = endTime.compareTo(nowTime);
        if (endResult < 0) {
            // 考试结束
            return ExamPaperStatus.EXAM_END;
        }

        // 3. 检测考试是否超过限制进入时间
        Integer limitMinute = argumentsEntity.getLimitEnterTime();
        DateTime limitDate = DateUtil.offsetMinute(startTime, limitMinute);
        // 返回1和0表示可以进入考试
        int limitResult = limitDate.compareTo(nowTime);
        if (limitResult < 0) {
            // 考试进行中，限制进入
            return ExamPaperStatus.EXAM_STARTED_NOT_ENTER;
        }

        return ExamPaperStatus.EXAM_STARTED_CAN_ENTER;
    }

    /**
     * 获取不同状态的考试列表
     * 50001 未开始
     * 50002 考试中
     * @return
     */
    @Override
    public List<ExamWithStatusVM> getExamListByStatus(Integer examStatusCode) {
        List<ExamWithStatusVM> examWithStatusVMList = new ArrayList<>();

        List<TExamArgumentsEntity> argumentsEntities = this.baseMapper.selectList(new QueryWrapper<>());

        List<TExamArgumentsEntity> newArgumentList = new ArrayList<>();

        if (examStatusCode == null) {
            for (TExamArgumentsEntity argumentsEntity : argumentsEntities) {
                ExamWithStatusVM examWithStatusVM = new ExamWithStatusVM();
                examWithStatusVM.setArgumentId(argumentsEntity.getId());
                BeanUtils.copyProperties(argumentsEntity,examWithStatusVM);

                Integer examPaperId = argumentsEntity.getExamPaperId();
                RestResponse<ExamPaperEditRequestVM> select = examPaperFeignService.select(examPaperId);
                ExamPaperEditRequestVM examPaperEditRequestVM = select.getResponse();
                examWithStatusVM.setExamPaperEditRequestVM(examPaperEditRequestVM);

//                ExamPaperStatus status = this.getExamStatusByPaperId(argumentsEntity.getExamPaperId());
                ExamPaperStatus status = this.getExamStatusByArgumentId(argumentsEntity.getId());
                examWithStatusVM.setExamStatus(status.getCode());

                examWithStatusVMList.add(examWithStatusVM);
            }
            return examWithStatusVMList;
        }

        // 过滤
        if(examStatusCode == 50002){
            // 50002 和 50003都是进行中
            newArgumentList = argumentsEntities.stream().filter(argumentsEntity -> {
//                ExamPaperStatus status = this.getExamStatusByPaperId(argumentsEntity.getExamPaperId());
                ExamPaperStatus status = this.getExamStatusByArgumentId(argumentsEntity.getId());
                return status.getCode() == examStatusCode || status.getCode() == ExamPaperStatus.EXAM_STARTED_NOT_ENTER.getCode();
            }).collect(Collectors.toList());
        }else{
            newArgumentList = argumentsEntities.stream().filter(argumentsEntity -> {
//                ExamPaperStatus status = this.getExamStatusByPaperId(argumentsEntity.getExamPaperId());
                ExamPaperStatus status = this.getExamStatusByArgumentId(argumentsEntity.getId());
                return status.getCode() == examStatusCode;
            }).collect(Collectors.toList());
        }

        for (TExamArgumentsEntity argumentsEntity : newArgumentList) {
            ExamWithStatusVM examWithStatusVM = new ExamWithStatusVM();
            BeanUtils.copyProperties(argumentsEntity,examWithStatusVM);
            examWithStatusVM.setArgumentId(argumentsEntity.getId());

            Integer examPaperId = argumentsEntity.getExamPaperId();
            RestResponse<ExamPaperEditRequestVM> select = examPaperFeignService.select(examPaperId);
            ExamPaperEditRequestVM examPaperEditRequestVM = select.getResponse();
            examWithStatusVM.setExamPaperEditRequestVM(examPaperEditRequestVM);
            examWithStatusVM.setExamStatus(examStatusCode);
            examWithStatusVMList.add(examWithStatusVM);
        }
        return examWithStatusVMList;
    }

    @Override
    public List<StudentExamListVo> getStudentExamList(Integer examStatusCode) {
        List<StudentExamListVo> vos = new ArrayList<>();


        IPage<TExamArgumentsEntity> argumentsEntityIPage = this.baseMapper.selectPage(new Page<TExamArgumentsEntity>().setPages(1).setSize(5), new QueryWrapper<TExamArgumentsEntity>());
        List<TExamArgumentsEntity> argumentsEntities = argumentsEntityIPage.getRecords();

        if (examStatusCode == 0) {
            argumentsEntities.forEach(argumentsEntity -> {
                StudentExamListVo studentExamListVo = new StudentExamListVo();

                Integer argumentsId = argumentsEntity.getId();
                studentExamListVo.setId(argumentsId);
                RestResponse<ExamPaperEditRequestVM> paperVm = examPaperFeignService.select(argumentsEntity.getExamPaperId());
                ExamPaperEditRequestVM examPaperEntity = paperVm.getResponse();
                studentExamListVo.setName(examPaperEntity.getName());
                studentExamListVo.setScore(Integer.valueOf(examPaperEntity.getScore()));
                studentExamListVo.setExamStartTime(argumentsEntity.getExamStartTime());
                ExamPaperStatus status = this.getExamStatusByArgumentId(argumentsEntity.getId());
                studentExamListVo.setExamStatusCode(status.getCode());
                studentExamListVo.setAllowMultidevice(argumentsEntity.getAllowMultidevice());
                Integer examTime = getExamTimeByArgumentId(argumentsEntity.getId());
                studentExamListVo.setExamTime(examTime);

                vos.add(studentExamListVo);
            });
        }else{
            List<TExamArgumentsEntity> collect = null;
            // 未开始
            if(examStatusCode == 50002){
                collect = argumentsEntities.stream().filter(argumentsEntity -> {
                    ExamPaperStatus status = getExamStatusByArgumentId(argumentsEntity.getId());
                    return status.getCode() == ExamPaperStatus.EXAM_STARTED_CAN_ENTER.getCode() ||
                            status.getCode() == ExamPaperStatus.EXAM_STARTED_NOT_ENTER.getCode();
                }).collect(Collectors.toList());

            } else {
                collect = argumentsEntities.stream().filter(argumentsEntity -> {
                    ExamPaperStatus status = getExamStatusByArgumentId(argumentsEntity.getId());
                    if (status.getCode() == examStatusCode) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

            }

            collect.forEach(argumentsEntity -> {
                StudentExamListVo studentExamListVo = new StudentExamListVo();
                studentExamListVo.setId(argumentsEntity.getId());
                RestResponse<ExamPaperEditRequestVM> paperEditRequestVMRestResponse = examPaperFeignService.select(argumentsEntity.getExamPaperId());
                ExamPaperEditRequestVM examPaperEntity = paperEditRequestVMRestResponse.getResponse();
                studentExamListVo.setName(examPaperEntity.getName());
                studentExamListVo.setScore(Integer.valueOf(examPaperEntity.getScore()));
                studentExamListVo.setExamStartTime(argumentsEntity.getExamStartTime());
                //考试状态
                studentExamListVo.setExamStatusCode(examStatusCode);
                studentExamListVo.setAllowMultidevice(argumentsEntity.getAllowMultidevice());
                Integer examTime = getExamTimeByArgumentId(argumentsEntity.getId());
                studentExamListVo.setExamTime(examTime);
                vos.add(studentExamListVo);
            });
        }

        return vos;
    }

    @Override
    public boolean isExist(String paperId) {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<TExamArgumentsEntity>().eq("exam_paper_id", paperId));
        return count > 0;
    }

    /**
     * 根据argument_id获取考试时长
     */
    public Integer getExamTimeByArgumentId(Integer argumentId) {
        TExamArgumentsEntity argumentsEntity = this.baseMapper.selectById(argumentId);
        Date examStartTime = argumentsEntity.getExamStartTime();
        Date examEndTime = argumentsEntity.getExamEndTime();
        long betweenMinute = DateUtil.between(examStartTime, examEndTime, DateUnit.MINUTE);
        return Math.toIntExact(betweenMinute);
    }



}
