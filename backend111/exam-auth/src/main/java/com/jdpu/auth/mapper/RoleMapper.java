package com.jdpu.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jdpu.common.entity.authority.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
