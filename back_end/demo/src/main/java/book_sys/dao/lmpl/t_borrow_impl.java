package book_sys.dao.lmpl;

import book_sys.entity.TBorrow;
import book_sys.dao.interf.t_borrow;

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
public class t_borrow_impl implements t_borrow{


    @Autowired
    private JdbcTemplate jdbcTemplate;



    private String sql_create(String type, TBorrow borrow){
        String sql="where ";
        String select=type;//"select ""delete from table""update table set "
        if(!borrow.getBookId().contains("<none>")){
            if(borrow.getBookId().contains("<value>")) {
                sql += "book_id=" + borrow.getBookId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& borrow.getBookId().contains("<set>")){
                select +=  "book_id=" + borrow.getBookId().substring(5)+",";
            }
            if(select.contains("select")&& borrow.getBookId().contains("<query>")){
                select+="book_id,";
            }
        }
        if(!borrow.getUserId().contains("<none>")){
            if(borrow.getUserId().contains("<value>")) {
                sql += "user_id=" + borrow.getUserId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& borrow.getUserId().contains("<set>")){
                select +=  "user_id=" + borrow.getUserId().substring(5)+",";
            }
            if(select.contains("select")&& borrow.getUserId().contains("<query>")){
                select += "user_id,";
            }
        }
        if(!borrow.getBorrowStartTime().contains("<none>")){
            if(borrow.getBorrowStartTime().contains("<value>")) {
                sql += "borrow_start_time=" + borrow.getBorrowStartTime().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& borrow.getBorrowStartTime().contains("<set>")){
                select +=  "borrow_start_time=" + borrow.getBorrowStartTime().substring(5)+",";
            }
            if(select.contains("select")&& borrow.getBorrowStartTime().contains("<query>")){
                select+="borrow_start_time,";
            }
        }
        if(!borrow.getBorrowEndTime().contains("<none>")){
            if(borrow.getBorrowEndTime().contains("<value>")) {
                sql += "borrow_end_time=" + borrow.getBorrowEndTime().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& borrow.getBorrowEndTime().contains("<set>")){
                select +=  "borrow_end_time=" + borrow.getBorrowEndTime().substring(5)+",";
            }
            if(select.contains("select")&& borrow.getBorrowEndTime().contains("<query>")){
                select+="borrow_end_time,";
            }
        }
        if(!borrow.getNote().contains("<none>")){
            if(borrow.getNote().contains("<value>")) {
                sql += "note=" + borrow.getNote().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& borrow.getNote().contains("<set>")){
                select +=  "note=" + borrow.getNote().substring(5)+",";
            }
            if(select.contains("select")&& borrow.getNote().contains("<query>")){
                select+="note,";
            }
        }

        if(select.equals("select ")){
            select+="* from t_borrow ";
        }
        else if(select.contains("select")) {
            select=select.substring(0,select.length()-1)+" from t_borrow ";
        }
        if(select.equals("update t_borrow set ")){
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
            order=" order by borrow_start_time";
        }
        return select+sql+order+";";
    }
    public List<TBorrow> search(int start, int length, TBorrow borrow){
        List<TBorrow> result=new ArrayList<>();
        String sql=sql_create("select ", borrow);

        if(length!=-1) {
            sql=sql.substring(0, sql.length()-1);
            sql += " limit " + String.valueOf(start) + "," + String.valueOf(length) + ";";
        }
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return result;
        }
        for (Map<String, Object> stringObjectMap : list) {
            TBorrow each_row = new TBorrow();

            if(stringObjectMap.get("book_id")!=null) {
                each_row.setBookId(stringObjectMap.get("book_id").toString());
            }
            if(stringObjectMap.get("user_id")!=null) {
                each_row.setUserId(stringObjectMap.get("user_id").toString());
            }
            if(stringObjectMap.get("borrow_start_time")!=null) {
                each_row.setBorrowStartTime(stringObjectMap.get("borrow_start_time").toString());
            }
            if(stringObjectMap.get("borrow_end_time")!=null) {
                each_row.setBorrowEndTime(stringObjectMap.get("borrow_end_time").toString());
            }
            if(stringObjectMap.get("note")!=null) {
                each_row.setNote(stringObjectMap.get("note").toString());
            }

            result.add(each_row);
        }
        return result;
    }
    public int add(TBorrow borrow){


        String sql="insert into t_borrow values(?,?,?,?,?)";

        if(borrow.getBookId().equals("<none>")&& borrow.getUserId().equals("<none>")
                && borrow.getBorrowStartTime().equals("<none>")&& borrow.getBorrowEndTime().equals("<none>")
                && borrow.getNote().equals("<none>")){
            return 0;
        }

        Object[] args={borrow.getBookId(), borrow.getUserId(), borrow.getBorrowStartTime(), borrow.getBorrowEndTime()
                , borrow.getNote()};

        return jdbcTemplate.update(sql,args);
    }
    public int delete(TBorrow borrow){

        String sql=sql_create("delete from t_borrow ", borrow);

        return jdbcTemplate.update(sql);
    }
    public int change(TBorrow borrow){

        String sql=sql_create("update t_borrow set ", borrow);

        if(sql.equals("false")){
            return -1;
        }
        return jdbcTemplate.update(sql);
    }

}


