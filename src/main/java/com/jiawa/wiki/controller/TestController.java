package com.jiawa.wiki.controller;

import com.jiawa.wiki.pojo.Test;
import com.jiawa.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Value("${test.hello:TEST}")//:后面是默认值
    private String testHello;

    @Autowired
    private TestService testService;


    @GetMapping("/hello")
    public String hello(){
        return "Hello World "+testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "Hello World Post "+name;
    }

    @GetMapping("/test/list")
    public List<Test> testList(){
        List<Test> list = testService.list();
        return list;
    }

}
