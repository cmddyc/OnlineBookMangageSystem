package book_sys.service;

import book_sys.dao.interf.t_admin;
import book_sys.dao.interf.t_user;
import book_sys.entity.TAdmin;
import book_sys.entity.TUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class user_login_service {


    @Autowired(required = false)
    private t_user user;
    @Autowired(required = false)
    private t_admin admin;



    public Map<String, Object> check(TUser tUserRequest){

        Map<String, Object> result=new HashMap<>();

        if(tUserRequest.getUserId().equals("<none>")||tUserRequest.getPassword().equals("<none>")){
            result.put("result","no id/password input");
            return result;
        }
        tUserRequest.setPassword(DigestUtils.md5DigestAsHex(tUserRequest.getPassword().getBytes()));

        List<TUser> list;
        TAdmin tAdmin=new TAdmin();
        TUser tUserS=new TUser();
        tUserS.setUserId("<value>'"+tUserRequest.getUserId()+"'");
        tUserS.setPassword("<query>");
        tUserS.setUserName("<none>");
        tUserS.setAge("<none>");
        tUserS.setCredit("<none>");
        tUserS.setEmail("<none>");
        tUserS.setSex("<none>");
        tUserS.setTelephone("<none>");
        try {
            list=user.search(0, -1, tUserS);
            if(list.get(0).getPassword().equals(tUserRequest.getPassword())){
                result.put("result","user");
                result.put("state","success");
                return result;
            }
            else{
                result.put("state","fail");
                return result;
            }
        }
        catch(Exception e){
            tAdmin.setAdminId("<value>'"+tUserRequest.getUserId()+"'");
            tAdmin.setAdminPassword("<query>");
            tAdmin.setAdminPhone("<none>");
            tAdmin.setAdminEmail("<none>");
            tAdmin.setAdminName("<none>");
        }

        try{

            List<TAdmin> list_admin=admin.search(tAdmin);

            if(list_admin.get(0).getAdminPassword().equals(tUserRequest.getPassword())){
                result.put("result","admin");
                result.put("state","success");
            }
            else{
                result.put("state","fail");
                return result;
            }
        }
        catch(Exception e) {
            result.put("state","fail");
        }
        return result;
    }
}
