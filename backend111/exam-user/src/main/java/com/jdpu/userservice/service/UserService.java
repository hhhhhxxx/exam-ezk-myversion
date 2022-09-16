package com.jdpu.userservice.service;


import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.xzsOld.entities.KeyValue;

import java.util.List;

public interface UserService {
    /**
     * getUsers
     *
     * @return List<User>
     */
    List<UserEntity> getUsers();

    /**
     * getUserById
     *
     * @param id id
     * @return User
     */
    UserEntity getUserById(Integer id);

    /**
     * getUserByUserName
     *
     * @param username username
     * @return User
     */
    UserEntity getUserByUserName(String username);

    /**
     * getUserByUserName
     *
     * @param username username
     * @param pwd      pwd
     * @return User
     */
    UserEntity getUserByUserNamePwd(String username, String pwd);

    /**
     * getUserByUuid
     *
     * @param uuid uuid
     * @return User
     */



    /**
     * userPageCount
     *
     * @param name name
     * @return Integer
     */
    Integer userPageCount(String name);

    Integer selectAllCount();

    List<KeyValue> selectByUserName(String userName);

    void changePicture(UserEntity user, String imagePath);

    List<UserEntity> selectByIds(List<Integer> ids);

    UserEntity selectById(Integer userId);
}
