package com.cl.SERVICE;

import com.cl.client.UserClient;
import com.cl.config.JwtProperties;
import com.cl.pojo.UserInfo;
import com.cl.user.pojo.User;
import com.cl.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service

public class AuthService {
    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    UserClient userClient;

    public String authentication(String username, String password) {
        User user = userClient.queryUser(username, password);

        if(user==null){
            return null;
        }
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
           return  JwtUtils.generateToken(userInfo,jwtProperties.getPrivateKey(),jwtProperties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
