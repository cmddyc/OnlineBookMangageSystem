package book_sys.entity;


public class TBook {

  private String isbn = "<none>";
  private String bookId = "<none>";
  private String borrowType = "<none>";
  private String brokenType = "<none>";


  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }


  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }


  public String getBorrowType() {
    return borrowType;
  }

  public void setBorrowType(String borrowType) {
    this.borrowType = borrowType;
  }


  public String getBrokenType() {
    return brokenType;
  }

  public void setBrokenType(String brokenType) {
    this.brokenType = brokenType;
  }

}
