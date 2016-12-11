package com.awinson.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by winson on 2016/12/10.
 */
@Entity
@Table(name = "sys_user")
public class User {
    private String id;
    private String usercode;
    private String username;
    private String password;
    private String salt;
    private String lockded;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getLockded() {
        return lockded;
    }

    public void setLockded(String lockded) {
        this.lockded = lockded;
    }
}
