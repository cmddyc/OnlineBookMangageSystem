package book_sys.service;

import book_sys.dao.interf.t_admin;
import book_sys.entity.TAdmin;
import book_sys.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class admin_manage_service {
    @Autowired
    t_admin admin;
    public Map<String, Object> adminInfoChange(TAdmin tAdmin){
        Map<String, Object> result=new HashMap<>();
        try {
            admin.change(tAdmin);
            result.put("state","success");
        }
        catch(Exception e){

            result.put("state","fail");
        }
        return result;

    }
    public Map<String, Object> adminInfoSearch(TAdmin tAdmin){
        Map<String, Object> result=new HashMap<>();
        try {
            result.put("result",admin.search(tAdmin));
            result.put("state","success");
        }
        catch(Exception e){
            result.put("state","fail");
        }
        return result;
    }
    public Map<String, Object> adminInfoDelete(TAdmin tAdmin){
        Map<String, Object> result=new HashMap<>();
        try {
            result.put("result",admin.delete(tAdmin));
            result.put("state","success");
        }
        catch(Exception e){
            result.put("state","fail");
        }
        return result;
    }
}
