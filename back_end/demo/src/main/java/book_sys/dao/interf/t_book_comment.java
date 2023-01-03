package book_sys.dao.interf;

import book_sys.entity.TBookComment;

import java.util.List;

public interface t_book_comment {
    List<TBookComment> search(int start, int length, TBookComment bookComment);
    int add(TBookComment bookComment);
    int delete(TBookComment bookComment);
    int change(TBookComment bookComment);
}
