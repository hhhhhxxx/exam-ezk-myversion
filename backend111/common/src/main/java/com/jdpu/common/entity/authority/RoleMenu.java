package com.jdpu.common.entity.authority;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("role_menu_relation")
public class RoleMenu  extends AuthorityBasePO {

    private Integer menuId;

    private Integer roleId;
}
