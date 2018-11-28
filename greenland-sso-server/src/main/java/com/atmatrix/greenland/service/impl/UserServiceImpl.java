package com.atmatrix.greenland.service.impl;

import com.atmatrix.greenland.core.model.UserInfo;
import com.atmatrix.greenland.core.result.ReturnT;
import com.atmatrix.greenland.dao.SSOUserMapper;
import com.atmatrix.greenland.dao.UserBaseMapper;
import com.atmatrix.greenland.dao.po.SSOUser;
import com.atmatrix.greenland.dao.po.SSOUserExample;
import com.atmatrix.greenland.dao.po.UserBase;
import com.atmatrix.greenland.service.UserService;
import com.atmatrix.greenland.util.PasswordProvider;
import com.atmatrix.greenland.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private SSOUserMapper ssoUserMapper;

    @Autowired
    private UserBaseMapper userBaseMapper;


    @Override
    public ReturnT<UserInfo> findUser(String userId, String password) {

        if (userId == null || userId.trim().length() == 0 || !StringUtil.isNumeric(userId)) {
            return new ReturnT<UserInfo>(ReturnT.FAIL_CODE, "请输入正确的用户ID");
        }
        if (password == null || password.trim().length() == 0) {
            return new ReturnT<UserInfo>(ReturnT.FAIL_CODE, "请输入正确的密码");
        }
        //查询用户账户和密码


        UserBase userBase = userBaseMapper.selectByPrimaryKey(Long.valueOf(userId));
        if (userBase == null) {
            return new ReturnT<UserInfo>(ReturnT.FAIL_CODE, "不存在该用户");
        }
        List<SSOUser> ssoUsers = ssoUserMapper.selectByUserId(userBase.getId());


        if (!CollectionUtils.isEmpty(ssoUsers)) {
            SSOUser ssoUser = ssoUsers.get(0);
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(ssoUser.getUserId().toString());
            userInfo.setUserType(userBase.getType());
            if (ssoUser.getPassword().equals(PasswordProvider.encrypt(password))) {
                return new ReturnT<UserInfo>(userInfo);
            } else {
                return new ReturnT<UserInfo>(ReturnT.FAIL_CODE, "密码不正确");
            }
        } else {
            return new ReturnT<UserInfo>(ReturnT.FAIL_CODE, "不存在该用户");
        }

    }

    @Override
    public void initSSOUser() {
////        List<UserBase> all = userBaseMapper.getAll();
//        List<SSOUser> toInsert = new ArrayList<>();
//        SSOUser ssoUser;
//        for (UserBase userBase : all) {
//            ssoUser = new SSOUser();
//            ssoUser.setUserId(userBase.getId());
//            ssoUser.setPassword(PasswordProvider.encrypt(userBase.getId() + ""));
//            toInsert.add(ssoUser);
//        }
//        if (!CollectionUtils.isEmpty(toInsert)) {
//            ssoUserMapper.batchInsert(toInsert);
//        }


    }


}
