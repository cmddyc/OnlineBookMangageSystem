package book_sys.service;

import book_sys.dao.interf.t_book;
import book_sys.dao.interf.t_book_info;
import book_sys.entity.TBook;
import book_sys.entity.TBookInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


@Service
public class book_search_service {
//    public int start;
//    public int length=20;

    @Autowired(required = false)
    t_book book;
    @Autowired(required = false)
    t_book_info book_info;

    public List<Map<String, Object>> bookSearch(TBook tBook, TBookInfo tBookInfo, int type){
        List<TBook> dataResult=new ArrayList<>();
        List<TBookInfo> dataResult2=new ArrayList<>();
        List<Map<String, Object>> result=new ArrayList<>();
        int i=0;
        Map<String, Object> endEachrow=new HashMap<>();

        try {
            dataResult = book.search(0, -1, tBook);
            dataResult2 = book_info.search(0, -1, 1, tBookInfo);
            int u = 0;
            for (u = 0; u < dataResult2.size(); u++) {
                Map<String, Object> eachrow=new HashMap<>();
                List<TBook> eachBook = new ArrayList<>();
                for (i = 0; i < dataResult.size(); i++) {
                    if (dataResult2.get(u).getIsbn().equals(dataResult.get(i).getIsbn())) {
                        if ((type == 0 && dataResult.get(u).getBrokenType().equals("normal")) || type == 1) {
                            eachBook.add(dataResult.get(i));
                        }
                    }
                }
                if (eachBook.size() > 0) {
//                    String path = dataResult2.get(u).getBookImg();
//                    FileInputStream stream = new FileInputStream(path);
//                    int len= stream.available();
//                    byte[] bytes = new byte[len];
//                    stream.read(bytes);
//                    stream.close();
//                    Base64.Encoder encoder = Base64.getEncoder();
//                    String file = encoder.encodeToString(bytes);
                    String img=Files.readString(Paths.get(dataResult2.get(u).getBookImg()));


                    dataResult2.get(u).setBookImg(img);
                    eachrow.put("tBookInfo", dataResult2.get(u));
                    eachrow.put("tBook", eachBook);
                    result.add(eachrow);
                }
            }
            endEachrow.put("state", "success");
            result.add(endEachrow);
        }
        catch(Exception e){
            endEachrow.put("state","fail");
            result.add(endEachrow);

        }
        return result;
    }

}
