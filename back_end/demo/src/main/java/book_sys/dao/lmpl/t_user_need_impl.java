package book_sys.dao.lmpl;

import book_sys.entity.TUserNeed;
import book_sys.dao.interf.t_user_need;

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
public class t_user_need_impl implements t_user_need{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private String sql_create(String type, TUserNeed userNeed){
        String sql="where ";
        String select=type;//"select ""delete from table""update table set "
        if(!userNeed.getUserId().contains("<none>")){
            if(userNeed.getUserId().contains("<value>")) {
                sql += "user_id=" + userNeed.getUserId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& userNeed.getUserId().contains("<set>")){
                select +=  "user_id=" + userNeed.getUserId().substring(5)+",";
            }
            if(select.contains("select")&& userNeed.getUserId().contains("<query>")){
                select+="user_id,";
            }
        }
        if(!userNeed.getIsbn().contains("<none>")){
            if(userNeed.getIsbn().contains("<value>")) {
                sql += "isbn=" + userNeed.getIsbn().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& userNeed.getIsbn().contains("<set>")){
                select +=  "isbn=" + userNeed.getIsbn().substring(5)+",";
            }
            if(select.contains("select")&& userNeed.getIsbn().contains("<query>")){
                select += "isbn,";
            }
        }
        if(!userNeed.getPreBorrowStartTime().contains("<none>")){
            if(userNeed.getPreBorrowStartTime().contains("<value>")) {
                sql += "pre_borrow_start_time=" + userNeed.getPreBorrowStartTime().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& userNeed.getPreBorrowStartTime().contains("<set>")){
                select +=  "pre_borrow_start_time=" + userNeed.getPreBorrowStartTime().substring(5)+",";
            }
            if(select.contains("select")&& userNeed.getPreBorrowStartTime().contains("<query>")){
                select+="pre_borrow_start_time,";
            }
        }
        if(!userNeed.getPreBorrowEndTime().contains("<none>")){
            if(userNeed.getPreBorrowEndTime().contains("<value>")) {
                sql += "pre_borrow_end_time=" + userNeed.getPreBorrowEndTime().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& userNeed.getPreBorrowEndTime().contains("<set>")){
                select +=  "pre_borrow_end_time=" + userNeed.getPreBorrowEndTime().substring(5)+",";
            }
            if(select.contains("select")&& userNeed.getPreBorrowEndTime().contains("<query>")){
                select+="pre_borrow_end_time,";
            }
        }
        if(!userNeed.getNote().contains("<none>")){
            if(userNeed.getNote().contains("<value>")) {
                sql += "note=" + userNeed.getNote().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& userNeed.getNote().contains("<set>")){
                select +=  "note=" + userNeed.getNote().substring(5)+",";
            }
            if(select.contains("select")&& userNeed.getNote().contains("<query>")){
                select+="note,";
            }
        }
        if(select.equals("select ")){
            select+="* from t_user_need ";
        }
        else if(select.contains("select")) {
            select=select.substring(0,select.length()-1)+" from t_user_need ";
        }
        if(select.equals("update t_user_need set ")){
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
    public List<TUserNeed> search(int start, int length, TUserNeed userNeed){
        List<TUserNeed> result=new ArrayList<>();
        String sql=sql_create("select ", userNeed);

        if(length!=-1) {
            sql=sql.substring(0, sql.length()-1);
            sql += " limit " + String.valueOf(start) + "," + String.valueOf(length) + ";";
        }
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return result;
        }
        for (Map<String, Object> stringObjectMap : list) {
            TUserNeed each_row = new TUserNeed();
            if (stringObjectMap.get("user_id") != null) {
                each_row.setUserId(stringObjectMap.get("user_id").toString());
            }
            if (stringObjectMap.get("isbn") != null) {
                each_row.setIsbn(stringObjectMap.get("isbn").toString());
            }
            if (stringObjectMap.get("pre_borrow_start_time") != null) {
                each_row.setPreBorrowStartTime(stringObjectMap.get("pre_borrow_start_time").toString());
            }
            if (stringObjectMap.get("pre_borrow_end_time") != null) {
                each_row.setPreBorrowStartTime(stringObjectMap.get("pre_borrow_end_time").toString());
            }
            if (stringObjectMap.get("note") != null) {
                each_row.setNote(stringObjectMap.get("note").toString());
            }
            result.add(each_row);
        }
        return result;
    }
    public int add(TUserNeed userNeed){


        String sql="insert into t_user_need values(?,?,?,?,?)";

        if(userNeed.getUserId().equals("<none>")&& userNeed.getIsbn().equals("<none>")
                && userNeed.getPreBorrowStartTime().equals("<none>")&& userNeed.getPreBorrowEndTime().equals("<none>")){
            return 0;
        }

        Object[] args={userNeed.getUserId(), userNeed.getIsbn(), userNeed.getPreBorrowStartTime(), userNeed.getPreBorrowEndTime(), userNeed.getNote()};

        return jdbcTemplate.update(sql,args);
    }
    public int delete(TUserNeed userNeed){

        String sql=sql_create("delete from t_user_need ", userNeed);

        return jdbcTemplate.update(sql);
    }
    public int change(TUserNeed userNeed){

        String sql=sql_create("update t_user_need set ", userNeed);

        if(sql.equals("false")){
            return -1;
        }
        return jdbcTemplate.update(sql);
    }

}
