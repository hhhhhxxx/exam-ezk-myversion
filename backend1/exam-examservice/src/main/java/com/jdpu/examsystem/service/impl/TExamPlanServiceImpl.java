package com.jdpu.examsystem.service.impl;

import com.jdpu.examsystem.service.TExamPlanService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.Query;

import com.jdpu.examsystem.dao.TExamPlanDao;
import com.jdpu.examsystem.entity.TExamPlanEntity;


@Service("tExamPlanService")
public class TExamPlanServiceImpl extends ServiceImpl<TExamPlanDao, TExamPlanEntity> implements TExamPlanService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TExamPlanEntity> page = this.page(
                new Query<TExamPlanEntity>().getPage(params),
                new QueryWrapper<TExamPlanEntity>()
        );

        return new PageUtils(page);
    }

}
