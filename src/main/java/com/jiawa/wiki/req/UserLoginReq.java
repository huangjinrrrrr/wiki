package com.jiawa.wiki.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserLoginReq {
    @NotNull(message = "[用户名]不能为空")
    private String loginName;

    @NotNull(message = "[密码]不能为空")
//    @Length(min = 6,max = 20,message = "[密码]必须是6~20位")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$",message = "[密码]规则不正确")
    private String password;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserLoginReq{" +
                "loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}