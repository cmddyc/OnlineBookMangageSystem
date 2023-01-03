package book_sys.hello;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import book_sys.dao.interf.*;

@RestController
@RequestMapping("/test")
public class HelloWorld{



    @Autowired
    t_book admin;
    @GetMapping("hello")
    public int hello(){

        return 0;
    }
}
