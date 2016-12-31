package com.awinson.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by 10228 on 2016/12/29.
 */
@Entity
@Table(name="user_log")
public class UserLog {
    private String id;
    private String userId;
    private String type;
    private Timestamp createTimestamp;
    private String context;
    public UserLog() {
    }

    public UserLog( String userId, String type,String context) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.type = type;
        this.createTimestamp = new Timestamp(System.currentTimeMillis());
        this.context = context;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

   }
