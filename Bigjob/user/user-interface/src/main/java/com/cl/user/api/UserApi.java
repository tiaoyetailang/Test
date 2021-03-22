package com.cl.user.api;

import com.cl.user.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


public interface UserApi {

    @GetMapping("query")
    public  User queryUser(@RequestParam("username")String username, @RequestParam("password")String password);
}
