package book_sys.entity;


public class TManagement {

  private String adminId="<none>";
  private String bookId="<none>";
  private String manageTime="<none>";
  private String manageType="<none>";


  public String getAdminId() {
    return adminId;
  }

  public void setAdminId(String adminId) {
    this.adminId = adminId;
  }


  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }


  public String getManageTime() {
    return manageTime;
  }

  public void setManageTime(String manageTime) {
    this.manageTime = manageTime;
  }


  public String getManageType() {
    return manageType;
  }

  public void setManageType(String manageType) {
    this.manageType = manageType;
  }

}
