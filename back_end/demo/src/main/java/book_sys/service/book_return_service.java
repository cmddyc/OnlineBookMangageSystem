package book_sys.service;

import book_sys.dao.interf.*;
import book_sys.entity.TBook;
import book_sys.entity.TBorrow;
import book_sys.entity.TReturn;
import book_sys.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class book_return_service {
    @Autowired
    t_borrow borrow;
    @Autowired
    t_user user;
    @Autowired
    t_book book;
    @Autowired
    t_book_info bookInfo;
    @Autowired
    t_return treturn;
    public Map<String, Object> returns(TReturn tReturn) {
        Map<String, Object> result = new HashMap<>();
        try {
            TBorrow tBorrow = new TBorrow();
            tBorrow.setBookId("<value>'" + tReturn.getBookId() + "'");
            tBorrow.setUserId("<value>'" + tReturn.getUserId() + "'");
            tBorrow.setBorrowEndTime("<query>");
            List<TBorrow> tBorrow1 = borrow.search(0, -1, tBorrow);
            TBook tBook = new TBook();
            tBook.setBookId("<value>'" + tReturn.getBookId() + "'");
            tBook.setBorrowType("<query>");
            if (!book.search(0, -1, tBook).get(0).getBorrowType().equals("borrowed")) {
                result.put("state", "fail");
                return result;
            }
            int credit = 100;
            TUser tUser = new TUser();
            tUser.setUserId("<value>'" + tReturn.getUserId() + "'");

            if (Long.parseLong(tBorrow1.get(tBorrow1.size() - 1).getBorrowEndTime()) > Long.parseLong(tReturn.getReturnTime())) {
                tReturn.setReturnType("normal;" + tReturn.getReturnType());
                credit = 95;
            } else {
                tReturn.setReturnType("outtime;" + tReturn.getReturnType());
            }
            if (tReturn.getReturnType().contains("broken")) {
                credit -= 10;
            } else if (tReturn.getReturnType().contains("lose")) {
                credit -= 20;
            }
            tUser.setCredit("<set>'" + String.valueOf(credit) + "'");
            user.change(tUser);
            treturn.add(tReturn);

            tBook.setBorrowType("<set>'finish'");
            tBook.setBrokenType("<set>'" + tReturn.getReturnType().split(";")[1]+ "'");
            book.change(tBook);
            result.put("state", "success");
            return result;

        }
        catch(Exception e){

            result.put("state", "fail");
            return result;
        }
    }
    public List<Map<String, Object>> returnSearch(TReturn tReturn){

        List<Map<String, Object>> result=new ArrayList<>();

        Map<String, Object> eachrow=new HashMap<>();
        try {
            TBorrow tBorrow = new TBorrow();
            TReturn tReturn1 = new TReturn();
            if (!tReturn.getUserId().equals("<none>")) {
                tBorrow.setUserId("<value>'" + tReturn.getUserId() + "'");
                tReturn1.setUserId("<value>'" + tReturn.getUserId() + "'");
            }

            if (!(tReturn.getReturnType().contains("noreturn") || tReturn.getReturnType().contains("<none>"))) {
                tReturn1.setReturnType("<value>'" + tReturn.getReturnType() + "'");
            }

            List<TBorrow> borrowResult = borrow.search(0, -1, tBorrow);
            List<TReturn> returnResult = treturn.search(0, -1, tReturn1);
            int i = 0;
            int j = 0;
            for (i = 0; i < borrowResult.size(); i++) {
                eachrow.clear();
                for (j = 0; j < returnResult.size(); j++) {
                    if (returnResult.get(j).getBookId().equals(borrowResult.get(i).getBookId()) && returnResult.get(j).getUserId().equals(borrowResult.get(i).getUserId()) && Long.parseLong(returnResult.get(j).getReturnTime()) > Long.parseLong(borrowResult.get(i).getBorrowStartTime())) {
                        eachrow.put("userId", borrowResult.get(i).getUserId());
                        eachrow.put("bookId", borrowResult.get(i).getBookId());
                        eachrow.put("borrowStartTime", borrowResult.get(i).getBorrowStartTime());
                        eachrow.put("borrowEndTime", borrowResult.get(i).getBorrowEndTime());
                        eachrow.put("returnTime", returnResult.get(i).getReturnTime());
                        eachrow.put("returnType", returnResult.get(i).getReturnType());
                        break;
                    }
                }
                if (j == returnResult.size()) {
                    eachrow.put("userId", borrowResult.get(i).getUserId());
                    eachrow.put("bookId", borrowResult.get(i).getBookId());
                    eachrow.put("borrowStartTime", borrowResult.get(i).getBorrowStartTime());
                    eachrow.put("borrowEndTime", borrowResult.get(i).getBorrowEndTime());
                    eachrow.put("returnTime", "no");
                    if (System.currentTimeMillis() > Long.parseLong(borrowResult.get(i).getBorrowEndTime()))
                        eachrow.put("returnType", "outtime;noreturn");
                    else {
                        eachrow.put("returnType", "normal;noreturn");
                    }
                    if (tReturn.getReturnType().contains("outtime;noreturn") && eachrow.get("returnType").toString().contains("outtime;noreturn")) {
                        result.add(eachrow);
                    } else if (tReturn.getReturnType().contains("normal;noreturn") && eachrow.get("returnType").toString().contains("normal;noreturn")) {
                        result.add(eachrow);
                    } else if (tReturn.getReturnType().contains("<none>")) {
                        result.add(eachrow);
                    }
                } else {
                    result.add(eachrow);
                }

            }

            eachrow.put("state","success");
            result.add(eachrow);
            return result;
        }
        catch(Exception e){
            eachrow.put("state","fail");
            result.add(eachrow);
            return result;
        }

    }
}
