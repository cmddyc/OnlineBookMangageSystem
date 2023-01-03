package book_sys.service;


import book_sys.dao.interf.t_book;
import book_sys.dao.interf.t_book_info;
import book_sys.dao.interf.t_borrow;
import book_sys.dao.interf.t_user;
import book_sys.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;



@Service
public class book_borrow_service {

    @Autowired
    t_borrow borrow;
    @Autowired
    t_user user;
    @Autowired
    t_book book;
    @Autowired
    t_book_info bookInfo;


    public synchronized Map<String, Object> bookBorrow(TBook tBook, TUser tUser, TBorrow tBorrow, int type, String ebook){
        Map<String, Object> result=new HashMap<>();
        if(type==0){
            try{
                List<TUser> userResult=user.search(0,-1, tUser);
                if(Integer.parseInt(userResult.get(0).getCredit())<60){
                    result.put("state","fail");
                    return result;
                }

                if(ebook.equals("normal")) {
                    tBook.setIsbn("<value>'" + tBorrow.getBookId() + "'");
                    tBook.setBorrowType("<value>'finish'");
                    tBook.setBrokenType("<value>'normal'");
                    tBook.setBookId("<query>");

                    List<TBook> bookResult = book.search(0, -1, tBook);

                    tBook.setIsbn("<none>");
                    tBook.setBorrowType("<set>'preborrowed'");
                    tBook.setBrokenType("<none>");
                    tBook.setBookId("<none>");

                    int i = 0;
                    for (i = 0; i < bookResult.size(); i++) {
                        if(bookResult.get(i).getBookId().equals(bookResult.get(i).getIsbn()))
                            continue;
                        tBook.setBookId("<value>'"+bookResult.get(i).getBookId()+"'");
                        tBorrow.setBookId(bookResult.get(i).getBookId());
                        book.change(tBook);
                        borrow.add(tBorrow);
                        break;
                    }
                    if (i < bookResult.size()) {
                        result.put("state", "success");
                    } else {
                        result.put("state", "fail");
                    }
                }
                else{
                    tBook.setIsbn("<value>'" + tBorrow.getBookId() + "'");
                    //tBook.setBorrowType("<value>'ebook'");
                    tBook.setBookId("<value>'" + tBorrow.getBookId() + "'");
                    List<TBook> bookResult = book.search(0, -1, tBook);
                    TBorrow eBorrow=new TBorrow();
                    eBorrow.setBookId("<value>'"+tBorrow.getBookId()+"'");
                    eBorrow.setUserId("<value>'"+tBorrow.getUserId()+"'");

                    List<TBorrow> borrowResult=borrow.search(0,-1,eBorrow);
                    int i=0;
                    for(i=0;i<borrowResult.size();i++){
                        if(Long.parseLong(borrowResult.get(i).getBorrowEndTime())>System.currentTimeMillis()){

                            result.put("state","fail");
                            return result;
                        }
                    }
                    if(bookResult.size()>0){
                        borrow.add(tBorrow);
                    }
                    else{
                        result.put("state","fail");
                        return result;
                    }

                }
            }
            catch(Exception e){
                result.put("state","fail");
            }
            return result;
        }
        else{
            try{
                if(ebook.equals("normal")&&(!tBook.getBookId().equals(tBook.getIsbn()))) {
                    book.change(tBook);
                    result.put("state", "success");
                }
                else{
                    result.put("state", "fail");
                }
            }
            catch(Exception e){
                result.put("state", "fail");
            }
            return result;
        }

    }

