package com.cl.user.controller;

import com.cl.user.pojo.User;
import com.cl.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AmqpTemplate amqpTemplate;


    @RequestMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data")String data,@PathVariable("type")Integer type){

       Boolean bool= this.userService.checkUser(data,type);
       if(bool==null){
            return ResponseEntity.badRequest().build();
       }
       return ResponseEntity.ok(bool);

    }

    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone")String phone){
        this.userService.sendVerifyCode(phone);
        return  ResponseEntity.status(HttpStatus.CREATED).build();



    }
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code){
        this.userService.register(user,code);
        return  ResponseEntity.status(HttpStatus.CREATED).build();

    }
     @GetMapping("query")
    public  ResponseEntity<User> queryUser(@RequestParam("username")String username,@RequestParam("password")String password){
          User user=   this.userService.queryUser(username,password);
          if(user==null){
              return ResponseEntity.badRequest().build();
          }
return ResponseEntity.ok(user);

    }
}
