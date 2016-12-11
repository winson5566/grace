package com.awinson.valid;

import javax.validation.constraints.Size;

/**
 * Created by winson on 2016/12/10.
 */
public class RegisterValid {
    @Size(min = 4,max = 11,message = "用户名格式不正确!")
    private String username;
    @Size(min = 4,max = 30,message = "密码最少4位!")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
