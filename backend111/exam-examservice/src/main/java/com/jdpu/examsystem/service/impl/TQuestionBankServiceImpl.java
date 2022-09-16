package com.jdpu.examsystem.service.impl;

import com.jdpu.examsystem.service.TQuestionBankService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.Query;

import com.jdpu.examsystem.dao.TQuestionBankDao;
import com.jdpu.examsystem.entity.TQuestionBankEntity;


@Service("tQuestionBankService")
public class TQuestionBankServiceImpl extends ServiceImpl<TQuestionBankDao, TQuestionBankEntity> implements TQuestionBankService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TQuestionBankEntity> page = this.page(
                new Query<TQuestionBankEntity>().getPage(params),
                new QueryWrapper<TQuestionBankEntity>()
        );

        return new PageUtils(page);
    }

}
