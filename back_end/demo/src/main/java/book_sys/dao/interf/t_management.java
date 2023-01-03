package book_sys.dao.interf;

import book_sys.entity.TManagement;

import java.util.List;

public interface t_management {
    List<TManagement> search(int start, int length, TManagement management);
    int add(TManagement management);
    int delete(TManagement management);
    int change(TManagement management);
}
