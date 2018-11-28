package com.atmatrix.greenland.config;

import com.atmatrix.greenland.core.store.SsoLoginStore;
import com.atmatrix.greenland.core.util.JedisUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GreenlandSSORedisConfig implements InitializingBean, DisposableBean {

    @Value("${greenland.sso.redis.address}")
    private String redisAddress;

    @Value("${greenland.sso.redis.expire.minite}")
    private int redisExpireMinite;

    @Override
    public void afterPropertiesSet() throws Exception {
        SsoLoginStore.setRedisExpireMinite(redisExpireMinite);
        JedisUtil.init(redisAddress);
    }

    @Override
    public void destroy() throws Exception {
        JedisUtil.close();
    }

}
