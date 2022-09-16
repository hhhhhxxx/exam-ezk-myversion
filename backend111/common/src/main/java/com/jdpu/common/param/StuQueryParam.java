package com.jdpu.common.param;

import com.jdpu.common.xzsOld.base.BasePage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuQueryParam extends BasePage implements Serializable {
    private Integer userId;
    private String name;
    private Integer classId;
}
