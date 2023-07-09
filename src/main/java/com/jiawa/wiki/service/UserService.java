package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.mapper.UserMapper;
import com.jiawa.wiki.pojo.User;
import com.jiawa.wiki.pojo.UserExample;
import com.jiawa.wiki.req.UserQueryReq;
import com.jiawa.wiki.req.UserSaveReq;
import com.jiawa.wiki.resp.UserQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.util.CopyUtil;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq userReq){

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if(!ObjectUtils.isEmpty(userReq.getLoginName())){
            criteria.andLoginNameLike("%"+ userReq.getLoginName() +"%");
        }

        PageHelper.startPage(userReq.getPage(),userReq.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());


        PageResp<UserQueryResp> pageResp = new PageResp<>();

        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);

        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;

    }

    public void save(UserSaveReq userReq) {
        User user =CopyUtil.copy(userReq, User.class);
        if (ObjectUtils.isEmpty(user.getId())){
            //新增
            user.setId(snowFlake.nextId());
            userMapper.insert(user);
        } else {
            //修改
            userMapper.updateByPrimaryKey(user);
        }
    }


    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }
}
