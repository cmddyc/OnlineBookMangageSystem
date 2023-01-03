package book_sys.dao.interf;

import book_sys.entity.TBookInfo;

import java.util.List;

public interface t_book_info {
    List<TBookInfo> search(int start, int length, int type, TBookInfo bookInfo);

    int add(TBookInfo bookInfo);
    int delete(TBookInfo bookInfo);
    int change(TBookInfo bookInfo);
}
