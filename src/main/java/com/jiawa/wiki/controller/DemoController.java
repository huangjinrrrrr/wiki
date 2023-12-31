package com.jiawa.wiki.controller;

import com.jiawa.wiki.pojo.Demo;
import com.jiawa.wiki.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;


    @GetMapping("/list")
    public List<Demo> DemoList(){
        List<Demo> list = demoService.list();
        return list;
    }

}
