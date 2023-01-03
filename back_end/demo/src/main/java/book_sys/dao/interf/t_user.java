package book_sys.dao.interf;

import book_sys.entity.TUser;

import java.util.List;

public interface t_user {
    List<TUser> search(int start, int length, TUser user);
    int add(TUser user);
    int delete(TUser user);
    int change(TUser user);
}
