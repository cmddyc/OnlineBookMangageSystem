package book_sys.dao.interf;

import book_sys.entity.TUserNeed;

import java.util.List;

public interface t_user_need {
    List<TUserNeed> search(int start, int length, TUserNeed userNeed);
    int add(TUserNeed userNeed);
    int delete(TUserNeed userNeed);
    int change(TUserNeed userNeed);
}
