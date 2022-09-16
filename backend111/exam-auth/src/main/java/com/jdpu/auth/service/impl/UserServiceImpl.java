package com.jdpu.auth.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.jdpu.auth.entiy.UserClass;
import com.jdpu.auth.mapper.UserMapper;
import com.jdpu.auth.service.IRoleService;
import com.jdpu.auth.service.IUserClassService;
import com.jdpu.auth.service.IUserRoleService;
import com.jdpu.auth.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.constant.RoleNameEnum;
import com.jdpu.common.dto.UserDTO;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.entity.authority.Role;
import com.jdpu.common.entity.authority.UserRole;
import com.jdpu.common.param.StuQueryParam;
import com.jdpu.common.param.TeaQueryParam;
import com.jdpu.common.param.UserQueryParam;
import com.jdpu.common.param.user.UserPageRequestVM;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
    /**
     * Authorization认证开头是"bearer "
     */
    private static final String BEARER = "Bearer ";

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    // xzy-authority内容
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IUserClassService userClassService;

    /**
     * jwt token 密钥，主要用于token解析，签名验证
     */
    @Value("${spring.security.oauth2.jwt.signingKey:123456}")
    private String signingKey;


    @Override
    public UserDTO getUserById(Integer userId) {
        UserEntity user = this.getById(userId);
        if (null == user) {
            return null;
        }
        UserDTO dto = new UserDTO();
        BeanUtil.copyProperties(user, dto);
        return dto;
    }

    @Override
    public UserDTO getUserByPhone(String phone) {
        List<UserEntity> list = this.lambdaQuery().eq(UserEntity::getPhone, phone)
                .orderByDesc(UserEntity::getId).list();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        UserDTO dto = new UserDTO();
        BeanUtil.copyProperties(list.get(0), dto);
        return dto;
    }

    @Override
    public UserEntity getUserEntityByPhone(String phone) {

        UserEntity user = this.lambdaQuery().eq(UserEntity::getPhone, phone).one();

        if (null == user) {
            return null;
        }
        return user;
    }

    @Override
    public Page<UserDTO> getUserPages(UserQueryParam userQueryParam) {

        String phone = userQueryParam.getPhone();
        Integer userId = userQueryParam.getUserId();
        Integer currentPage = userQueryParam.getCurrentPage();
        Integer pageSize = userQueryParam.getPageSize();
        Date startCreateTime = userQueryParam.getStartCreateTime();
        Date endCreateTime = userQueryParam.getEndCreateTime();
        Page<UserEntity> page = new Page<>(currentPage, pageSize);
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        //根据课程名称查询
        if (StringUtils.isNotBlank(phone)) {
            queryWrapper.like("phone", phone);
        }
        if (null != startCreateTime && null != endCreateTime) {
            queryWrapper.ge("create_time", startCreateTime);
            queryWrapper.le("create_time", endCreateTime);
        }
        if (null != userId && userId > 0) {
            queryWrapper.eq("id", userId);
        }
        //根据课程状态查询
        int count = this.count(queryWrapper);
        queryWrapper.orderByDesc("id");
        IPage<UserEntity> selectPage = this.getBaseMapper().selectPage(page, queryWrapper);

        List<UserDTO> userDTOList = new ArrayList<>();
        //获取课程对应的模块的信息
        for (UserEntity user : selectPage.getRecords()) {
            UserDTO userDTO = new UserDTO();
            BeanUtil.copyProperties(user, userDTO);
            userDTOList.add(userDTO);
        }

        Page<UserDTO> result = new Page<>();
        //分页查询结果对象属性的拷贝
        BeanUtil.copyProperties(selectPage, result);
        //设置分页结果对象record属性
        result.setRecords(userDTOList);
        result.setTotal(count);
        return result;
    }


    /**
     * 添加用户
     * @param user
     * @return
     */
    @Override
    @Transactional
    public boolean saveUser(UserEntity user) {

        // 权限信息
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setStatus(1);
        // 其他信息
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDel(false);

        return this.save(user);
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @Override
    @Transactional
    public boolean updateUser(UserEntity user) {
        if (null == user.getId() || user.getId() <= 0) {
            log.info("用户id为空，无法更新");
            return false;
        }
        user.setUpdateTime(new Date());
        return this.updateById(user);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    @Transactional
    public boolean deleteUser(Integer id) {

        // TODO 消除别的表的关系

        // 逻辑删除
        return this.removeById(id);
    }

    @Override
    public boolean isRegister(String phone) {
        UserDTO user = this.getUserByPhone(phone);

        if(user == null) {
            return false;
        }
        return true;
    }




    @Override
    public Page<UserDTO> getUserPages_p(UserQueryParam userQueryParam) {
        return getUserPages(userQueryParam);
    }



    @Override
    public Page<UserDTO> getTeacherPages(TeaQueryParam userQueryParam) {
        String phone = userQueryParam.getPhone();
        Integer userId = userQueryParam.getUserId();
        Integer currentPage = userQueryParam.getPageIndex();
        Integer pageSize = userQueryParam.getPageSize();

        Role Teacher = roleService.getOne((new QueryWrapper<Role>().eq("name", "教师")));
        List<UserRole> userRoleList = new ArrayList<>();
        List<Integer> userIdList = new ArrayList<>();
        if (Teacher != null) {
            userRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("role_id", Teacher.getId()));
            for (UserRole userRole : userRoleList) {
                userIdList.add(userRole.getUserId());
            }
        }


        Page<UserEntity> page = new Page<>(currentPage, pageSize);
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();

        //查询教师角色
        if (Teacher != null) {
            queryWrapper.in("id", userIdList);
        }

        if (StringUtils.isNotBlank(phone)) {
            queryWrapper.like("phone", phone);
        }
        if (null != userId && userId > 0) {
            queryWrapper.eq("id", userId);
        }
        int count = count(queryWrapper);
        queryWrapper.orderByDesc("id");
        IPage<UserEntity> selectPage = this.getBaseMapper().selectPage(page, queryWrapper);

        List<UserDTO> userDTOList = new ArrayList<>();

        for (UserEntity user : selectPage.getRecords()) {
            UserDTO userDTO = new UserDTO();
            BeanUtil.copyProperties(user, userDTO);
            userDTOList.add(userDTO);
        }

        Page<UserDTO> result = new Page<>();
        //分页查询结果对象属性的拷贝
        BeanUtil.copyProperties(selectPage, result);
        //设置分页结果对象record属性
        result.setRecords(userDTOList);
        result.setTotal(count);
        return result;
    }

    @Override
    public Page<UserDTO> getStuPages(StuQueryParam stuQueryParam) {

        Integer userId = stuQueryParam.getUserId();
        Integer currentPage = stuQueryParam.getPageIndex();
        Integer pageSize = stuQueryParam.getPageSize();
        Integer classId = stuQueryParam.getClassId();//如果这个参数不为空，则查询该班级的
        //如果没有，则查询未加入班级的

        Page<UserEntity> page = new Page<>(currentPage, pageSize);
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();

        Role Stu = roleService.getOne((new QueryWrapper<Role>().eq("name", "学生")));//获取学生角色
        List<UserRole> userRoleList = new ArrayList<>();
        List<Integer> userIdList = new ArrayList<>();
        //查询拥有班级的学生列表id
        List<UserClass> userClassList = new ArrayList<>();

        if (classId == null) {
            //查询未加入班级的
            if (Stu != null) {
                //查询拥有学生角色的用户
                userRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("role_id", Stu.getId()));

                for (UserRole userRole : userRoleList) {
                    userIdList.add(userRole.getUserId());
                }

                //如果没有学生用户
                if (userIdList.isEmpty()) {
                    return new Page<UserDTO>();
                }

                userClassList = userClassService.list(new QueryWrapper<UserClass>().in("user_id", userIdList));

                List<Integer> HaveClassStuList = new ArrayList<>();
                for (UserClass userClass : userClassList) {
                    HaveClassStuList.add(userClass.getUserId());
                }



                //查询没有班级的学生的条件
                if (!HaveClassStuList.isEmpty()) {
                    queryWrapper.notIn("id", HaveClassStuList);
                }
            }

        } else {

            //查询该班级的学生
            List<UserClass> userClasses = userClassService.list(new QueryWrapper<UserClass>().eq("class_id", classId));
            if (userClasses.size() == 0) {
                return new Page<UserDTO>();
            }
            List<Integer> ClassStuList = new ArrayList<>();//本班级学生的id
            for (UserClass userClass : userClasses) {
                ClassStuList.add(userClass.getUserId());
            }
            //查询本班级学生的条件
            if (!ClassStuList.isEmpty()) {
                queryWrapper.in("id", ClassStuList);
            }

        }

        if (userId != null) {
            queryWrapper.like("id", userId);
        }
        int count = this.count(queryWrapper);
        queryWrapper.orderByDesc("id");
        IPage<UserEntity> selectPage = this.getBaseMapper().selectPage(page, queryWrapper);

        List<UserDTO> userDTOList = new ArrayList<>();

        for (UserEntity user : selectPage.getRecords()) {
            UserDTO userDTO = new UserDTO();
            BeanUtil.copyProperties(user, userDTO);
            userDTOList.add(userDTO);
        }
        Page<UserDTO> result = new Page<>();
        //分页查询结果对象属性的拷贝
        BeanUtil.copyProperties(selectPage, result);
        //设置分页结果对象record属性
        result.setRecords(userDTOList);
        result.setTotal(count);
        return result;
    }

    @Override
    public Page<UserEntity> userPage(UserPageRequestVM requestVM) {



        Page<UserEntity> pageInfo = new Page<>(requestVM.getPageIndex(), requestVM.getPageSize());
        Page<UserEntity> resultPage = null;
        if (requestVM.getRoleId() != null && requestVM.getRoleId().intValue() != 0) {
            // 根据roleId查每一类用户
            resultPage = (Page<UserEntity>) this.baseMapper.queryUserByRoleId(pageInfo, requestVM.getUserName(), requestVM.getRoleId());
        } else if(requestVM.getRoleId() != null && requestVM.getRoleId().intValue() == 0) {
            // 无角色用户
            resultPage = (Page<UserEntity>) this.baseMapper.queryUserNoRole(pageInfo, requestVM.getUserName());
        } else {
            // 全部用户
            resultPage = (Page<UserEntity>) this.baseMapper.queryUserByRoleId(pageInfo, requestVM.getUserName(), null);
        }

        return resultPage;
    }


    @Override
    public Page<UserEntity> teacherPage(TeaQueryParam teaQueryParam) {

        Role teacherRole = roleService.lambdaQuery().eq(Role::getName, RoleNameEnum.TEACHER.getName()).one();

        Page<UserEntity> pageInfo = new Page<>(teaQueryParam.getPageIndex(), teaQueryParam.getPageSize());


        if(teaQueryParam.getClassId() == null) {

            // this.baseMapper.queryUserNoInClass(pageInfo, null, teacherRole.getId());

            // 老师可以加入多个班级 就不查询不在班级的时候
            List<UserRole> userRoleList = userRoleService.lambdaQuery()
                    .eq(UserRole::getRoleId, teacherRole.getId())
                    .select(UserRole::getUserId)
                    .list();

            if(userRoleList.isEmpty()) {
                return pageInfo;
            }

            List<Integer> ids = userRoleList.stream().map(UserRole::getUserId).collect(Collectors.toList());

            Page<UserEntity> page = this.lambdaQuery().in(UserEntity::getId, ids).page(pageInfo);

            return page;

        } else {

            this.baseMapper.queryUserInClass(pageInfo,teaQueryParam.getName(),teacherRole.getId(),teaQueryParam.getClassId());
        }

        return pageInfo;

    }

    @Override
    public Page<UserEntity> studentPage(StuQueryParam stuQueryParam) {

        Role studentRole = roleService.lambdaQuery().eq(Role::getName, RoleNameEnum.STUDENT.getName()).one();

        Page<UserEntity> pageInfo = new Page<>(stuQueryParam.getPageIndex(), stuQueryParam.getPageSize());

        if(stuQueryParam.getClassId() == null) {

            pageInfo = (Page<UserEntity>) this.baseMapper.queryUserNoInClass(pageInfo, null, studentRole.getId());

        } else {

            pageInfo = (Page<UserEntity>) this.baseMapper.queryUserInClass(pageInfo,stuQueryParam.getName(),studentRole.getId(),stuQueryParam.getClassId());
        }

        return pageInfo;
    }

    @Override
    public List<UserEntity> getUserByIds(List<Integer> userIds) {
        if(userIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<UserEntity> userList = this.lambdaQuery().in(UserEntity::getId, userIds).list();


        if(userList.isEmpty()) {
            return Collections.emptyList();
        }

        return userList;
    }

    @Override
    public List<UserEntity> getTeacherList() {

        Role teacherRole = roleService.lambdaQuery().eq(Role::getName, RoleNameEnum.TEACHER.getName()).one();

        List<UserRole> userRoleList = userRoleService.lambdaQuery()
                .eq(UserRole::getRoleId, teacherRole.getId())
                .select(UserRole::getUserId)
                .list();

        if(userRoleList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> ids = userRoleList.stream().map(UserRole::getUserId).collect(Collectors.toList());

        List<UserEntity> list = this.lambdaQuery().in(UserEntity::getId, ids).list();

        return list;
    }
}
