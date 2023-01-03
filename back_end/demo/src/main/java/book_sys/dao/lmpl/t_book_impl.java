package book_sys.dao.lmpl;

import book_sys.entity.TBook;
import book_sys.dao.interf.t_book;

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
public class t_book_impl implements t_book{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String sql_create(String type, TBook book){
        String sql="where ";
        String select=type;//"select ""delete from table""update table set "
        if(!book.getIsbn().contains("<none>")){
            if(book.getIsbn().contains("<value>")) {
                sql += "isbn=" + book.getIsbn().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book.getIsbn().contains("<set>")){
                select +=  "isbn=" + book.getIsbn().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&book.getIsbn().contains("<query>")){
                select+="isbn,";
            }
        }
        if(!book.getBookId().contains("<none>")){
            if(book.getBookId().contains("<value>")) {
                sql += "book_id=" + book.getBookId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book.getBookId().contains("<set>")){
                select +=  "book_id=" + book.getBookId().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&book.getBookId().contains("<query>")){
                select += "book_id,";
            }
        }
        if(!book.getBorrowType().contains("<none>")){
            if(book.getBorrowType().contains("<value>")) {
                sql += "borrow_type=" + book.getBorrowType().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book.getBorrowType().contains("<set>")){
                select +=  "borrow_type=" + book.getBorrowType().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&book.getBorrowType().contains("<query>")){
                select+="borrow_type,";
            }
        }
        if(!book.getBrokenType().contains("<none>")){
            if(book.getBrokenType().contains("<value>")) {
                sql += "broken_type=" + book.getBrokenType().split("value>")[1]+" and ";
            }
            if(select.contains("update")&&book.getBrokenType().contains("<set>")){
                select +=  "broken_type=" + book.getBrokenType().substring(5).split("<value>")[0]+",";
            }
            if(select.contains("select")&&book.getBrokenType().contains("<query>")){
                select+="broken_type,";
            }
        }
        if(select.equals("select ")){
            select+="* from t_book ";
        }
        else if(select.contains("select")) {
            select=select.substring(0,select.length()-1)+" from t_book ";
        }
        if(select.equals("update t_book set ")){
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
    public List<TBook> search(int start, int length, TBook book){
        List<TBook> result=new ArrayList<>();
        String sql=sql_create("select ", book);

        if(length!=-1) {
            sql=sql.substring(0, sql.length()-1);
            sql += " limit " + String.valueOf(start) + "," + String.valueOf(length) + ";";
        }
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return result;
        }
        for (Map<String, Object> stringObjectMap : list) {
            TBook each_row = new TBook();
            if(stringObjectMap.get("isbn")!=null) {
                each_row.setIsbn(stringObjectMap.get("isbn").toString());
            }
            if(stringObjectMap.get("book_id")!=null) {
                each_row.setBookId(stringObjectMap.get("book_id").toString());
            }
            if(stringObjectMap.get("borrow_type")!=null) {
                each_row.setBorrowType(stringObjectMap.get("borrow_type").toString());
            }
            if(stringObjectMap.get("broken_type")!=null) {
                each_row.setBrokenType(stringObjectMap.get("broken_type").toString());
            }
            result.add(each_row);
        }
        return result;
    }
    public int add(TBook book){


        String sql="insert into t_book values(?,?,?,?)";

        if(book.getBookId().contains("<none>")&&book.getIsbn().contains("<none>")
                &&book.getBorrowType().contains("<none>")&&book.getBrokenType().contains("<none>")){
            return 0;
        }

        Object[] args={book.getIsbn(),book.getBookId(),book.getBorrowType(),book.getBrokenType()};

        return jdbcTemplate.update(sql,args);
    }
    public int delete(TBook book){

        String sql=sql_create("delete from t_book ", book);

        return jdbcTemplate.update(sql);
    }
    public int change(TBook book){

        String sql=sql_create("update t_book set ", book);

        if(sql.equals("false")){
            return -1;
        }
        return jdbcTemplate.update(sql);
    }

}
