package book_sys.controller;

import book_sys.entity.TBook;
import book_sys.entity.TBookInfo;
import book_sys.entity.TManagement;
import book_sys.entity.TUser;
import book_sys.service.book_change_service;
import book_sys.service.user_login_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class bookManage {
    @Autowired(required = false)
    user_login_service userLoginService;

    @Autowired(required = false)
    book_change_service bookChangeService;

    @PostMapping("/admin/addBook")
    public Map<String, Object> addBook(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("isbn") String isbn,@RequestParam("bookId") String bookId){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            TBook tBook=new TBook();
            tBook.setBookId(bookId);
            tBook.setBrokenType("normal");
            tBook.setBorrowType("finish");
            if(bookId.equals(isbn)){
                tBook.setBorrowType("ebook");
            }
            tBook.setIsbn(isbn);
            return bookChangeService.addBook(tBook,id);
        }
        else{
            checkResult.put("state","fail");
            return checkResult;
        }
    }
    @PostMapping("/admin/addBookInfo")
    public Map<String, Object> addBookInfo(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("isbn") String isbn,@RequestParam("name") String name,@RequestParam("author") String author,@RequestParam("intro") String intro,@RequestParam("price") String price,@RequestParam("img") String img,@RequestParam("type") String type,@RequestParam("ebook") String ebook) throws IOException {

        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            TBookInfo tBookInfo=new TBookInfo();
            tBookInfo.setBookTypes(type);
            tBookInfo.setBookName(name);
            tBookInfo.setBookIntro(intro);
            tBookInfo.setBookAuthor(author);
            tBookInfo.setBookPrice(price);
            tBookInfo.setIsbn(isbn);
            if(!ebook.equals("no")){
                try {
                    FileWriter fileWriter = new FileWriter("C:/Users/lenovo/Desktop/book/"+isbn+".txt");
                    fileWriter.write(ebook);
                    fileWriter.close();
                }
                catch (Exception e){
                    System.out.print(e);
                }
                ebook="C:/Users/lenovo/Desktop/book/"+isbn+".txt";

            }
            tBookInfo.setEBook(ebook);
            if(!img.equals("no")){
                try {
                    FileWriter fileWriter = new FileWriter("C:/Users/lenovo/Desktop/book/"+isbn+"picture.txt");
                    fileWriter.write(img);
                    fileWriter.close();
                }
                catch (Exception e){
                    System.out.print(e);
                }
                img="C:/Users/lenovo/Desktop/book/"+isbn+"picture.txt";

            }
            tBookInfo.setBookImg(img);
            return bookChangeService.addBookInfo(tBookInfo,id);
        }
        else{
            checkResult.put("state","fail");
            return checkResult;
        }
    }
    @PostMapping("/admin/changeBook")
    public Map<String, Object> changeBook(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("isbn") String isbn,@RequestParam("borrowType") String borrowType,@RequestParam("brokenType") String brokenType,@RequestParam("newBookId") String bookId,@RequestParam("bookId") String oldBookId){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            TBook tBook=new TBook();
            if(!bookId.equals("no"))
                tBook.setBookId("<set>'"+bookId+"'"+"<value>'"+oldBookId+"'");
            else{
                tBook.setBookId("<value>'"+oldBookId+"'");
            }
            if(!brokenType.equals("no"))
                tBook.setBrokenType("<set>'"+brokenType+"'");
            if(!borrowType.equals("no"))
                tBook.setBorrowType("<set>'"+borrowType+"'");
            if(!isbn.equals("no"))
                tBook.setIsbn("<set>'"+isbn+"'");
            return bookChangeService.changeBook(tBook,id,bookId);
        }
        else{
            checkResult.put("state","fail");
            return checkResult;
        }
    }
    @PostMapping("/admin/changeBookInfo")
    public Map<String, Object> changeBookInfo(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("newIsbn") String isbn,@RequestParam("isbn") String oldIsbn,@RequestParam("name") String name,@RequestParam("author") String author,@RequestParam("intro") String intro,@RequestParam("price") String price,@RequestParam("img") String img,@RequestParam String type,@RequestParam String ebook) throws IOException {

        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            TBookInfo tBookInfo=new TBookInfo();
            if(!type.equals("no"))
                tBookInfo.setBookTypes("<set>'"+type+"'");
            if(!name.equals("no"))
                tBookInfo.setBookName("<set>'"+name+"'");
            if(!intro.equals("no"))
                tBookInfo.setBookIntro("<set>'"+intro+"'");
            if(!author.equals("no"))
                tBookInfo.setBookAuthor("<set>'"+author+"'");
            if(!price.equals("no"))
                tBookInfo.setBookPrice("<set>'"+price+"'");
            if(!isbn.equals("no"))
                tBookInfo.setIsbn("<set>'"+isbn+"'<value>'"+oldIsbn+"'");
            else{
                tBookInfo.setIsbn("'<value>'"+oldIsbn+"'");
            }
            if(!img.equals("no")) {

                try {
                    FileWriter fileWriter = new FileWriter("C:/Users/lenovo/Desktop/book/" + isbn + "picture.txt");
                    fileWriter.write(ebook);
                    fileWriter.close();
                } catch (Exception e) {
                    System.out.print(e);
                }
                img = "C:/Users/lenovo/Desktop/book/" + isbn + "picture.txt";


                tBookInfo.setBookImg("<set>'" + img + "'");
            }
            if(!ebook.equals("no")) {
                try {
                    FileWriter fileWriter = new FileWriter("C:/Users/lenovo/Desktop/book/" + isbn + ".txt");
                    fileWriter.write(img);
                    fileWriter.close();
                } catch (Exception e) {
                    System.out.print(e);
                }
                ebook = "C:/Users/lenovo/Desktop/book/" + isbn + ".txt";

                tBookInfo.setEBook(ebook);
                tBookInfo.setEBook("<set>'" + ebook + "'");
            }
            return bookChangeService.changeBookInfo(tBookInfo,id, isbn);
        }
        else{
            checkResult.put("state","fail");
            return checkResult;
        }
    }
    @PostMapping("/admin/deleteBook")
    public Map<String, Object> deleteBook(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("bookId") String bookId){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            TBook tBook=new TBook();
            tBook.setBookId("<value>'"+bookId+"'");
            return bookChangeService.deleteBook(tBook,id,bookId);
        }
        else{
            checkResult.put("state","fail");
            return checkResult;
        }
    }
    @PostMapping("/admin/deleteBookInfo")
    public Map<String, Object> deleteBookInfo(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("isbn") String isbn) throws IOException {

        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            TBookInfo tBookInfo=new TBookInfo();
            tBookInfo.setIsbn("<value>'"+isbn+"'");

            return bookChangeService.deleteBookInfo(tBookInfo,id,isbn);
        }
        else{
            checkResult.put("state","fail");
            return checkResult;
        }
    }
    @PostMapping("/admin/manageSearch")
    public Map<String, Object> manageSearch(@RequestParam("id") String id,@RequestParam("token") String token,@RequestParam("adminId") String adminId,@RequestParam("bookId") String bookId,@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            TManagement tManagement=new TManagement();
            if(!adminId.equals("no")){
                tManagement.setAdminId("<value>'"+adminId+"'");
            }
            if(!bookId.equals("no")){
                tManagement.setBookId("<value>'"+bookId+"'");
            }

            return bookChangeService.manageSearch(tManagement,startTime,endTime);
        }
        else{
            checkResult.put("state","fail");
            return checkResult;
        }
    }
}
