package book_sys.dao.interf;

import book_sys.entity.TAdmin;

import java.util.List;

public interface t_admin {
    List<TAdmin> search(TAdmin admin);
    int add(TAdmin admin);
    int delete(TAdmin admin);
    int change(TAdmin admin);

}
