package book_sys.service;

import book_sys.dao.interf.t_user;
import book_sys.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class user_manage_service {
    @Autowired
    t_user user;
    public Map<String, Object> userInfoChange(TUser tUser){
        Map<String, Object> result=new HashMap<>();
        try {
            user.change(tUser);
            result.put("state","success");
        }
        catch(Exception e){

            result.put("state","fail");
        }
        return result;

    }
    public Map<String, Object> userInfoSearch(TUser tUser){
        Map<String, Object> result=new HashMap<>();
        try {
            result.put("result",user.search(0,-1,tUser));
            result.put("state","success");
        }
        catch(Exception e){
            result.put("state","fail");
        }
        return result;
    }
    public Map<String, Object> userInfoDelete(TUser tUser){
        Map<String, Object> result=new HashMap<>();
        try {
            result.put("result",user.delete(tUser));
            result.put("state","success");
        }
        catch(Exception e){
            result.put("state","fail");
        }
        return result;
    }
}
