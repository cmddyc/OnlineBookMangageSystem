package book_sys.dao.lmpl;

import book_sys.entity.TManagement;
import book_sys.dao.interf.t_management;

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
public class t_management_impl implements t_management{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private String sql_create(String type, TManagement management){
        String sql="where ";
        String select=type;//"select ""delete from table""update table set "
        if(!management.getAdminId().contains("<none>")){
            if(management.getAdminId().contains("<value>")) {
                sql += "admin_id=" + management.getAdminId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& management.getAdminId().contains("<set>")){
                select +=  "admin_id=" + management.getAdminId().substring(5)+",";
            }
            if(select.contains("select")&& management.getAdminId().contains("<query>")){
                select+="admin_id,";
            }
        }
        if(!management.getBookId().contains("<none>")){
            if(management.getBookId().contains("<value>")) {
                sql += "book_id=" + management.getBookId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& management.getBookId().contains("<set>")){
                select +=  "book_id=" + management.getBookId().substring(5)+",";
            }
            if(select.contains("select")&& management.getBookId().contains("<query>")){
                select += "book_id,";
            }
        }
        if(!management.getManageType().contains("<none>")){
            if(management.getManageType().contains("<value>")) {
                sql += "manage_type=" + management.getManageType().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& management.getManageType().contains("<set>")){
                select +=  "manage_type=" + management.getManageType().substring(5)+",";
            }
            if(select.contains("select")&& management.getManageType().contains("<query>")){
                select+="manage_type,";
            }
        }
        if(!management.getManageTime().contains("<none>")){
            if(management.getManageTime().contains("<value>")){
                sql += "manage_time=" + management.getManageTime().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& management.getManageTime().contains("<set>")){
                select +=  "manage_time=" + management.getManageTime().substring(5)+",";
            }
            if(select.contains("select")&& management.getManageTime().contains("<query>")){
                select+="manage_time,";
            }
        }
        if(select.equals("select ")){
            select+="* from t_management ";
        }
        else if(select.contains("select")) {
            select=select.substring(0,select.length()-1)+" from t_management ";
        }
        if(select.equals("update t_management set ")){
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
    public List<TManagement> search(int start, int length, TManagement management){
        List<TManagement> result=new ArrayList<>();
        String sql=sql_create("select ", management);

        if(length!=-1) {
            sql=sql.substring(0, sql.length()-1);
            sql += " limit " + String.valueOf(start) + "," + String.valueOf(length) + ";";
        }
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return result;
        }
        for (Map<String, Object> stringObjectMap : list) {
            TManagement each_row = new TManagement();
            if(stringObjectMap.get("admin_id")!=null) {
                each_row.setAdminId(stringObjectMap.get("admin_id").toString());
            }
            if(stringObjectMap.get("book_id")!=null) {
                each_row.setBookId(stringObjectMap.get("book_id").toString());
            }
            if(stringObjectMap.get("manage_type")!=null) {
                each_row.setManageType(stringObjectMap.get("manage_type").toString());
            }
            if(stringObjectMap.get("manage_time")!=null) {
                each_row.setManageTime(stringObjectMap.get("manage_time").toString());
            }
            result.add(each_row);
        }
        return result;
    }
    public int add(TManagement management){


        String sql="insert into t_management values(?,?,?,?)";

        if(management.getBookId().contains("<none>")&& management.getAdminId().contains("<none>")
                && management.getManageType().contains("<none>")&& management.getManageTime().equals("<none>")){
            return 0;
        }

        Object[] args={management.getAdminId(), management.getBookId(), management.getManageTime(), management.getManageType()};

        return jdbcTemplate.update(sql,args);
    }
    public int delete(TManagement management){

        String sql=sql_create("delete from t_management ",management);

        return jdbcTemplate.update(sql);
    }
    public int change(TManagement management){

        String sql=sql_create("update t_management set ",management);

        if(sql.equals("false")){
            return -1;
        }
        return jdbcTemplate.update(sql);
    }

}
