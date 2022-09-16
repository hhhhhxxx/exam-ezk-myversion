package com.jdpu.common.param;


import com.jdpu.common.xzsOld.base.BasePage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeaQueryParam extends BasePage implements Serializable   {
    private String name;
    private String phone;
    private Integer userId;
    private Integer classId;
}
