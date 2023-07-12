package com.jiawa.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiawa.wiki.req.UserLoginReq;
import com.jiawa.wiki.req.UserQueryReq;
import com.jiawa.wiki.req.UserRestPasswordReq;
import com.jiawa.wiki.req.UserSaveReq;
import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.UserLoginResp;
import com.jiawa.wiki.resp.UserQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.service.UserService;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    @GetMapping("/list")
    public CommonResp UserList(@Valid UserQueryReq userReq){
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> pageResp = userService.list(userReq);
        resp.setContent(pageResp);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq userReq){
        CommonResp resp = new CommonResp<>();
        userReq.setPassword(DigestUtils.md5DigestAsHex(userReq.getPassword().getBytes()));
        userService.save(userReq);
        return resp;
    }

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserRestPasswordReq userRestPasswordReq){
        CommonResp resp = new CommonResp<>();
        userRestPasswordReq.setPassword(DigestUtils.md5DigestAsHex(userRestPasswordReq.getPassword().getBytes()));
        userService.resetPassword(userRestPasswordReq);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp resp= new CommonResp();
        userService.delete(id);
        return resp;
    }

    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq userLoginReq){
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        userLoginReq.setPassword(DigestUtils.md5DigestAsHex(userLoginReq.getPassword().getBytes()));
        UserLoginResp userLoginResp = userService.login(userLoginReq);

        //生成单点登录token，并放入Redis中
        Long token = snowFlake.nextId();
        LOG.info("生成单点登录token: {}，并放入Redis中",token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userLoginResp), 3600*24, TimeUnit.SECONDS);//token作为key userLoginResp作为value

        resp.setContent(userLoginResp);
        return resp;
    }


    @GetMapping("/logout/{token}")
    public CommonResp delete(@PathVariable String token){
        CommonResp resp= new CommonResp<>();
        redisTemplate.delete(token);
        LOG.info("从Redis中清除token: {}",token);
        return resp;
    }

}
