package book_sys.entity;


public class TBookComment {

  private String userId = "<none>";
  private String isbn = "<none>";
  private String comment = "<none>";
  private String commentTime = "<none>";


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }


  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }


  public String getCommentTime() {
    return commentTime;
  }

  public void setCommentTime(String commentTime) {
    this.commentTime = commentTime;
  }

}
