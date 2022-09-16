package com.jdpu.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jdpu.common.entity.authority.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
