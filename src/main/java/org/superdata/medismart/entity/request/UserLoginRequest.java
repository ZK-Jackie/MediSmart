package org.superdata.medismart.entity.request;

import lombok.Data;

@Data
public class UserLoginRequest {

    // 用户名
    private String username;

    // 密码
    private String password;

    // 验证码
    private String code;

    // uuid
    private String uuid;
}
