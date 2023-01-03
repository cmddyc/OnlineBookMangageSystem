package book_sys.service;

import book_sys.dao.interf.t_book_comment;
import book_sys.entity.TBookComment;
import book_sys.entity.TFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class book_comment_service {
    @Autowired
    t_book_comment bookComment;
    public Map<String, Object> bookComment(TBookComment tBookComment){
        Map<String, Object> result=new HashMap<>();
        try{
            bookComment.add(tBookComment);
            result.put("state","success");
            return result;
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
    }
    public Map<String, Object> searchBookComment(TBookComment tBookComment, String startTime, String endTime){
        Map<String, Object> result=new HashMap<>();
        try{
            List<TBookComment> list=bookComment.search(0,-1,tBookComment);
            if(!(startTime.equals("no")||endTime.equals("no"))){
                int i=0;
                for(i=0;i<list.size();i++){
                    if(Long.parseLong(list.get(i).getCommentTime())<Long.parseLong(startTime)||Long.parseLong(list.get(i).getCommentTime())>Long.parseLong(endTime)){
                        list.remove(i);
                    }
                }
            }
            result.put("result",list);
            result.put("state","success");
            return result;
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
    }
    public Map<String, Object> deleteBookComment(TBookComment tBookComment){
        Map<String, Object> result=new HashMap<>();
        try{
            bookComment.delete(tBookComment);
            result.put("state","success");
            return result;
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
    }
}
