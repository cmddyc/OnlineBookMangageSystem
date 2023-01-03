package book_sys.controller;

import book_sys.entity.TUser;
import book_sys.service.user_login_service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class user_login {

    @Autowired
    user_login_service userLoginService;
    @PostMapping ("/login")
    @CrossOrigin
    public Map<String, Object> check(@RequestBody TUser request){

        Map<String, Object> result=new HashMap<>();

//        userLoginService.tUserRequest.setUserId(request.getUserId());
//        userLoginService.tUserRequest.setPassword(request.getPassword());
        result=userLoginService.check(request);

        return result;
    }
}
