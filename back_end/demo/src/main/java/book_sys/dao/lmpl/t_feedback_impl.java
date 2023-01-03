package book_sys.dao.lmpl;

import book_sys.entity.TFeedback;
import book_sys.dao.interf.t_feedback;

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
public class t_feedback_impl implements t_feedback{


    @Autowired
    private JdbcTemplate jdbcTemplate;



    private String sql_create(String type, TFeedback feedback){
        String sql="where ";
        String select=type;//"select ""delete from table""update table set "
        if(!feedback.getFeedbackTimeId().contains("<none>")){
            if(feedback.getFeedbackTimeId().contains("<value>")) {
                sql += "feedback_time_id=" + feedback.getFeedbackTimeId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& feedback.getFeedbackTimeId().contains("<set>")){
                select +=  "feedback_time_id=" + feedback.getFeedbackTimeId().substring(5)+",";
            }
            if(select.contains("select")&& feedback.getFeedbackTimeId().contains("<query>")){
                select+="feedback_time_id,";
            }
        }
        if(!feedback.getUserId().contains("<none>")){
            if(feedback.getUserId().contains("<value>")) {
                sql += "user_id=" + feedback.getUserId().split("value>")[1]+" and ";
            }
            if(select.contains("update")&& feedback.getUserId().contains("<set>")){
                select +=  "user_id=" + feedback.getUserId().substring(5)+",";
            }
            if(select.contains("select")&& feedback.getUserId().contains("<query>")){
                select += "user_id,";
            }
        }
        if(!feedback.getFeedbackContent().contains("<none>")){
            if(feedback.getFeedbackContent().contains("<value>")) {
                sql += "feedback_content=" + feedback.getFeedbackContent()+" and ";
            }
            if(select.contains("update")&& feedback.getFeedbackContent().contains("<set>")){
                select +=  "feedback_content=" + feedback.getFeedbackContent().substring(5)+",";
            }
            if(select.contains("select")&& feedback.getFeedbackContent().contains("<query>")){
                select+="feedback_content,";
            }
        }
        if(!feedback.getFeedbackState().contains("<none>")){
            if(feedback.getFeedbackState().contains("<value>")) {
                sql += "feedback_state=" + feedback.getFeedbackState().split("value>")[1]+" and ";
            }
            else if(select.contains("update")&& feedback.getFeedbackState().contains("<set>")){
                select +=  "feedback_state=" + feedback.getFeedbackState().substring(5)+",";
            }
            else if(select.contains("select")&& feedback.getFeedbackState().contains("<query>")){
                select+="feedback_state,";
            }
        }
        if(select.equals("select ")){
            select+="* from t_feedback ";
        }
        else if(select.contains("select")) {
            select=select.substring(0,select.length()-1)+" from t_feedback ";
        }
        if(select.equals("update t_feedback set ")){
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
    public List<TFeedback> search(int start, int length, TFeedback feedback){
        List<TFeedback> result=new ArrayList<>();
        String sql=sql_create("select ", feedback);

        if(length!=-1) {
            sql=sql.substring(0, sql.length()-1);
            sql += " limit " + String.valueOf(start) + "," + String.valueOf(length) + ";";
        }
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            return result;
        }
        for (Map<String, Object> stringObjectMap : list) {
            TFeedback each_row = new TFeedback();
            if(stringObjectMap.get("feedback_time_id")!=null) {
                each_row.setFeedbackTimeId(stringObjectMap.get("feedback_time_id").toString());
            }
            if(stringObjectMap.get("user_id")!=null) {
                each_row.setUserId(stringObjectMap.get("user_id").toString());
            }
            if(stringObjectMap.get("feedback_content")!=null) {
                each_row.setFeedbackContent(stringObjectMap.get("feedback_content").toString());
            }
            if(stringObjectMap.get("feedback_state")!=null) {
                each_row.setFeedbackState(stringObjectMap.get("feedback_state").toString());
            }
            result.add(each_row);
        }
        return result;
    }
    public int add(TFeedback feedback){


        String sql="insert into t_feedback values(?,?,?,?)";

        if(feedback.getUserId().contains("<none>")&& feedback.getFeedbackTimeId().contains("<none>")
                && feedback.getFeedbackContent().contains("<none>")&& feedback.getFeedbackState().contains("<none>")){
            return 0;
        }

        Object[] args={feedback.getFeedbackTimeId(), feedback.getUserId(), feedback.getFeedbackContent(), feedback.getFeedbackState()};

        return jdbcTemplate.update(sql,args);
    }
    public int delete(TFeedback feedback){

        String sql=sql_create("delete from t_feedback ", feedback);

        return jdbcTemplate.update(sql);
    }
    public int change(TFeedback feedback){

        String sql=sql_create("update t_feedback set ", feedback);

        if(sql.equals("false")){
            return -1;
        }
        return jdbcTemplate.update(sql);
    }

}

