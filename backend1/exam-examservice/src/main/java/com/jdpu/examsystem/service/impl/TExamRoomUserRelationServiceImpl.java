package com.jdpu.examsystem.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.Query;

import com.jdpu.examsystem.dao.TExamRoomUserRelationDao;
import com.jdpu.examsystem.entity.TExamRoomUserRelationEntity;
import com.jdpu.examsystem.service.TExamRoomUserRelationService;


@Service("tExamRoomUserRelationService")
public class TExamRoomUserRelationServiceImpl extends ServiceImpl<TExamRoomUserRelationDao, TExamRoomUserRelationEntity> implements TExamRoomUserRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TExamRoomUserRelationEntity> page = this.page(
                new Query<TExamRoomUserRelationEntity>().getPage(params),
                new QueryWrapper<TExamRoomUserRelationEntity>()
        );

        return new PageUtils(page);
    }

}
