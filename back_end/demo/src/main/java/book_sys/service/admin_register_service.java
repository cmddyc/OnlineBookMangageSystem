package book_sys.service;

import book_sys.dao.interf.t_admin;
import book_sys.entity.TAdmin;
import book_sys.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class admin_register_service {
    @Autowired
    t_admin admin;
    public Map<String, Object> register(TAdmin request){
        TAdmin tUser=new TAdmin();
        Map<String, Object> result=new HashMap<>();
        int i=0;
        if(request.getAdminId()!=null) {
            tUser.setAdminId(request.getAdminId());
            i += 1;
        }
        if(request.getAdminPassword()!=null) {
            tUser.setAdminPassword(DigestUtils.md5DigestAsHex(request.getAdminPassword().getBytes()));
            i += 1;
        }
        if(request.getAdminName()!=null) {
            tUser.setAdminName(request.getAdminName());
            i += 1;
        }
        if(request.getAdminEmail()!=null) {
            tUser.setAdminEmail(request.getAdminEmail());
            i += 1;
        }

        if(request.getAdminPhone()!=null) {
            tUser.setAdminPhone(request.getAdminPhone());
            i += 1;
        }
        if(i!=5){
            result.put("state","some parameter null");
            return result;
        }
        else{
            try{
                admin.add(tUser);
                result.put("state","success");
                return result;
            }
            catch(Exception e){
                result.put("state","fail");
            }
        }
        return result;
    }
}
