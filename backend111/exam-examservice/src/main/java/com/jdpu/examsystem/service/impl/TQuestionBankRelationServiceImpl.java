package com.jdpu.examsystem.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.Query;

import com.jdpu.examsystem.dao.TQuestionBankRelationDao;
import com.jdpu.examsystem.entity.TQuestionBankRelationEntity;
import com.jdpu.examsystem.service.TQuestionBankRelationService;


@Service("tQuestionBankRelationService")
public class TQuestionBankRelationServiceImpl extends ServiceImpl<TQuestionBankRelationDao, TQuestionBankRelationEntity> implements TQuestionBankRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TQuestionBankRelationEntity> page = this.page(
                new Query<TQuestionBankRelationEntity>().getPage(params),
                new QueryWrapper<TQuestionBankRelationEntity>()
        );

        return new PageUtils(page);
    }

}
