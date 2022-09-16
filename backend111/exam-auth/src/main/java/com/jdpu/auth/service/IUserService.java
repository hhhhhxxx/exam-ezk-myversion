package com.jdpu.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdpu.common.dto.UserDTO;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.param.StuQueryParam;
import com.jdpu.common.param.TeaQueryParam;
import com.jdpu.common.param.UserQueryParam;
import com.jdpu.common.param.user.UserPageRequestVM;

import java.util.List;

/**
 * 整合
 */
public interface IUserService extends IService<UserEntity> {

    // xzy-authority的userService
    Page<UserDTO> getTeacherPages(TeaQueryParam userQueryParam);
    Page<UserDTO> getStuPages(StuQueryParam stuQueryParam);


    UserDTO getUserById(Integer userId);

    UserDTO getUserByPhone(String phone);

    UserEntity getUserEntityByPhone(String phone);

    Page<UserDTO> getUserPages(UserQueryParam userQueryParam);

    Page<UserDTO> getUserPages_p(UserQueryParam userQueryParam);


    // exam-user
    Page<UserEntity> userPage(UserPageRequestVM requestVM);
    // 更新

    boolean saveUser(UserEntity user);

    boolean updateUser(UserEntity user);

    boolean deleteUser(Integer id);

    boolean isRegister(String phone);

    Page<UserEntity> teacherPage(TeaQueryParam userQueryParam);

    Page<UserEntity> studentPage(StuQueryParam stuQueryParam);

    List<UserEntity> getUserByIds(List<Integer> userIds);

    List<UserEntity> getTeacherList();
}

