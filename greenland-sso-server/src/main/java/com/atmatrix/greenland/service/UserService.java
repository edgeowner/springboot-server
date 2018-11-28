package com.atmatrix.greenland.service;

import com.atmatrix.greenland.core.result.ReturnT;
import com.atmatrix.greenland.core.model.UserInfo;

public interface UserService {

    public ReturnT<UserInfo> findUser(String username, String password);

    void initSSOUser();
}
