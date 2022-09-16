package com.jdpu.common.param.clazz;


import com.jdpu.common.xzsOld.base.BasePage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassPageVM  extends BasePage {
    public String classNmae;
    public Boolean my;
    public Integer teacherId;
}
