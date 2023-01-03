package book_sys.service;

import book_sys.dao.interf.t_feedback;
import book_sys.entity.TFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class feed_back_service {
    @Autowired
    t_feedback feedback;
    public Map<String, Object> addFeedBack(TFeedback tFeedback){
        Map<String, Object> result=new HashMap<>();
        try{
            feedback.add(tFeedback);
            result.put("state","success");
            return result;
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
    }
    public Map<String, Object> searchFeedBack(TFeedback tFeedback, String startTime, String endTime){
        Map<String, Object> result=new HashMap<>();
        try{
            List<TFeedback> list=feedback.search(0,-1,tFeedback);
            if(!(startTime.equals("no")||endTime.equals("no"))){
                int i=0;
                for(i=0;i<list.size();i++){
                    if(Long.parseLong(list.get(i).getFeedbackTimeId())<Long.parseLong(startTime)||Long.parseLong(list.get(i).getFeedbackTimeId())>Long.parseLong(endTime)){
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
    public Map<String, Object> handleFeedBack(TFeedback tFeedback){
        Map<String, Object> result=new HashMap<>();
        try{
            feedback.change(tFeedback);
            result.put("state","success");
            return result;
        }
        catch(Exception e){
            result.put("state","fail");
            return result;
        }
    }
}
