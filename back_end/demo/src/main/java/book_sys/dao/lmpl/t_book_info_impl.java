package book_sys.dao.lmpl;

import book_sys.entity.TBookInfo;
import book_sys.dao.interf.t_book_info;

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
public class t_book_info_impl implements t_book_info{




    @Autowired
    private JdbcTemplate jdbcTemplate;



    private String sql_create(String type, String search_type, TBookInfo book_info){
        String sql="where ";
        String select=type;//"select ""delete from table""update table set "
        if(!book_info.getIsbn().contains("<none>")){
            if(book_info.getIsbn().contains("<value>")) {
                sql += "isbn"+ search_type + book_info.getIsbn().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book_info.getIsbn().contains("<set>")){
                select +=  "isbn=" + book_info.getIsbn().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&book_info.getIsbn().contains("<query>")){
                select+="isbn,";
            }
        }
        if(!book_info.getBookName().contains("<none>")){
            if(book_info.getBookName().contains("<value>")) {
                sql += "book_name"+search_type + book_info.getBookName().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book_info.getBookName().contains("<set>")){
                select +=  "book_name=" + book_info.getBookName().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&book_info.getBookName().contains("<query>")){
                select += "book_name,";
            }
        }
        if(!book_info.getBookAuthor().contains("<none>")){
            if(book_info.getBookAuthor().contains("<value>")) {
                sql += "book_author"+search_type + book_info.getBookAuthor().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book_info.getBookAuthor().contains("<set>")){
                select +=  "book_author=" + book_info.getBookAuthor().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&book_info.getBookAuthor().contains("<query>")){
                select+="book_author,";
            }
        }
        if(!book_info.getBookIntro().contains("<none>")){
            if(book_info.getBookIntro().contains("<value>")) {
                sql += "book_intro"+search_type + book_info.getBookIntro().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book_info.getBookIntro().contains("<set>")){
                select +=  "book_intro=" + book_info.getBookIntro().substring(5).split("<value>")[0]+",";
            }
            else if(select.contains("select")&&book_info.getBookIntro().contains("<query>")){
                select+="book_intro,";
            }
        }
        if(!book_info.getEBook().contains("<none>")){
            if(book_info.getEBook().contains("<value>")) {
                sql += "e_book"+search_type + book_info.getEBook().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book_info.getEBook().contains("<set>")){
                select +=  "e_book=" + book_info.getEBook().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&book_info.getEBook().contains("<query>")){
                select+="e_book,";
            }
        }
        if(!book_info.getBookTypes().contains("<none>")){
            if(book_info.getBookTypes().contains("<value>")) {
                sql += "book_types"+search_type + book_info.getBookTypes().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book_info.getBookTypes().contains("<set>")){
                select +=  "book_types=" + book_info.getBookTypes().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&book_info.getBookTypes().contains("<query>")){
                select+="book_types,";
            }
        }
        if(!book_info.getBookImg().contains("<none>")){
            if(book_info.getBookImg().contains("<value>")) {
                sql += "book_img"+search_type + book_info.getBookImg().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book_info.getBookImg().contains("<set>")){
                select +=  "book_img=" + book_info.getBookImg().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&book_info.getBookImg().contains("<query>")){
                select += "book_img,";
            }
        }
        if(!book_info.getBookPrice().contains("<none>")){
            if(book_info.getBookPrice().contains("<value>")) {
                sql += "book_price=" + book_info.getBookPrice().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book_info.getBookPrice().contains("<set>")){
                select +=  "book_price=" + book_info.getBookPrice().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&book_info.getBookPrice().contains("<query>")){
                select+="book_price,";
            }
        }
        if(select.equals("select ")){
            select+="* from t_book_info ";
        }
        else if(select.contains("select")) {
            select=select.substring(0,select.length()-1)+" from t_book_info ";
        }
        if(select.equals("update t_book_info set ")){
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

    public List<TBookInfo> search(int start, int length, int type, TBookInfo bookInfo){
        List<TBookInfo> result=new ArrayList<>();
        String search_type="=";
        if(type==1){
            search_type=" like ";
        }
        String sql=sql_create("select ", search_type, bookInfo);
        if(search_type.equals(" like ")) {
            if (sql.contains("where")) {
                sql = "select * from t_book_info where " + sql.split("where")[1];
            } else {
                sql = "select * from t_book_info;";
            }
        }
        if(length!=-1) {
            sql=sql.substring(0, sql.length()-1);
            sql += " limit " + String.valueOf(start) + "," + String.valueOf(length) + ";";
        }
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return result;
        }
        for (Map<String, Object> stringObjectMap : list) {
            TBookInfo each_row = new TBookInfo();

            if(stringObjectMap.get("isbn")!=null) {
                each_row.setIsbn(stringObjectMap.get("isbn").toString());
            }
            if(stringObjectMap.get("book_name")!=null) {
                each_row.setBookName(stringObjectMap.get("book_name").toString());
            }
            if(stringObjectMap.get("book_author")!=null) {
                each_row.setBookAuthor(stringObjectMap.get("book_author").toString());
            }
            if(stringObjectMap.get("book_intro")!=null) {
                each_row.setBookIntro(stringObjectMap.get("book_intro").toString());
            }
            if(stringObjectMap.get("e_book")!=null) {
                each_row.setEBook(stringObjectMap.get("e_book").toString());
            }
            if(stringObjectMap.get("book_types")!=null) {
                each_row.setBookTypes(stringObjectMap.get("book_types").toString());
            }
            if(stringObjectMap.get("book_img")!=null) {
                each_row.setBookImg(stringObjectMap.get("book_img").toString());
            }
            if(stringObjectMap.get("book_price")!=null) {
                each_row.setBookPrice(stringObjectMap.get("book_price").toString());
            }
            result.add(each_row);
        }
        return result;
    }
    public int add(TBookInfo book_info){


        String sql="insert into t_book_info values(?,?,?,?,?,?,?,?)";

        if(book_info.getIsbn().equals("<none>")&&book_info.getBookName().equals("<none>")
                &&book_info.getBookAuthor().equals("<none>")&&book_info.getBookIntro().equals("<none>")
                    &&book_info.getEBook().equals("<none>")&&book_info.getBookTypes().equals("<none>")
                        &&book_info.getBookImg().equals("<none>")&&book_info.getBookPrice().equals("<none>")){
            return 0;
        }

        Object[] args={book_info.getIsbn(),book_info.getBookName(),book_info.getBookAuthor(),book_info.getBookIntro()
        ,book_info.getEBook(),book_info.getBookTypes(),book_info.getBookImg(),book_info.getBookPrice()};

        return jdbcTemplate.update(sql,args);
    }
    public int delete(TBookInfo bookInfo){

        String sql=sql_create("delete from t_book_info ","=", bookInfo);

        return jdbcTemplate.update(sql);
    }
    public int change(TBookInfo bookInfo){

        String sql=sql_create("update t_book_info set ","=",bookInfo);

        if(sql.equals("false")){
            return -1;
        }
        return jdbcTemplate.update(sql);
    }

}

