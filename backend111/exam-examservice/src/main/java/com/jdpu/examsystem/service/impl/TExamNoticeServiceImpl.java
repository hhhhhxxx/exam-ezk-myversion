package com.jdpu.examsystem.service.impl;

import com.jdpu.examsystem.entity.TExamArgumentsEntity;
import com.jdpu.examsystem.feign.exampaper.ExamPaperFeignService;
import com.jdpu.examsystem.service.TExamArgumentsService;
import com.jdpu.examsystem.vo.ExamNoticeVo;
import com.jdpu.examsystem.vo.dto.admin.exam.ExamPaperEditRequestVM;
import com.jdpu.common.entity.vo.RestResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.Query;

import com.jdpu.examsystem.dao.TExamNoticeDao;
import com.jdpu.examsystem.entity.TExamNoticeEntity;
import com.jdpu.examsystem.service.TExamNoticeService;


@Service("tExamNoticeService")
public class TExamNoticeServiceImpl extends ServiceImpl<TExamNoticeDao, TExamNoticeEntity> implements TExamNoticeService {

    @Autowired
    private TExamArgumentsService argumentsService;
    @Autowired
    private ExamPaperFeignService paperFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TExamNoticeEntity> page = this.page(
                new Query<TExamNoticeEntity>().getPage(params),
                new QueryWrapper<TExamNoticeEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 发布考试通知
     */
    @Override
    public boolean publishToUser(Integer argumentId, List<Integer> userIdList) {
        //List<Integer> userIds = JSON.parseArray(userIdList, Integer.class);

        try {
            userIdList.forEach(userId -> {
                TExamNoticeEntity noticeEntity = new TExamNoticeEntity();
                noticeEntity.setExamArgumentsId(argumentId);
                noticeEntity.setUserId(userId);
                int count = this.baseMapper.insert(noticeEntity);
                if (count == 0) {
                    throw new RuntimeException("插入noticeEntity异常");
                }
            });
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<ExamNoticeVo> getNoticeListByUserId(Integer userId) {
        List<ExamNoticeVo> vos  = new ArrayList<>();
        List<TExamNoticeEntity> noticeEntities = this.baseMapper.selectList(new QueryWrapper<TExamNoticeEntity>().eq("user_id", userId));
        try {
            noticeEntities.forEach(notice -> {
                ExamNoticeVo examNoticeVo = new ExamNoticeVo();
                BeanUtils.copyProperties(notice,examNoticeVo);
                TExamArgumentsEntity argumentsEntity = argumentsService.getById(notice.getExamArgumentsId());
                BeanUtils.copyProperties(argumentsEntity,examNoticeVo);
                examNoticeVo.setArgumentsId(argumentsEntity.getId());
                RestResponse<ExamPaperEditRequestVM> examPaperEditRequestVMRestResponse = paperFeignService.select(argumentsEntity.getExamPaperId());
                ExamPaperEditRequestVM examPaperEditRequestVM = examPaperEditRequestVMRestResponse.getResponse();
                examNoticeVo.setName(examPaperEditRequestVM.getName());
                vos.add(examNoticeVo);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vos;
    }


}
