package com.atmatrix.greenland.controller;

import com.atmatrix.greenland.core.login.SSOTokenLoginHelper;
import com.atmatrix.greenland.core.model.UserInfo;
import com.atmatrix.greenland.core.result.ReturnT;
import com.atmatrix.greenland.core.store.SsoLoginStore;
import com.atmatrix.greenland.core.store.SsoSessionIdHelper;
import com.atmatrix.greenland.core.user.GreenlandSSOUser;
import com.atmatrix.greenland.service.UserService;
import com.atmatrix.greenland.service.vo.LoginVO;
import com.atmatrix.greenland.util.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * sso server (for app)
 *
 * @author xuxueli 2018-04-08 21:02:54
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private UserService userService;


    @RequestMapping("/login")
    @ResponseBody
    public ReturnT<String> login(String userId, String password) {

        // valid login
        ReturnT<UserInfo> result = userService.findUser(userId, password);
        if (result.getCode() != ReturnT.SUCCESS_CODE) {
            return new ReturnT<String>(result.getCode(), result.getMsg());
        }

        // 1、make greenland-sso user
        GreenlandSSOUser ssoUser = new GreenlandSSOUser();
        ssoUser.setUserid(String.valueOf(result.getData().getUserId()));
        ssoUser.setUsername(result.getData().getUsername());
        ssoUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        ssoUser.setExpireMinite(SsoLoginStore.getRedisExpireMinite());
        ssoUser.setExpireFreshTime(System.currentTimeMillis());


        // 2、generate sessionId + storeKey
        String sessionId = SsoSessionIdHelper.makeSessionId(ssoUser);

        // 3、login, store storeKey
        SSOTokenLoginHelper.login(sessionId, ssoUser);

        // 4、return sessionId
        return new ReturnT<String>(sessionId);
    }
    /**
     * Login Get
     *
     * @param password
     * @return
     */
    @RequestMapping("/login1")
    @ResponseBody
    public ResponseEntity<Map<String, Object>>  login1(String userId, String password) {

        // valid login
        ReturnT<UserInfo> reource = userService.findUser(userId, password);
        if (reource.getCode() != ReturnT.SUCCESS_CODE) {
            return WebUtil.result(reource);
        }
        // 1、make greenland-sso user
        GreenlandSSOUser ssoUser = new GreenlandSSOUser();
        ssoUser.setUserid(String.valueOf(reource.getData().getUserId()));
        ssoUser.setUsername(reource.getData().getUserId());
        ssoUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        ssoUser.setExpireMinite(SsoLoginStore.getRedisExpireMinite());
        ssoUser.setExpireFreshTime(System.currentTimeMillis());
        // 2、generate sessionId + storeKey
        String sessionId = SsoSessionIdHelper.makeSessionId(ssoUser);
        // 3、login, store storeKey
        SSOTokenLoginHelper.login(sessionId, ssoUser);
        // 4、return sessionId

        Map<String, Object> data = new HashMap<>();
        data.put("sessionId",sessionId);
        data.put("userId",reource.getData().getUserId());
        data.put("type",reource.getData().getUserType());
//        return new ReturnT<String>(sessionId+"_"+reource.getData().getUserId()+"_"+reource.getData().getUserType());
        return WebUtil.result(data);
    }


    /**
     * Login Get
     *
     * @param loginVO
     * @return
     */
    @RequestMapping("/login2")
    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> loginPost(@RequestBody LoginVO loginVO) {

        // valid login
        if (loginVO == null) {
            return WebUtil.error("loginVO为空");
        }
        ReturnT<UserInfo> reource = userService.findUser(loginVO.getUserId(), loginVO.getPassword());
        if (reource.getCode() != ReturnT.SUCCESS_CODE) {
            return WebUtil.result(reource);
        }
        // 1、make greenland-sso user
        GreenlandSSOUser ssoUser = new GreenlandSSOUser();
        ssoUser.setUserid(String.valueOf(reource.getData().getUserId()));
        ssoUser.setUsername(reource.getData().getUserId());
        ssoUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        ssoUser.setExpireMinite(SsoLoginStore.getRedisExpireMinite());
        ssoUser.setExpireFreshTime(System.currentTimeMillis());
        // 2、generate sessionId + storeKey
        String sessionId = SsoSessionIdHelper.makeSessionId(ssoUser);
        // 3、login, store storeKey
        SSOTokenLoginHelper.login(sessionId, ssoUser);
        // 4、return sessionId
        Map<String, Object> data = new HashMap<>();
        data.put("sessionId", sessionId);
        data.put("userId", reource.getData().getUserId());
        data.put("type", reource.getData().getUserType());
//        return new ReturnT<String>(sessionId+"_"+reource.getData().getUserId()+"_"+reource.getData().getUserType());
        return WebUtil.result(data);
    }



    /**
     * Logout
     *
     * @param sessionId
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    public ReturnT<String> logout(String sessionId) {
        // logout, remove storeKey
        SSOTokenLoginHelper.logout(sessionId);
        return ReturnT.SUCCESS;
    }

    /**
     * logincheck
     *
     * @param sessionId
     * @return
     */
    @RequestMapping("/logincheck")
    @ResponseBody
    public ReturnT<GreenlandSSOUser> logincheck(String sessionId) {

        // logout
        GreenlandSSOUser ssoUser = SSOTokenLoginHelper.loginCheck(sessionId);
        if (ssoUser == null) {
            return new ReturnT<GreenlandSSOUser>(ReturnT.FAIL_CODE, "sso not login.");
        }
        return new ReturnT<GreenlandSSOUser>(ssoUser);
    }

}