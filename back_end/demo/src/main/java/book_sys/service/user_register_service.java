package book_sys.service;

import book_sys.dao.interf.t_user;
import book_sys.entity.TUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Scope(value= ConfigurableBeanFactory.SCOPE_PROTOTYPE,proxyMode = ScopedProxyMode.TARGET_CLASS)
public class user_register_service {

    @Autowired
    t_user user;


    public Map<String, Object> register(TUser request){
        TUser tUser=new TUser();
        Map<String, Object> result=new HashMap<>();
        int i=0;
        if(request.getUserId()!=null) {
            tUser.setUserId(request.getUserId());
            i += 1;
        }
        if(request.getPassword()!=null) {
            tUser.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
            i += 1;
        }
        if(request.getUserName()!=null) {
            tUser.setUserName(request.getUserName());
            i += 1;
        }
        if(request.getTelephone()!=null) {
            tUser.setTelephone(request.getTelephone());
            i += 1;
        }
        if(request.getTelephone()!=null) {
            tUser.setSex(request.getTelephone());
            i += 1;
        }
        tUser.setCredit("100");
        if(request.getEmail()!=null) {
            tUser.setEmail(request.getEmail());
            i += 1;
        }
        if(request.getAge()!=null) {
            tUser.setAge(request.getAge());
            i += 1;
        }
        if(i!=7){
            result.put("state","some parameter null");
            return result;
        }
        else{
            try{
                user.add(tUser);
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
