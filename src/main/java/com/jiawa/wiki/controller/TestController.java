package com.jiawa.wiki.controller;

import com.jiawa.wiki.pojo.Test;
import com.jiawa.wiki.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    @Value("${test.hello:TEST}")//:后面是默认值
    private String testHello;

    @Autowired
    private TestService testService;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);


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

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key,@PathVariable String value){
        redisTemplate.opsForValue().set(key, value,3600, TimeUnit.SECONDS);
        LOG.info("key:{},value:{}", key,value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key){
        Object obj = redisTemplate.opsForValue().get(key);
        LOG.info("key:{},value:{}", key,obj);
        return obj;
    }

}
