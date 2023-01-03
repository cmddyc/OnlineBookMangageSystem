package book_sys.entity;


public class TBookInfo {

  private String isbn="<none>";
  private String bookName="<none>";
  private String bookAuthor="<none>";
  private String bookIntro="<none>";
  private String eBook="<none>";
  private String bookTypes="<none>";
  private String bookImg="<none>";
  private String bookPrice="<none>";


  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }


  public String getBookName() {
    return bookName;
  }

  public void setBookName(String bookName) {
    this.bookName = bookName;
  }


  public String getBookAuthor() {
    return bookAuthor;
  }

  public void setBookAuthor(String bookAuthor) {
    this.bookAuthor = bookAuthor;
  }


  public String getBookIntro() {
    return bookIntro;
  }

  public void setBookIntro(String bookIntro) {
    this.bookIntro = bookIntro;
  }


  public String getEBook() {
    return eBook;
  }

  public void setEBook(String eBook) {
    this.eBook = eBook;
  }


  public String getBookTypes() {
    return bookTypes;
  }

  public void setBookTypes(String bookTypes) {
    this.bookTypes = bookTypes;
  }


  public String getBookImg() {
    return bookImg;
  }

  public void setBookImg(String bookImg) {
    this.bookImg = bookImg;
  }


  public String getBookPrice() {
    return bookPrice;
  }

  public void setBookPrice(String bookPrice) {
    this.bookPrice = bookPrice;
  }

}
