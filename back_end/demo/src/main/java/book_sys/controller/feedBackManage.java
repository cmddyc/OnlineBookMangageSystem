package book_sys.controller;

import book_sys.entity.TBookComment;
import book_sys.entity.TFeedback;
import book_sys.entity.TManagement;
import book_sys.entity.TUser;
import book_sys.service.book_comment_service;
import book_sys.service.feed_back_service;
import book_sys.service.user_login_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class feedBackManage {
    @Autowired
    user_login_service userLoginService;
    @Autowired
    feed_back_service feedBackService;
    @Autowired
    book_comment_service bookCommentService;
    @PostMapping("/user/feedback")
    public Map<String, Object> feedBack(@RequestParam("id") String id,@RequestParam("token") String token, @RequestParam("content") String content){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            checkResult.put("state", "fail");
            return checkResult;
        }
        else{
            TFeedback tFeedback=new TFeedback();
            tFeedback.setUserId(id);
            tFeedback.setFeedbackContent(content);
            tFeedback.setFeedbackState("unhandled");
            tFeedback.setFeedbackTimeId(String.valueOf(System.currentTimeMillis()));
            return feedBackService.addFeedBack(tFeedback);
        }
    }
    @PostMapping("/searchFeedBack")
    public Map<String, Object> searchFeedBack(@RequestParam("id") String id,@RequestParam("token") String token, @RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,@RequestParam("state") String state){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        TFeedback tFeedback=new TFeedback();
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            System.out.print("admin");
        }
        else{
            tFeedback.setUserId("<value>'"+id+"'");
        }
        if(!state.equals("no"))
            tFeedback.setFeedbackState("<value>'"+state+"'");
        return feedBackService.searchFeedBack(tFeedback,startTime,endTime);
    }

    @PostMapping("/admin/handleFeedBack")
    public Map<String, Object> handleFeedBack(@RequestParam("id") String id,@RequestParam("token") String token, @RequestParam("userId") String userId, @RequestParam("feedbackTimeId") String timeId, @RequestParam("feedbackState") String feedbackState){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            TFeedback tFeedback=new TFeedback();
            tFeedback.setUserId("<value>'"+userId+"'");
            tFeedback.setFeedbackTimeId("<value>'"+timeId+"'");
            tFeedback.setFeedbackState("<set>'"+feedbackState+"'");
            return feedBackService.handleFeedBack(tFeedback);
        }
        else{
            checkResult.put("state", "fail");
            return checkResult;
        }
    }
    @PostMapping("/user/bookComment")
    public Map<String, Object> bookComment(@RequestParam("id") String id,@RequestParam("token") String token, @RequestParam("comment") String comment, @RequestParam("isbn") String isbn){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            checkResult.put("state", "fail");
            return checkResult;
        }
        else{
            TBookComment tBookComment=new TBookComment();
            tBookComment.setUserId(id);
            tBookComment.setComment(comment);
            tBookComment.setIsbn(isbn);
            tBookComment.setCommentTime(String.valueOf(System.currentTimeMillis()));
            return bookCommentService.bookComment(tBookComment);
        }
    }
    @PostMapping("/bookCommentSearch")
    public Map<String, Object> searchBookComment(@RequestParam("id") String id,@RequestParam("token") String token, @RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,@RequestParam("isbn") String isbn,@RequestParam("userId") String userId){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        TBookComment tBookComment=new TBookComment();
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            System.out.print("admin");
        }
        else{
            System.out.print("user");
        }
        if(!isbn.equals("no"))
            tBookComment.setIsbn("<value>'"+isbn+"'");
        if(!userId.equals("no"))
            tBookComment.setUserId("<value>'"+userId+"'");
        return bookCommentService.searchBookComment(tBookComment,startTime,endTime);
    }

    @PostMapping("/admin/deleteBookComment")
    public Map<String, Object> deleteBookComment(@RequestParam("id") String id,@RequestParam("token") String token, @RequestParam("userId") String userId, @RequestParam("commentTime") String commentTime,@RequestParam("isbn") String isbn){
        TUser tUserRequest=new TUser();
        tUserRequest.setUserId(id);
        tUserRequest.setPassword(token);
        Map<String, Object> checkResult=new HashMap<>();
        checkResult=userLoginService.check(tUserRequest);
        if(checkResult.get("state").equals("fail")){
            return checkResult;
        }
        else if(checkResult.get("result").equals("admin")) {
            TBookComment tBookComment=new TBookComment();
            tBookComment.setUserId("<value>'"+userId+"'");
            tBookComment.setIsbn("<value>'"+isbn+"'");
            tBookComment.setCommentTime("<value>'"+commentTime+"'");
            return bookCommentService.deleteBookComment(tBookComment);
        }
        else{
            checkResult.put("state", "fail");
            return checkResult;
        }
    }

}
