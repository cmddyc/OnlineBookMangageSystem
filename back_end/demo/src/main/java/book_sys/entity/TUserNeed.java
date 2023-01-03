package book_sys.entity;


public class TUserNeed {

  private String userId = "<none>";
  private String isbn = "<none>";
  private String preBorrowStartTime = "<none>";
  private String preBorrowEndTime = "<none>";
  private String note = "<none>";


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


  public String getPreBorrowStartTime() {
    return preBorrowStartTime;
  }

  public void setPreBorrowStartTime(String preBorrowStartTime) {
    this.preBorrowStartTime = preBorrowStartTime;
  }


  public String getPreBorrowEndTime() {
    return preBorrowEndTime;
  }

  public void setPreBorrowEndTime(String preBorrowEndTime) {
    this.preBorrowEndTime = preBorrowEndTime;
  }


  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

}
