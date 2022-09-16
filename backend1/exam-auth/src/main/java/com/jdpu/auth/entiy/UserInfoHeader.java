package com.jdpu.auth.entiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: xJh
 * @Date: 2022/3/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoHeader {
    // 请求头对应的键值
    private String X_USER_NAME;
    private String X_USER_ID;
    private String X_USER_IP;
}