    public Map<String, Object> code_create(TUser tUser, TBorrow tBorrow){
        Map<String, Object> result=new HashMap<>();
        Calendar calendar=Calendar.getInstance();
        String codeText=String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH))
                +String.valueOf(calendar.get(Calendar.DATE)) +String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        codeText+= tUser.getUserId();
        codeText+="&"+tBorrow.getBookId()+"&";
        TUser require=new TUser();
        require.setUserId("<value>'"+tUser.getUserId()+"'");
        require.setPassword("<query>");
        String password=user.search(0,-1,require).get(0).getPassword();
        try{
            List<TBorrow> list= borrow.search(0,-1,tBorrow);
            TBook tBook=new TBook();
            tBook.setBookId(tBorrow.getBookId());
            tBook.setBorrowType("<value>'preborrowed'");
            List<TBook> list2=book.search(0,-1,tBook);
            tBook.setBorrowType("<value>'borrowed'");
            List<TBook> list3=book.search(0,-1,tBook);
            if(!(list.size()>0&&(list2.size()>0||list3.size()>0))){
                result.put("state","fail");
                return result;
            }
            else{
                tBorrow.setBorrowStartTime(list.get(list.size()-1).getBorrowStartTime());
                tBorrow.setBorrowEndTime(list.get(list.size()-1).getBorrowEndTime());
            }
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
        codeText+="&"+tBorrow.getBorrowStartTime();
        codeText+="&"+password;
        result.put("result", tUser.getUserId()+"&sp&"+tBorrow.getBookId()+"&sp&"+tBorrow.getBorrowEndTime()+"&sp&"+DigestUtils.md5DigestAsHex(codeText.getBytes()));
        result.put("state","success");
        return result;
    }
    public List<TBorrow> borrowSearch(TBorrow tBorrow, String type){
        List<TBorrow> result=new ArrayList<>();
        TBorrow state=new TBorrow();
        try {
            result=borrow.search(0, -1, tBorrow);
            int i=0;
            for(i=0;i<result.size()&&type!="no";i++){
                String bookId=result.get(i).getBookId();
                TBook tBook=new TBook();
                tBook.setBookId("<value>'"+bookId+"'");
                tBook.setBorrowType("<query>");
                tBook.setIsbn("<query>");
                TBook tBookResult=book.search(0,-1,tBook).get(0);
                String borrowType = tBookResult.getBorrowType();
                String isbn=tBookResult.getIsbn();
                if(type.equals("finish")&&borrowType.equals("finish")){
                    continue;
                }
                if((!borrowType.equals("finish"))||isbn.equals(bookId)){
                    if(type.equals("normal")&&System.currentTimeMillis()<Long.parseLong(result.get(i).getBorrowEndTime())){
                        continue;
                    }
                    if(type.equals("outtime")&&System.currentTimeMillis()>Long.parseLong(result.get(i).getBorrowEndTime())){
                        continue;
                    }
                }
                result.remove(i);
            }
            state.setUserId("success");
            result.add(state);
            return result;
        }
        catch(Exception e){
            state.setUserId("fail");
            result.add(state);
            return result;
        }

    }
    public Map<String, Object> cancelBorrow(TBorrow tBorrow){
        Map<String, Object> result=new HashMap<>();
        if(System.currentTimeMillis()/1000-Long.parseLong(tBorrow.getBorrowStartTime())/1000>3600*48){
            result.put("state", "fail");
            return result;
        }
        else{
            try {
                TBook tBook = new TBook();
                tBook.setBookId("<value>'" + tBorrow.getBookId() + "'");
                tBook.setBorrowType("<query>");
                List<TBook> searchResult = book.search(0, -1, tBook);
                if (searchResult.get(0).getBorrowType().contains("preborrowed")) {
                    borrow.delete(tBorrow);
                    tBook.setBorrowType("<set>'finish'");
                    book.change(tBook);
                    result.put("state","success");
                    return result;
                }
                else{
                    result.put("state","fail");
                    return result;
                }
            }
            catch(Exception e){
                result.put("state","fail");
                return result;
            }
        }
    }
    public Map<String, Object> eBookRead(TBorrow tBorrow, int type) throws IOException {

        Map<String, Object> result = new HashMap<>();
        if(type==0){
            try {
                List<TBorrow> borrowResult = borrow.search(0, -1, tBorrow);
                if(borrowResult.size()==0){
                    result.put("state","fail");
                    return result;
                }
                if(System.currentTimeMillis()>Long.parseLong(borrowResult.get(0).getBorrowEndTime())){
                    result.put("state","fail");
                    return result;
                }
            }
            catch(Exception e){
                result.put("state","fail");
                return result;
            }
        }
        try {
            TBookInfo tBookInfo = new TBookInfo();
            tBookInfo.setIsbn(tBorrow.getBookId());
            List<TBookInfo> bookResult = bookInfo.search(0, -1, 1, tBookInfo);
            if (bookResult.size() > 0) {
//                String path = bookResult.get(0).getEBook();
//                FileInputStream stream = new FileInputStream(path);
//                int len=stream.available();
//                byte[] bytes = new byte[len];
//                stream.read(bytes);
//                stream.close();
//                Base64.Encoder encoder = Base64.getEncoder();
//                String file = encoder.encodeToString(bytes);
                String ebook= Files.readString(Paths.get(bookResult.get(0).getEBook()));

                bookResult.get(0).setEBook(ebook);
                result.put("result", bookResult.get(0).getEBook());
                result.put("state", "success");
                return result;

            }
            else{
                result.put("state", "fail");
                return result;
            }
        }
        catch(Exception e){
            result.put("state", "fail");
            return result;
        }
    }


}
