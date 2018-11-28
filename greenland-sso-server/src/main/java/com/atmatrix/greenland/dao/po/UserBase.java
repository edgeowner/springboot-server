package com.atmatrix.greenland.dao.po;

import java.util.Date;

public class UserBase {
    private Long id;

    private Date createTime;

    private String email;

    private Long identityType;

    private Integer isDelete;

    private String name;

    private String phone;

    private String publicAddr;

    private Integer status;

    private Integer type;

    private Date updateTime;

    public UserBase(Long id, Date createTime, String email, Long identityType, Integer isDelete, String name, String phone, String publicAddr, Integer status, Integer type, Date updateTime) {
        this.id = id;
        this.createTime = createTime;
        this.email = email;
        this.identityType = identityType;
        this.isDelete = isDelete;
        this.name = name;
        this.phone = phone;
        this.publicAddr = publicAddr;
        this.status = status;
        this.type = type;
        this.updateTime = updateTime;
    }

    public UserBase() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIdentityType() {
        return identityType;
    }

    public void setIdentityType(Long identityType) {
        this.identityType = identityType;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPublicAddr() {
        return publicAddr;
    }

    public void setPublicAddr(String publicAddr) {
        this.publicAddr = publicAddr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}