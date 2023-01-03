package book_sys.controller;

import book_sys.entity.*;
import book_sys.service.book_borrow_service;
import book_sys.service.book_return_service;
import book_sys.service.book_search_service;
import book_sys.service.user_login_service;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class bookBorrowManage {
    @Autowired(required = false)
    user_login_service userLoginService;
    @Autowired(required = false)
    book_search_service bookSearchService;
    @Autowired(required = false)
    book_borrow_service bookBorrowService;
    @Autowired(required = false)
    book_return_service bookReturnService;


    @PostMapping("/bookSearch")
    public Map<String, Object> bookSearch(@RequestParam(value="id") String id,@RequestParam(value="token") String token,@RequestParam(value="isbn") String isbn,@RequestParam(value="bookId") String book_id,@RequestParam(value="name") String name,@RequestParam(value="author") String author,@RequestParam(value="type") String type,@RequestParam(value="brokenType") String broken_type,@RequestParam(value="borrowType") String borrow_type){


        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        TBook tBook=new TBook();
        TBookInfo tBookInfo=new TBookInfo();
        if(!(isbn.equals("no")||isbn.contains(">"))) {
            tBook.setIsbn("<value>'"+isbn+"'");
            tBookInfo.setIsbn("<value>'"+isbn+"'");
        }
        else{
            tBook.setIsbn("<none>");
            tBookInfo.setIsbn("<none>");
        }
        if(!book_id.equals("no"))
            tBook.setBookId("<value>'"+book_id+"'");
        else{
            tBook.setBookId("<none>");
        }
        if(!name.equals("no"))
            tBookInfo.setBookName("<value>'%"+name+"%'");
        else{
            tBookInfo.setBookName("<none>");
        }
        if(!author.equals("no"))
            tBookInfo.setBookAuthor("<value>'%"+author+"%'");
        else{
            tBookInfo.setBookAuthor("<none>");
        }
        if(!type.equals("no"))
            tBookInfo.setBookTypes("<value>'"+type+"'");
        else{
            tBookInfo.setBookTypes("<none>");
        }
        if(checkResult.get("result").equals("admin")){

            if(!borrow_type.equals("no"))
                tBook.setBorrowType("<value>'"+borrow_type+"'");
            else{
                tBook.setBorrowType("<none>");
            }
            if(!broken_type.equals("no"))
                tBook.setBrokenType("<value>'"+broken_type+"'");
            else{
                tBook.setBrokenType("<none>");
            }
        }
        Map<String, Object> result=new HashMap<>();
        List<Map<String,Object>> service=bookSearchService.bookSearch(tBook, tBookInfo, 1);
        int size=service.size();
        Map<String, Object> state=service.get(size-1);
        service.remove(size-1);
        result.put("result",service);
        result.put("state",state.get("state"));
        return result;

    }
    @PostMapping("/borrow")
    public Map<String, Object> borrow(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("isbn") String isbn,@RequestParam("startTime") String start_time,@RequestParam("endTime") String end_time,@RequestParam("note") String note,@RequestParam("type") String type){
        TBorrow tBorrow=new TBorrow();
        TUser tUser=new TUser();
        tUser.setUserId(id);
        tUser.setPassword(token);
        TBook tBook=new TBook();
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUser);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        if(checkResult.get("result").equals("admin")){
            tBook.setBookId("<value>'"+isbn+"'");
            tBook.setBorrowType("<set>'borrowed'");
            return bookBorrowService.bookBorrow(tBook, tUser, tBorrow,1, type);
        }
        else{
            Long endTime=Long.parseLong(end_time);
            tUser.setUserId("<value>'"+id+"'");
            tUser.setCredit("<query>");
            tBorrow.setBookId(isbn);

            tBorrow.setBorrowStartTime(String.valueOf(System.currentTimeMillis()));
            endTime=Long.parseLong(tBorrow.getBorrowStartTime())+endTime*24*3600*1000;
            end_time=String.valueOf(endTime);
            if(Long.parseLong(end_time)/1000-System.currentTimeMillis()/1000<3600*24*365&&Long.parseLong(end_time)>System.currentTimeMillis()) {
                tBorrow.setBorrowEndTime(end_time);
            }
            tBorrow.setUserId(id);
            tBorrow.setNote(type+"<type>"+note);
            return bookBorrowService.bookBorrow(tBook, tUser, tBorrow,0, type);
        }
    }
    @PostMapping("/borrowCode")
    public Map<String, Object> borrowCode(@RequestParam("id") String id, @RequestParam("token") String token, @RequestParam("userId") String userId,@RequestParam("bookId") String bookId){
        Map<String, Object> result=new HashMap<>();
        TUser tUser=new TUser();
        tUser.setPassword(token);
        tUser.setUserId(id);
        TBorrow tBorrow=new TBorrow();
        tBorrow.setBookId("<value>'"+bookId+"'");
        tBorrow.setUserId("<value>'"+id+"'");

        Map<String, Object> checkResult=userLoginService.check(tUser);
        if(checkResult.get("state").equals("fail")){
            result.put("state","fail");
            return result;
        }
        else if(checkResult.get("result").equals("admin")){
            tUser.setUserId(userId);
            tBorrow.setUserId("<value>'"+userId+"'");

            return bookBorrowService.code_create(tUser,tBorrow);

        }
        else{
            return bookBorrowService.code_create(tUser,tBorrow);

        }
    }
    @PostMapping("/borrowSearch")
    public Map<String, Object> borrowSearch(@RequestParam("id") String id, @RequestParam("token") String token, @RequestParam("bookId") String bookId, @RequestParam("userId") String userId, @RequestParam("type") String type){
        Map<String, Object> result=new HashMap<>();
        TUser tUser=new TUser();
        tUser.setPassword(token);
        tUser.setUserId(id);
        TBorrow tBorrow=new TBorrow();
        if(!bookId.equals("no")) {
            tBorrow.setBookId("<value>'" + bookId+"'");
        }

        Map<String, Object> checkResult=userLoginService.check(tUser);

        if(checkResult.get("state").equals("fail")){
            result.put("state","fail");
            return result;
        }
        else if(checkResult.get("result").equals("admin")){
            if(!userId.equals("no")){
                tBorrow.setUserId("<value>'"+userId+"'");
            }
        }
        else{
            tBorrow.setUserId("<value>'"+id+"'");
        }
        List<TBorrow> service= bookBorrowService.borrowSearch(tBorrow,type);
        if(service.get(service.size()-1).getUserId().equals("success")){
            service.remove(service.size()-1);
            result.put("state","success");
            result.put("result",service);
        }
        else{
            result.put("state","fail");
        }
        return result;
    }
    @PostMapping("cancelBorrow")
    public Map<String, Object> cancelBorrow(@RequestParam("id") String id, @RequestParam("token") String token, @RequestParam("bookId") String bookId, @RequestParam("startTime") String startTime){
        Map<String, Object> result=new HashMap<>();
        TUser tUser=new TUser();
        tUser.setPassword(token);
        tUser.setUserId(id);
        TBorrow tBorrow=new TBorrow();
        tBorrow.setBookId("<value>'" + bookId+"'");
        tBorrow.setBorrowStartTime("<value>'" + startTime+"'");

        tBorrow.setUserId("<value>'" + id+"'");
        Map<String, Object> checkResult=userLoginService.check(tUser);

        if(checkResult.get("state").equals("fail")){
            result.put("state","fail");
            return result;
        }
        else{
            return bookBorrowService.cancelBorrow(tBorrow);
        }
    }
    @PostMapping("/eBookRead")
    public Map<String, Object> eBookRead(@RequestParam("id") String id, @RequestParam("token") String token, @RequestParam("bookId") String bookId, @RequestParam("startTime") String startTime) throws IOException {
        Map<String, Object> result=new HashMap<>();
        TUser tUser=new TUser();
        tUser.setPassword(token);
        tUser.setUserId(id);
        TBorrow tBorrow=new TBorrow();
        tBorrow.setBookId("<value>'" + bookId+"'");
        tBorrow.setBorrowStartTime("<value>'" + startTime + "'");


        tBorrow.setUserId("<value>'" + id+"'");
        Map<String, Object> checkResult=userLoginService.check(tUser);

        if(checkResult.get("state").equals("fail")){
            result.put("state","fail");
            return result;
        }
        else if(checkResult.get("result").equals("admin")){
            tBorrow.setBorrowStartTime("<none>");
            tBorrow.setUserId("<none>");

            return bookBorrowService.eBookRead(tBorrow,1);
        }
        else{
            return bookBorrowService.eBookRead(tBorrow,0);
        }
    }
    @PostMapping("/admin/return")
    public Map<String, Object> returns(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("userId") String userId,@RequestParam("bookId") String bookId,@RequestParam("type") String type,@RequestParam("note") String note){
        Map<String, Object> result=new HashMap<>();
        TUser tUser=new TUser();
        tUser.setPassword(token);
        tUser.setUserId(id);
        Map<String, Object> checkResult=userLoginService.check(tUser);

        if(checkResult.get("state").equals("fail")){
            result.put("state","fail");
            return result;
        }
        else if(checkResult.get("result").equals("admin")) {
            TReturn tReturn=new TReturn();
            tReturn.setBookId(bookId);
            tReturn.setNote(note);
            tReturn.setUserId(userId);
            tReturn.setReturnTime(String.valueOf(System.currentTimeMillis()));
            if(type.equals("normal")||type.equals("broken")||type.equals("lose")) {
                tReturn.setReturnType(type);
            }
            return bookReturnService.returns(tReturn);

        }
        else{
            result.put("state","fail");
            return result;
        }

    }
    @PostMapping("/returnSearch")
    public Map<String,Object> returnSearch(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("userId") String userId,@RequestParam("type") String type){
        TReturn tReturn=new TReturn();
        Map<String, Object> result=new HashMap<>();
        TUser tUser=new TUser();
        tUser.setPassword(token);
        tUser.setUserId(id);
        Map<String, Object> checkResult=userLoginService.check(tUser);

        if(checkResult.get("state").equals("fail")){
            result.put("state","fail");
            return result;
        }
        else if(checkResult.get("result").equals("admin")){
            if(!userId.equals("no"))
                tReturn.setUserId(userId);
            tReturn.setReturnType(type);
        }
        else{
            tReturn.setUserId(id);
            tReturn.setReturnType(type);
        }
        List<Map<String,Object>> service=bookReturnService.returnSearch(tReturn);
        Map<String, Object> state=service.get(service.size()-1);
        service.remove(service.size()-1);
        result.put("state",state.get("state"));
        result.put("result",service);
        return result;
    }
}
