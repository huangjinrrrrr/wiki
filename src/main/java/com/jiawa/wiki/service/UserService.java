package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.exception.BusinessException;
import com.jiawa.wiki.exception.BusinessExceptionCode;
import com.jiawa.wiki.mapper.UserMapper;
import com.jiawa.wiki.pojo.User;
import com.jiawa.wiki.pojo.UserExample;
import com.jiawa.wiki.req.UserLoginReq;
import com.jiawa.wiki.req.UserQueryReq;
import com.jiawa.wiki.req.UserRestPasswordReq;
import com.jiawa.wiki.req.UserSaveReq;
import com.jiawa.wiki.resp.UserLoginResp;
import com.jiawa.wiki.resp.UserQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.util.CopyUtil;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
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
            User userDB = selectByLoginName(userReq.getLoginName());//判断用户名是否存在
            if(ObjectUtils.isEmpty(userDB)){
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            } else {
                //用户名已存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }

        } else {
            //修改
            user.setLoginName(null);//Selective，user中有的值才会更新，防止LoginName被更改
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    //修改密码
    public void resetPassword(UserRestPasswordReq userRestPasswordReq) {
        User user =CopyUtil.copy(userRestPasswordReq, User.class);

        userMapper.updateByPrimaryKeySelective(user);

    }


    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }


    //判断用户名是否存在
    public User selectByLoginName(String LoginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);

        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)){
            return null;
        } else {
            return users.get(0);
        }

    }

    public UserLoginResp login(UserLoginReq userLoginReq) {
        User userDB = selectByLoginName(userLoginReq.getLoginName());
        if (userDB == null){
            LOG.info("用户名不存在,{}", userLoginReq.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);

        } else {
            if (userDB.getPassword().equals(userLoginReq.getPassword())){
                //登录成功
                UserLoginResp userLoginResp = CopyUtil.copy(userDB, UserLoginResp.class);
                return userLoginResp;
            } else {
                LOG.info("密码不对，输入的密码：{},数据库密码：{}", userLoginReq.getPassword(),userDB.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }

        }
    }
}
