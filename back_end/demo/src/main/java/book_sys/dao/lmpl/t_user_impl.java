package book_sys.dao.lmpl;

import book_sys.entity.TUser;
import book_sys.dao.interf.t_user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class t_user_impl implements t_user{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String sql_create(String type, TUser user){
        String sql="where ";
        String select=type;//"select ""delete from table""update table set "
        if(!user.getUserId().contains("<none>")){
            if(user.getUserId().contains("<value>")) {
                sql += "user_id=" + user.getUserId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& user.getUserId().contains("<set>")){
                select +=  "user_id=" + user.getUserId().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&& user.getUserId().contains("<query>")){
                select+="user_id,";
            }
        }
        if(!user.getUserName().contains("<none>")){
            if(user.getUserName().contains("<value>")) {
                sql += "user_name=" + user.getUserName().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& user.getUserName().contains("<set>")){
                select +=  "user_name=" + user.getUserName().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&& user.getUserName().contains("<query>")){
                select += "user_name,";
            }
        }
        if(!user.getPassword().contains("<none>")){
            if(user.getPassword().contains("<value>")) {
                sql += "password=" + user.getPassword().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& user.getPassword().contains("<set>")){
                select +=  "password=" + user.getPassword().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&& user.getPassword().contains("<query>")){
                select+="password,";
            }
        }
        if(!user.getAge().contains("<none>")){
            if(user.getAge().contains("<value>")) {
                sql += "age=" + user.getAge().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& user.getAge().contains("<set>")){
                select +=  "age=" + user.getAge().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&& user.getAge().contains("<query>")){
                select+="age,";
            }
        }
        if(!user.getSex().contains("<none>")){
            if(user.getSex().contains("<value>")) {
                sql += "sex=" + user.getSex().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& user.getSex().contains("<set>")){
                select +=  "sex=" + user.getSex().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&& user.getSex().contains("<query>")){
                select+="sex,";
            }
        }
        if(!user.getTelephone().contains("<none>")){
            if(user.getTelephone().contains("<value>")) {
                sql += "telephone=" + user.getTelephone().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& user.getTelephone().contains("<set>")){
                select +=  "telephone=" + user.getTelephone().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&& user.getTelephone().contains("<query>")){
                select+="telephone,";
            }
        }
        if(!user.getEmail().contains("<none>")){
            if(user.getEmail().contains("<value>")) {
                sql += "Email=" + user.getEmail().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& user.getEmail().contains("<set>")){
                select +=  "Email=" + user.getEmail().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&& user.getEmail().contains("<query>")){
                select += "Email,";
            }
        }
        if(!user.getCredit().contains("<none>")){
            if(user.getCredit().contains("<value>")) {
                sql += "credit=" + user.getCredit().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& user.getCredit().contains("<set>")){
                select +=  "credit=" + user.getCredit().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&& user.getCredit().contains("<query>")){
                select+="credit,";
            }
        }
        if(select.equals("select ")){
            select+="* from t_user ";
        }
        else if(select.contains("select")) {
            select=select.substring(0,select.length()-1)+" from t_user ";
        }
        if(select.equals("update t_user set ")){
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
    @Scheduled(cron="1 1 1 1 1/3 *")
    public void creditUpdate(){
        String sql="update t_user set credit=100 where credit>20";
        jdbcTemplate.update(sql);
    }
    public List<TUser> search(int start, int length, TUser user){
        List<TUser> result=new ArrayList<>();
        String sql=sql_create("select ", user);

        if(length!=-1) {
            sql=sql.substring(0, sql.length()-1);
            sql += " limit " + String.valueOf(start) + "," + String.valueOf(length) + ";";
        }
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return result;
        }
        for (Map<String, Object> stringObjectMap : list) {
            TUser each_row = new TUser();

            if(stringObjectMap.get("user_id")!=null) {
                each_row.setUserId(stringObjectMap.get("user_id").toString());
            }
            if(stringObjectMap.get("user_name")!=null) {
                each_row.setUserName(stringObjectMap.get("user_name").toString());
            }
            if(stringObjectMap.get("password")!=null) {
                each_row.setPassword(stringObjectMap.get("password").toString());
            }
            if(stringObjectMap.get("age")!=null) {
                each_row.setAge(stringObjectMap.get("age").toString());
            }
            if(stringObjectMap.get("sex")!=null) {
                each_row.setSex(stringObjectMap.get("sex").toString());
            }
            if(stringObjectMap.get("telephone")!=null) {
                each_row.setTelephone(stringObjectMap.get("telephone").toString());
            }
            if(stringObjectMap.get("Email")!=null) {
                each_row.setEmail(stringObjectMap.get("Email").toString());
            }
            if(stringObjectMap.get("credit")!=null) {
                each_row.setCredit(stringObjectMap.get("credit").toString());
            }
            result.add(each_row);
        }
        return result;
    }
    public int add(TUser user){


        String sql="insert into t_user values(?,?,?,?,?,?,?,?)";

        if(user.getUserId().equals("<none>")&& user.getUserName().equals("<none>")
                && user.getPassword().equals("<none>")&& user.getAge().equals("<none>")
                && user.getSex().equals("<none>")&& user.getTelephone().equals("<none>")
                && user.getEmail().equals("<none>")&& user.getCredit().equals("<none>")){
            return 0;
        }

        Object[] args={user.getUserId(), user.getUserName(), user.getPassword(), user.getAge()
                , user.getSex(), user.getTelephone(), user.getEmail(), user.getCredit()};


        return jdbcTemplate.update(sql,args);
    }
    public int delete(TUser user){

        String sql=sql_create("delete from t_user ", user);

        return jdbcTemplate.update(sql);
    }
    public int change(TUser user){

        String sql=sql_create("update t_user set ", user);

        if(sql.equals("false")){
            return -1;
        }
        return jdbcTemplate.update(sql);
    }

}


