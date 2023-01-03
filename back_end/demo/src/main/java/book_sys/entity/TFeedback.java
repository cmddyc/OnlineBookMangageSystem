package book_sys.entity;


public class TFeedback {

  private String feedbackTimeId="<none>";
  private String userId="<none>";
  private String feedbackContent="<none>";
  private String feedbackState="<none>";


  public String getFeedbackTimeId() {
    return feedbackTimeId;
  }

  public void setFeedbackTimeId(String feedbackTimeId) {
    this.feedbackTimeId = feedbackTimeId;
  }


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getFeedbackContent() {
    return feedbackContent;
  }

  public void setFeedbackContent(String feedbackContent) {
    this.feedbackContent = feedbackContent;
  }


  public String getFeedbackState() {
    return feedbackState;
  }

  public void setFeedbackState(String feedbackState) {
    this.feedbackState = feedbackState;
  }

}
