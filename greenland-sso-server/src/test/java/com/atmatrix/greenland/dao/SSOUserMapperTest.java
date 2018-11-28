package com.atmatrix.greenland.dao;

import com.atmatrix.greenland.dao.po.SSOUser;
import com.atmatrix.greenland.util.PasswordProvider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class SSOUserMapperTest extends BaseTest{


    @Autowired
    private SSOUserMapper ssoUserMapper;

    @Test
    public void selectByExample() {
        List<SSOUser> toInsert = new ArrayList<>();


        SSOUser ssoUser1 = new SSOUser();
        SSOUser ssoUser2 = new SSOUser();
        ssoUser1.setUserId(1L);
        ssoUser2.setUserId(2L);

        ssoUser1.setPassword(PasswordProvider.encrypt(1+""));
        ssoUser1.setPassword(PasswordProvider.encrypt(2+""));

        toInsert.add(ssoUser1);
        toInsert.add(ssoUser2);
        ssoUserMapper.batchInsert(toInsert);
    }
}