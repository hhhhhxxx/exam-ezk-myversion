package com.jdpu.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.entity.authority.Resource;
import com.jdpu.common.entity.authority.ResourceCategory;
import com.jdpu.auth.mapper.ResourceCategoryMapper;
import com.jdpu.auth.service.IResourceCategoryService;
import com.jdpu.auth.service.IResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : chenrg
 * @create 2020/7/14 19:43
 **/
@Service
@Slf4j
public class ResourceCategoryServiceImpl extends ServiceImpl<ResourceCategoryMapper, ResourceCategory> implements IResourceCategoryService {

    @Autowired
    private IResourceService resourceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Integer id) {
        List<Resource> resources = resourceService.getByCategoryId(id);
        if (CollectionUtils.isNotEmpty(resources)) {
            throw new RuntimeException("资源分类下有资源信息，不允许删除!");
        }
        return this.removeById(id);
    }
}
