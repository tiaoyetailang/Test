package com.cl.user.service;

import com.cl.user.mapper.UserMapper;
import com.cl.user.pojo.User;
import com.cl.utils.CodecUtils;
import com.cl.utils.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    AmqpTemplate amqpTemplate;


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    final static  String key_prefix="user:verify";

    public Boolean checkUser(String data, Integer type) {
        User user = new User();
        if(type==1){
            user.setUsername(data);
        }else if(type==2){
            user.setPhone(data);

        }else {
            return null;
        }
        return   this.userMapper.selectCount(user)==0;
    }

    public void sendVerifyCode(String phone) {
        if(StringUtils.isBlank(phone)){
            return ;
        }
        String code = NumberUtils.generateCode(6);
        Map<String,String> mes=new HashMap<>();
        mes.put("phone",phone);
        mes.put("code",code);
              this.amqpTemplate.convertAndSend("leyou.sms.exchange","sms.verify.code",mes);

         stringRedisTemplate.opsForValue().set(key_prefix+phone,code,5, TimeUnit.MINUTES);

    }

    public void register(User user, String code) {
        String redisCode = this.stringRedisTemplate.opsForValue().get(key_prefix + user.getPhone());
        if(!StringUtils.equals(redisCode,code)){
            return ;
        }
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        user.setId(null);
        user.setCreated(new Date());
        userMapper.insertSelective(user);

    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if(user==null){
            return null;
        }
      if(StringUtils.equals(CodecUtils.md5Hex(password,user.getSalt()),user.getPassword())){
          return user;
      }
      return null;

    }
}
