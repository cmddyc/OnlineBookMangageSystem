package book_sys.controller;

import book_sys.entity.TAdmin;
import book_sys.entity.TUser;
import book_sys.service.admin_register_service;
import book_sys.service.user_register_service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class user_register {
    @Autowired(required = false)
    user_register_service userRegister;
    @Autowired
    admin_register_service adminRegisterService;

    @PostMapping ("/register")
    @CrossOrigin
    public Map<String, Object> register(@RequestParam("id") String id, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("age") String age, @RequestParam("sex") String sex, @RequestParam("password") String password, @RequestParam("telephone") String telephone) {
        TUser tUser=new TUser();
        Map<String, Object> result = new HashMap<>();
        tUser.setUserId(id);
        tUser.setUserName(name);
        tUser.setEmail(email);
        tUser.setAge(age);
        tUser.setSex(sex);
        tUser.setPassword(password);
        tUser.setTelephone(telephone);

        result = userRegister.register(tUser);
        return result;
    }
    @GetMapping ("/admin/register")
    public Map<String, Object> adminRegister(HttpServletRequest request) {
        TAdmin tUser=new TAdmin();
        Map<String, Object> result = new HashMap<>();
        tUser.setAdminId(request.getParameter("id"));
        tUser.setAdminName(request.getParameter("name"));
        tUser.setAdminEmail(request.getParameter("email"));
        tUser.setAdminPassword(request.getParameter("password"));
        tUser.setAdminPhone(request.getParameter("telephone"));

        result = adminRegisterService.register(tUser);
        return result;
    }
}
