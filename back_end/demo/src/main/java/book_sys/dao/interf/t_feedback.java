package book_sys.dao.interf;

import book_sys.entity.TFeedback;

import java.util.List;

public interface t_feedback {
    List<TFeedback> search(int start, int length, TFeedback feedback);
    int add(TFeedback feedback);
    int delete(TFeedback feedback);
    int change(TFeedback feedback);
}
