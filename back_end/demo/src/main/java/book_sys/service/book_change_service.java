package book_sys.service;

import book_sys.dao.interf.t_book;
import book_sys.dao.interf.t_book_info;
import book_sys.dao.interf.t_management;
import book_sys.entity.TBook;
import book_sys.entity.TBookInfo;
import book_sys.entity.TManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class book_change_service {
    @Autowired
    t_management management;
    @Autowired
    t_book book;
    @Autowired
    t_book_info bookInfo;
    public Map<String, Object> addBook(TBook tBook, String id){
        Map<String, Object> result=new HashMap<>();
        try {
            TManagement tManagement = new TManagement();
            tManagement.setBookId(tBook.getBookId());
            tManagement.setManageTime(String.valueOf(System.currentTimeMillis()));
            tManagement.setManageType("add");
            tManagement.setAdminId(id);
            management.add(tManagement);
            book.add(tBook);
            result.put("state","success");
            return result;
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
    }
    public Map<String, Object> addBookInfo(TBookInfo tBookInfo, String id){
        Map<String, Object> result=new HashMap<>();
        try {
            TManagement tManagement = new TManagement();
            tManagement.setBookId(tBookInfo.getIsbn());
            tManagement.setManageTime(String.valueOf(System.currentTimeMillis()));
            tManagement.setManageType("addinfo");
            tManagement.setAdminId(id);
            management.add(tManagement);
            bookInfo.add(tBookInfo);
            result.put("state","success");
            return result;
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
    }
    public Map<String, Object> changeBook(TBook tBook, String id, String bookId){
        Map<String, Object> result=new HashMap<>();
        try {
            TManagement tManagement = new TManagement();
            tManagement.setBookId(bookId);
            tManagement.setManageTime(String.valueOf(System.currentTimeMillis()));
            tManagement.setManageType("change");
            tManagement.setAdminId(id);
            management.add(tManagement);
            book.change(tBook);
            result.put("state","success");
            return result;
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
    }
    public Map<String, Object> changeBookInfo(TBookInfo tBookInfo, String id, String isbn){
        Map<String, Object> result=new HashMap<>();
        try {
            TManagement tManagement = new TManagement();
            tManagement.setBookId(isbn);
            tManagement.setManageTime(String.valueOf(System.currentTimeMillis()));
            tManagement.setManageType("changeinfo");
            tManagement.setAdminId(id);
            management.add(tManagement);
            bookInfo.change(tBookInfo);
            result.put("state","success");
            return result;
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
    }
    public Map<String, Object> deleteBook(TBook tBook, String id, String bookId){
        Map<String, Object> result=new HashMap<>();
        try {
            TManagement tManagement = new TManagement();
            tManagement.setBookId(bookId);
            tManagement.setManageTime(String.valueOf(System.currentTimeMillis()));
            tManagement.setManageType("delete");
            tManagement.setAdminId(id);
            management.add(tManagement);
            book.delete(tBook);
            result.put("state","success");
            return result;
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
    }
    public Map<String, Object> deleteBookInfo(TBookInfo tBookInfo, String id, String isbn){
        Map<String, Object> result=new HashMap<>();
        try {
            TManagement tManagement = new TManagement();
            tManagement.setBookId(isbn);
            tManagement.setManageTime(String.valueOf(System.currentTimeMillis()));
            tManagement.setManageType("deleteinfo");
            tManagement.setAdminId(id);
            management.add(tManagement);
            bookInfo.delete(tBookInfo);
            result.put("state","success");
            return result;
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
    }
    public Map<String, Object> manageSearch(TManagement tManagement,String startTime, String endTime){
        Map<String, Object> result=new HashMap<>();
        try {
            List<TManagement> list =management.search(0,-1,tManagement);
            if(!(startTime.equals("no")&&endTime.equals("no"))){
                int i=0;
                for(i=0;i<list.size();i++){
                    if(Long.parseLong(list.get(i).getManageTime())<Long.parseLong(startTime)||Long.parseLong(list.get(i).getManageTime())>Long.parseLong(endTime)){
                        list.remove(i);
                    }
                }
            }
            result.put("state","success");
            result.put("result",list);
            return result;
        }
        catch(Exception e){

            result.put("state","fail");
            return result;
        }

    }
}
