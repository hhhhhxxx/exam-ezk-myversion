package com.jdpu.common.param.clazz;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserClassCreateVM {
    private Integer userId;
    private Integer classId;
    private Boolean teacher;
}
