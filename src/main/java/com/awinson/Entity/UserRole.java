package com.awinson.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by winson on 2016/12/10.
 */
@Entity
@Table(name="sys_user_role")
public class UserRole {
    private String id;
    private String userId;
    private String roleId;
    @Id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="sys_user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name="sys_role_id")
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
