package book_sys.controller;

import book_sys.entity.TAdmin;
import book_sys.entity.TBookComment;
import book_sys.entity.TUser;
import book_sys.service.admin_manage_service;
import book_sys.service.user_login_service;
import book_sys.service.user_manage_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class personnalInfoManage {
    @Autowired
    admin_manage_service adminManageService;
    @Autowired
    user_manage_service userManageService;
    @Autowired
    user_login_service userLoginService;
    @PostMapping("/userInfoChange")
    public Map<String, Object> userInfoChange(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam String userId,@RequestParam("newId") String newId,@RequestParam("name") String name,@RequestParam("password") String password,@RequestParam("age") String age,@RequestParam("sex") String sex,@RequestParam("telephone") String telephone,@RequestParam("email") String email,@RequestParam("credit") String credit) {
        TUser tUserRequest = new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult = new HashMap<>();
        checkResult = userLoginService.check(tUserRequest);
        TUser tUser = new TUser();
        if (checkResult.get("state").equals("fail")) {
            return checkResult;
        } else if (checkResult.get("result").equals("admin")) {
            if (!userId.equals("no")) {
                if(!newId.equals("no"))
                    tUser.setUserId("<set>'" + newId + "'<value>'" + userId + "'");
                else{
                    tUser.setUserId("<value>'" + userId + "'");
                }
            }
            if(!credit.equals("no")){
                tUser.setCredit("<set>'"+credit+"'");
            }
        } else {

            if (!newId.equals("no")) {
                tUser.setUserId("<set>'" + newId + "'<value>'" + id + "'");
            } else {
                tUser.setUserId("<value>'" + id + "'");
            }
        }
        if (!name.equals("no")) {
            tUser.setUserName("<set>'" + name + "'");
        }
        if (!password.equals("no")) {
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            tUser.setPassword("<set>'" + password + "'");
        }
        if (!age.equals("no")) {
            tUser.setAge("<set>'" + age + "'");
        }
        if (!sex.equals("no")) {
            tUser.setSex("<set>'" + sex + "'");
        }
        if (!telephone.equals("no")) {
            tUser.setTelephone("<set>'" + telephone + "'");
        }
        if (!email.equals("no")) {
            tUser.setEmail("<set>'" + email + "'");
        }
        return userManageService.userInfoChange(tUser);


    }
    @PostMapping ("/userInfoSearch")
    Map<String, Object> userInfoSearch(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("userId") String userId){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        TUser tUser=new TUser();
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            if(!userId.equals("no")){
                tUser.setUserId("<value>'"+userId+"'");
            }
        }
        else {
            tUser.setUserId("<value>'"+id+"'");
        }
        return userManageService.userInfoSearch(tUser);
    }
    @PostMapping ("/userInfoDelete")
    Map<String, Object> userInfoDelete(@RequestParam("id") String id,@RequestParam("token") String token, @RequestParam("userId") String userId){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        TUser tUser=new TUser();
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            tUser.setUserId("<value>'"+userId+"'");
        }
        else {
            tUser.setUserId("<value>'"+id+"'");
        }
        return userManageService.userInfoDelete(tUser);
    }


    @PostMapping("/admin/adminInfoChange")
    public Map<String, Object> adminInfoChange(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("newId") String newId,@RequestParam("name") String name,@RequestParam("password") String password,@RequestParam("telephone") String telephone,@RequestParam("email") String email){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            TAdmin tAdmin=new TAdmin();
            if(!newId.equals("no")){
                tAdmin.setAdminId("<set>'"+newId+"'<value>'"+id+"'");
            }
            if(!name.equals("no")){
                tAdmin.setAdminName("<set>'"+name+"'");
            }
            if(!password.equals("no")){
                password= DigestUtils.md5DigestAsHex(password.getBytes());
                tAdmin.setAdminPassword("<set>'"+password+"'");
            }
            if(!telephone.equals("no")){
                tAdmin.setAdminPhone("<set>'"+telephone+"'");
            }
            if(!email.equals("no")){
                tAdmin.setAdminEmail("<set>'"+email+"'");
            }
            return adminManageService.adminInfoChange(tAdmin);
        }
        else{
            checkResult.put("state", "fail");
            return checkResult;

        }

    }
    @PostMapping ("/admin/adminInfoSearch")
    @CrossOrigin
    Map<String, Object> adminInfoSearch(@RequestParam("id") String id,@RequestParam("token") String token){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        TAdmin tAdmin=new TAdmin();
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            tAdmin.setAdminId("<value>'"+id+"'");
            return adminManageService.adminInfoSearch(tAdmin);
        }
        else {
            checkResult.put("state", "fail");
            return checkResult;
        }
    }
    @PostMapping ("/admin/adminInfoDelete")
    Map<String, Object> adminInfoDelete(@RequestParam("id") String id,@RequestParam("token") String token){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        TAdmin tAdmin=new TAdmin();
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            tAdmin.setAdminId("<value>'"+id+"'");
            return adminManageService.adminInfoDelete(tAdmin);
        }
        else {
            checkResult.put("state", "fail");
            return checkResult;
        }
    }
}
