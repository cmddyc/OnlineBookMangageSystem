package book_sys.entity;


public class TBorrow {

  private String bookId = "<none>";
  private String userId = "<none>";
  private String borrowStartTime = "<none>";
  private String borrowEndTime = "<none>";
  private String note = "<none>";


  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getBorrowStartTime() {
    return borrowStartTime;
  }

  public void setBorrowStartTime(String borrowStartTime) {
    this.borrowStartTime = borrowStartTime;
  }


  public String getBorrowEndTime() {
    return borrowEndTime;
  }

  public void setBorrowEndTime(String borrowEndTime) {
    this.borrowEndTime = borrowEndTime;
  }


  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

}
