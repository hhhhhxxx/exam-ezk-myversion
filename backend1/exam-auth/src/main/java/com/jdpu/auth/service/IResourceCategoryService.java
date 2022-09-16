package com.jdpu.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.entity.authority.ResourceCategory;

/**
 * @author chenrg
 */
public interface IResourceCategoryService extends IService<ResourceCategory> {

    /**
     * 删除资源分类，分类下有资源的不允许删除
     *
     * @param id
     * @return
     */
    boolean deleteById(Integer id);
}
