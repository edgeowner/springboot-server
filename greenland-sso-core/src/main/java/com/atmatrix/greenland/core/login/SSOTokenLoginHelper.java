package com.atmatrix.greenland.core.login;

import com.atmatrix.greenland.core.conf.Conf;
import com.atmatrix.greenland.core.user.GreenlandSSOUser;
import com.atmatrix.greenland.core.store.SsoLoginStore;
import com.atmatrix.greenland.core.store.SsoSessionIdHelper;

import javax.servlet.http.HttpServletRequest;


public class SSOTokenLoginHelper {

    /**
     * client login
     *
     * @param sessionId
     * @param ssoUser
     */
    public static void login(String sessionId, GreenlandSSOUser ssoUser) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }

        SsoLoginStore.put(storeKey, ssoUser);
    }

    /**
     * client logout
     *
     * @param sessionId
     */
    public static void logout(String sessionId) {
        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return;
        }
        SsoLoginStore.remove(storeKey);
    }
    /**
     * client logout
     *
     * @param request
     */
    public static void logout(HttpServletRequest request) {
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        logout(headerSessionId);
    }


    /**
     * login check
     *
     * @param sessionId
     * @return
     */
    public static GreenlandSSOUser loginCheck(String  sessionId){

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return null;
        }

        GreenlandSSOUser ssoUser = SsoLoginStore.get(storeKey);
        if (ssoUser != null) {
            String version = SsoSessionIdHelper.parseVersion(sessionId);
            if (ssoUser.getVersion().equals(version)) {
                // After the expiration time has passed half, Auto refresh
                if ((System.currentTimeMillis() - ssoUser.getExpireFreshTime()) > ssoUser.getExpireMinite()/2) {
                    ssoUser.setExpireFreshTime(System.currentTimeMillis());
                    SsoLoginStore.put(storeKey, ssoUser);
                }

                return ssoUser;
            }
        }
        return null;
    }


    /**
     * login check
     *
     * @param request
     * @return
     */
    public static GreenlandSSOUser loginCheck(HttpServletRequest request){
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        return loginCheck(headerSessionId);
    }


}
