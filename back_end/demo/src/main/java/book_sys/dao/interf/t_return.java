package book_sys.dao.interf;

import book_sys.entity.TReturn;

import java.util.List;

public interface t_return {
    List<TReturn> search(int start, int length, TReturn tReturn);
    int add(TReturn tReturn);
    int delete(TReturn tReturn);
    int change(TReturn tReturn);
}
