package book_sys.dao.interf;

import book_sys.entity.TBorrow;

import java.util.List;

public interface t_borrow {
    List<TBorrow> search(int start, int length, TBorrow borrow);
    int add(TBorrow borrow);
    int delete(TBorrow borrow);
    int change(TBorrow borrow);
}
