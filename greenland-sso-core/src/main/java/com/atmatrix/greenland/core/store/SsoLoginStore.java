package com.atmatrix.greenland.core.store;

import com.atmatrix.greenland.core.conf.Conf;
import com.atmatrix.greenland.core.user.GreenlandSSOUser;
import com.atmatrix.greenland.core.util.JedisUtil;

public class SsoLoginStore {

    private static int redisExpireMinite = 1440;    // 1440 minite, 24 hour
    public static void setRedisExpireMinite(int redisExpireMinite) {
        if (redisExpireMinite < 30) {
            redisExpireMinite = 30;
        }
        SsoLoginStore.redisExpireMinite = redisExpireMinite;
    }
    public static int getRedisExpireMinite() {
        return redisExpireMinite;
    }

    /**
     * get
     *
     * @param storeKey
     * @return
     */
    public static GreenlandSSOUser get(String storeKey) {

        String redisKey = redisKey(storeKey);
        Object objectValue = JedisUtil.getObjectValue(redisKey);
        if (objectValue != null) {
            GreenlandSSOUser ssoUser = (GreenlandSSOUser) objectValue;
            return ssoUser;
        }
        return null;
    }

    /**
     * remove
     *
     * @param storeKey
     */
    public static void remove(String storeKey) {
        String redisKey = redisKey(storeKey);
        JedisUtil.del(redisKey);
    }

    /**
     * put
     *
     * @param storeKey
     * @param greenlandUser
     */
    public static void put(String storeKey, GreenlandSSOUser greenlandUser) {
        String redisKey = redisKey(storeKey);
        JedisUtil.setObjectValue(redisKey, greenlandUser, redisExpireMinite * 60);  // minite to second
    }

    private static String redisKey(String sessionId){
        return Conf.SSO_SESSIONID.concat("#").concat(sessionId);
    }

}
