package com.jdpu.examsystem.service.impl;

import com.jdpu.examsystem.service.TExamRoomService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.Query;

import com.jdpu.examsystem.dao.TExamRoomDao;
import com.jdpu.examsystem.entity.TExamRoomEntity;


@Service("tExamRoomService")
public class TExamRoomServiceImpl extends ServiceImpl<TExamRoomDao, TExamRoomEntity> implements TExamRoomService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TExamRoomEntity> page = this.page(
                new Query<TExamRoomEntity>().getPage(params),
                new QueryWrapper<TExamRoomEntity>()
        );

        return new PageUtils(page);
    }

}
