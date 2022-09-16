package com.jdpu.examsystem.service.impl;

import com.jdpu.examsystem.service.TExamArguPlanRoomRelationService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.Query;

import com.jdpu.examsystem.dao.TExamArguPlanRoomRelationDao;
import com.jdpu.examsystem.entity.TExamArguPlanRoomRelationEntity;


@Service("tExamArguPlanRoomRelationService")
public class TExamArguPlanRoomRelationServiceImpl extends ServiceImpl<TExamArguPlanRoomRelationDao, TExamArguPlanRoomRelationEntity> implements TExamArguPlanRoomRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TExamArguPlanRoomRelationEntity> page = this.page(
                new Query<TExamArguPlanRoomRelationEntity>().getPage(params),
                new QueryWrapper<TExamArguPlanRoomRelationEntity>()
        );

        return new PageUtils(page);
    }

}
