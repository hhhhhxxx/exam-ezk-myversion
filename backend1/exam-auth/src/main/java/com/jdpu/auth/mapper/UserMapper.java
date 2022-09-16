package com.jdpu.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdpu.common.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author hhx
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    IPage<UserEntity> queryUserByRoleId(Page<UserEntity> page, @Param("username") String userName, @Param("role_id") Integer roleId);

    IPage<UserEntity> queryUserNoRole(Page<UserEntity> page, @Param("username") String userName);


    IPage<UserEntity> queryUserNoInClass(Page<UserEntity> page, @Param("username") String userName, @Param("role_id") Integer roleId);

    IPage<UserEntity> queryUserInClass(Page<UserEntity> page,
                                       @Param("username") String userName,
                                       @Param("role_id") Integer roleId,
                                       @Param("class_id") Integer classId);
}
