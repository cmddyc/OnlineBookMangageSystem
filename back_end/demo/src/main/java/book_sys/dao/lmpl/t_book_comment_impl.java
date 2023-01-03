package book_sys.dao.lmpl;

import book_sys.entity.TBookComment;
import book_sys.dao.interf.t_book_comment;

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
public class t_book_comment_impl implements t_book_comment {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String sql_create(String type, TBookComment bookComment){
        String sql="where ";
        String select=type;//"select ""delete from table""update table set "
        if(!bookComment.getIsbn().contains("<none>")){
            if(bookComment.getIsbn().contains("<value>")) {
                sql += "isbn=" + bookComment.getIsbn().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& bookComment.getIsbn().contains("<set>")){
                select +=  "isbn=" + bookComment.getIsbn().substring(5)+",";
            }
            if(select.contains("select")&& bookComment.getIsbn().contains("<query>")){
                select+="isbn,";
            }
        }
        if(!bookComment.getUserId().contains("<none>")){
            if(bookComment.getUserId().contains("<value>")) {
                sql += "user_id=" + bookComment.getUserId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& bookComment.getUserId().contains("<set>")){
                select +=  "user_id=" + bookComment.getUserId().substring(5)+",";
            }
            if(select.contains("select")&& bookComment.getUserId().contains("<query>")){
                select += "user_id,";
            }
        }
        if(!bookComment.getComment().contains("<none>")){
            if(bookComment.getComment().contains("<value>")) {
                sql += "comment=" + bookComment.getComment().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& bookComment.getComment().contains("<set>")){
                select +=  "comment=" + bookComment.getComment().substring(5)+",";
            }
            if(select.contains("select")&& bookComment.getComment().contains("<query>")){
                select+="comment,";
            }
        }
        if(!bookComment.getCommentTime().contains("<none>")){
            if(bookComment.getCommentTime().contains("<value>")) {
                sql += "comment_time=" + bookComment.getCommentTime().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& bookComment.getCommentTime().contains("<set>")){
                select +=  "comment_time=" + bookComment.getCommentTime().substring(5)+",";
            }
            if(select.contains("select")&& bookComment.getCommentTime().contains("<query>")){
                select+="comment_time,";
            }
        }
        if(select.equals("select ")){
            select+="* from t_book_comment ";
        }
        else if(select.contains("select")) {
            select=select.substring(0,select.length()-1)+" from t_book_comment ";
        }
        if(select.equals("update t_book_comment set ")){
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
    public List<TBookComment> search(int start, int length, TBookComment bookComment){
        List<TBookComment> result=new ArrayList<>();
        String sql=sql_create("select ", bookComment);

        if(length!=-1) {
            sql=sql.substring(0, sql.length()-1);
            sql += " limit " + String.valueOf(start) + "," + String.valueOf(length) + ";";
        }
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return result;
        }
        for (Map<String, Object> stringObjectMap : list) {
            TBookComment each_row = new TBookComment();
            if(stringObjectMap.get("isbn")!=null) {
                each_row.setIsbn(stringObjectMap.get("isbn").toString());
            }
            if(stringObjectMap.get("user_id")!=null) {
                each_row.setUserId(stringObjectMap.get("user_id").toString());
            }
            if(stringObjectMap.get("comment")!=null) {
                each_row.setComment(stringObjectMap.get("comment").toString());
            }
            if(stringObjectMap.get("comment_time")!=null) {
                each_row.setCommentTime(stringObjectMap.get("comment_time").toString());
            }
            result.add(each_row);
        }
        return result;
    }
    public int add(TBookComment bookComment){


        String sql="insert into t_book_comment values(?,?,?,?)";

        if(bookComment.getUserId().contains("<none>")&& bookComment.getIsbn().contains("<none>")
                && bookComment.getComment().contains("<none>")&& bookComment.getCommentTime().contains("<none>")){
            return 0;
        }

        Object[] args={bookComment.getUserId(), bookComment.getIsbn(), bookComment.getComment(), bookComment.getCommentTime()};

        return jdbcTemplate.update(sql,args);
    }
    public int delete(TBookComment bookComment){

        String sql=sql_create("delete from t_book_comment ", bookComment);

        return jdbcTemplate.update(sql);
    }
    public int change(TBookComment bookComment){

        String sql=sql_create("update t_book_comment set ",bookComment);

        if(sql.equals("false")){
            return -1;
        }
        return jdbcTemplate.update(sql);
    }

}

