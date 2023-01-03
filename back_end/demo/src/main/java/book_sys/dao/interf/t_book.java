package book_sys.dao.interf;

import book_sys.entity.TBook;

import java.util.List;

public interface t_book {
    List<TBook> search(int start, int length, TBook book);
    int add(TBook book);
    int delete(TBook book);
    int change(TBook book);
}
