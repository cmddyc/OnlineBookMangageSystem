package book_sys.dao.lmpl;

import book_sys.entity.TReturn;
import book_sys.dao.interf.t_return;

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
public class t_return_impl implements t_return{


    @Autowired
    private JdbcTemplate jdbcTemplate;



    private String sql_create(String type, TReturn tReturn){
        String sql="where ";
        String select=type;//"select ""delete from table""update table set "
        if(!tReturn.getBookId().contains("<none>")){
            if(tReturn.getBookId().contains("<value>")) {
                sql += "book_id=" + tReturn.getBookId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& tReturn.getBookId().contains("<set>")){
                select +=  "book_id=" + tReturn.getBookId().substring(5)+",";
            }
            if(select.contains("select")&& tReturn.getBookId().contains("<query>")){
                select+="book_id,";
            }
        }
        if(!tReturn.getUserId().contains("<none>")){
            if(tReturn.getUserId().contains("<value>")) {
                sql += "user_id=" + tReturn.getUserId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& tReturn.getUserId().contains("<set>")){
                select +=  "user_id=" + tReturn.getUserId().substring(5)+",";
            }
            if(select.contains("select")&& tReturn.getUserId().contains("<query>")){
                select += "user_id,";
            }
        }
        if(!tReturn.getReturnTime().contains("<none>")){
            if(tReturn.getReturnTime().contains("<value>")) {
                sql += "return_time=" + tReturn.getReturnTime().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& tReturn.getReturnTime().contains("<set>")){
                select +=  "return_time=" + tReturn.getReturnTime().substring(5)+",";
            }
            if(select.contains("select")&& tReturn.getReturnTime().contains("<query>")){
                select+="return_time,";
            }
        }
        if(!tReturn.getReturnType().contains("<none>")){
            if(tReturn.getReturnType().contains("<value>")) {
                sql += "return_type=" + tReturn.getReturnType().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& tReturn.getReturnType().contains("<set>")){
                select +=  "return_type=" + tReturn.getReturnType().substring(5)+",";
            }
            if(select.contains("select")&& tReturn.getReturnType().contains("<query>")){
                select+="return_type,";
            }
        }
        if(!tReturn.getNote().contains("<none>")){
            if(tReturn.getNote().contains("<value>")) {
                sql += "note=" + tReturn.getNote().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& tReturn.getNote().contains("<set>")){
                select +=  "note=" + tReturn.getNote().substring(5)+",";
            }
            if(select.contains("select")&& tReturn.getNote().contains("<query>")){
                select+="note,";
            }
        }
        if(select.equals("select ")){
            select+="* from t_return ";
        }
        else if(select.contains("select")) {
            select=select.substring(0,select.length()-1)+" from t_return ";
        }
        if(select.equals("update t_return set ")){
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
        String order="";
        if(select.contains("select")){
            order=" order by return_time";
        }
        return select+sql+order+";";
    }
    public List<TReturn> search(int start, int length, TReturn tReturn){
        List<TReturn> result=new ArrayList<>();
        String sql=sql_create("select ", tReturn);

        if(length!=-1) {
            sql=sql.substring(0, sql.length()-1);
            sql += " limit " + String.valueOf(start) + "," + String.valueOf(length) + ";";
        }
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return result;
        }
        for (Map<String, Object> stringObjectMap : list) {
            TReturn each_row = new TReturn();
            if (stringObjectMap.get("book_id") != null) {
                each_row.setBookId(stringObjectMap.get("book_id").toString());
            }
            if (stringObjectMap.get("user_id") != null) {
                each_row.setUserId(stringObjectMap.get("user_id").toString());
            }
            if (stringObjectMap.get("return_time") != null) {
                each_row.setReturnTime(stringObjectMap.get("return_time").toString());
            }
            if (stringObjectMap.get("return_type") != null) {
                each_row.setReturnType(stringObjectMap.get("return_type").toString());
            }
            if (stringObjectMap.get("note") != null) {
                each_row.setNote(stringObjectMap.get("note").toString());
            }
            result.add(each_row);
        }
        return result;
    }
    public int add(TReturn tReturn){


        String sql="insert into t_return values(?,?,?,?,?)";

        if(tReturn.getBookId().equals("<none>")&& tReturn.getUserId().equals("<none>")
                && tReturn.getReturnTime().equals("<none>")&& tReturn.getReturnType().equals("<none>")){
            return 0;
        }

        Object[] args={tReturn.getBookId(), tReturn.getUserId(), tReturn.getReturnTime(), tReturn.getReturnType(), tReturn.getNote()};

        return jdbcTemplate.update(sql,args);
    }
    public int delete(TReturn tReturn){

        String sql=sql_create("delete from t_return ", tReturn);

        return jdbcTemplate.update(sql);
    }
    public int change(TReturn tReturn){

        String sql=sql_create("update t_return set ",tReturn);

        if(sql.equals("false")){
            return -1;
        }
        return jdbcTemplate.update(sql);
    }

}
