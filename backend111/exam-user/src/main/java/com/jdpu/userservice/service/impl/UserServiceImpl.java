package com.jdpu.userservice.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.param.user.UserIdsVM;
import com.jdpu.common.xzsOld.entities.KeyValue;
import com.jdpu.userservice.client.UserClient;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.userservice.context.WebContext;
import com.jdpu.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    // TODO 修改缓存名字
    private final static String CACHE_NAME = "exam:user";

    @Autowired
    public WebContext webContext;

    @Autowired
    public UserClient userClient;


    @Override
    public List<UserEntity> getUsers() {
        // return userMapper.getAllUser();
        return null;
    }

    @Override
    public UserEntity getUserById(Integer id) {
        // return userMapper.getUserById(id);

        return null;
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#username", unless = "#result == null")
    public UserEntity getUserByUserName(String username) {
        // return userMapper.getUserByUserName(username);
        return null;
    }



    @Override
    public UserEntity getUserByUserNamePwd(String username, String pwd) {
        // return userMapper.getUserByUserNamePwd(username, pwd);
        return null;
    }


    @Override
    public Integer userPageCount(String name) {
        // Map<String, Object> map = new HashMap<>(1);
        // map.put("name", name);
        // return userMapper.userPageCount(map);
        return null;
    }



    @Override
    public Integer selectAllCount() {
        // return userMapper.selectAllCount();
        return null;
    }

    @Override
    public List<KeyValue> selectByUserName(String userName) {
        // return userMapper.selectByUserName(userName);
        return null;
    }

    @Override
    public List<UserEntity> selectByIds(List<Integer> ids) {


        UserIdsVM model = new UserIdsVM();

        model.setIds(ids);

        RestResponse res = userClient.getUserByIds(model);

        if(!res.isSuccess()) {
            return Collections.emptyList();
        }

        List<UserEntity> user = (List) res.getResponse();

        return user;
    }

    @Override
    public UserEntity selectById(Integer userId) {
        RestResponse res = userClient.getUserById(userId);

        if (!res.isSuccess()) {
            return null;
        }

        UserEntity userEntity = BeanUtil.toBean(res.getResponse(), UserEntity.class);

        return userEntity;
    }


    @Override
    // 缓存清除
    @CacheEvict(value = CACHE_NAME, key = "#user.name")
    @Transactional
    public void changePicture(UserEntity user, String imagePath) {
        // User changePictureUser = new User();
        // changePictureUser.setId(user.getId());
        // changePictureUser.setImagePath(imagePath);
        // userMapper.updateByPrimaryKeySelective(changePictureUser);

        System.out.println("changePicture未实现");
    }
}
