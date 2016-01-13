package org.springframework.simulator.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by gman on 7/01/16.
 */
@Controller
public class MyController {

    @Autowired
    private MyService myService;

    @RequestMapping("/")
    ResponseEntity<String> something(@RequestParam("num") int num) throws Exception {
        myService.doSomething(num, new MyObject());
        Thread.sleep(500);
        myService.doSomething(num);
        Thread.sleep(500);
        return ResponseEntity.ok(myService.getSomething(new MyObject(num)));
    }

}
