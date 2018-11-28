package com.atmatrix.greenland.dao.po;

import lombok.Data;

import java.util.Date;


public class SSOUser {
    private Long id;

    private Long userId;

    private String password;

    private Integer status;

    private Integer type;

    private Date createTime;

    private Date updateTime;

    public SSOUser() {
    }

    public SSOUser(Long id, Long userId, String password, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}