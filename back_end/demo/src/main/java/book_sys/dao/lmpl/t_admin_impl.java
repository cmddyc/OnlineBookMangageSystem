package book_sys.dao.lmpl;

import book_sys.dao.interf.t_admin;
import book_sys.entity.TAdmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class t_admin_impl implements t_admin {


    @Autowired
    private JdbcTemplate jdbcTemplate;



    private String sql_create(String type, TAdmin admin){
        String sql="where ";
        String select=type;//"select ""delete from table""update table set "
        if(!admin.getAdminId().contains("<none>")){
            if(admin.getAdminId().contains("<value>")) {
                sql += "admin_id=" + admin.getAdminId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&admin.getAdminId().contains("<set>")){
                select +=  "admin_id=" + admin.getAdminId().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&admin.getAdminId().contains("<query>")){
                select+="admin_id,";
            }
        }
        if(!admin.getAdminName().contains("<none>")){
            if(admin.getAdminName().contains("<value>")) {
                sql += "admin_name=" + admin.getAdminName().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&admin.getAdminName().contains("<set>")){
                select +=  "admin_name=" + admin.getAdminName().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&admin.getAdminName().contains("<query>")){
                select += "admin_name,";
            }
        }
        if(!admin.getAdminPassword().contains("<none>")){
            if(admin.getAdminPassword().contains("<value>")) {
                sql += "admin_password=" + admin.getAdminPassword().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&admin.getAdminPassword().contains("<set>")){
                select +=  "admin_password=" + admin.getAdminPassword().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&admin.getAdminPassword().contains("<query>")){
                select+="admin_password,";
            }
        }
        if(!admin.getAdminPhone().contains("<none>")){
            if(admin.getAdminPhone().contains("<value>")) {
                sql += "admin_phone=" + admin.getAdminPhone().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&admin.getAdminPhone().contains("<set>")){
                select +=  "admin_phone=" + admin.getAdminPhone().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&admin.getAdminPhone().contains("<query>")){
                select+="admin_phone,";
            }
        }
        if(!admin.getAdminEmail().contains("<none>")){
            if(admin.getAdminEmail().contains("<value>")) {
                sql += "admin_email=" + admin.getAdminEmail().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&admin.getAdminEmail().contains("<set>")){
                select +=  "admin_email=" + admin.getAdminEmail().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&admin.getAdminEmail().contains("<query>")){
                select+="admin_email,";
            }
        }
        if(select.equals("select ")){
            select+="* from t_admin ";
        }
        else if(select.contains("select")) {
            select=select.substring(0,select.length()-1)+" from t_admin ";
        }
        if(select.equals("update t_admin set ")){
            return "false";
        }
        else if(select.contains("update")) {
            select=select.substring(0,select.length()-1)+" ";
        }

        if(sql.equals("where ")){
            sql = "";
        }
        else {
            sql=sql.substring(0,sql.length()-5);
        }
        return select+sql+";";
    }
    public List<TAdmin> search(TAdmin admin){
        List<TAdmin> result=new ArrayList<>();
        String sql=sql_create("select ", admin);
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return result;
        }
        for (Map<String, Object> stringObjectMap : list) {
            TAdmin each_row = new TAdmin();
            if (stringObjectMap.get("admin_id") != null) {
                each_row.setAdminId(stringObjectMap.get("admin_id").toString());
            }
            if (stringObjectMap.get("admin_name") != null) {
                each_row.setAdminName(stringObjectMap.get("admin_name").toString());
            }
            if (stringObjectMap.get("admin_password") != null) {
                each_row.setAdminPassword(stringObjectMap.get("admin_password").toString());
            }
            if (stringObjectMap.get("admin_phone") != null) {
                each_row.setAdminPhone(stringObjectMap.get("admin_phone").toString());
            }
            if (stringObjectMap.get("admin_email") != null) {
                each_row.setAdminEmail(stringObjectMap.get("admin_email").toString());
            }
            result.add(each_row);
        }
        return result;
    }
    public int add(TAdmin admin){


        String sql="insert into t_admin values(?,?,?,?,?)";

        if(admin.getAdminId().equals("<none>")&&admin.getAdminName().equals("<none>")
                &&admin.getAdminPassword().equals("<none>")&&admin.getAdminPhone().equals("<none>")){
            return 0;
        }

        Object[] args={admin.getAdminId(),admin.getAdminName(),admin.getAdminPassword(),admin.getAdminPhone(),admin.getAdminEmail()};

        return jdbcTemplate.update(sql,args);
    }
    public int delete(TAdmin admin){

        String sql=sql_create("delete from t_admin ",admin);

        return jdbcTemplate.update(sql);
    }
    public int change(TAdmin admin){

        String sql=sql_create("update t_admin set ", admin);

        if(sql.equals("false")){
            return -1;
        }
        return jdbcTemplate.update(sql);
    }

}