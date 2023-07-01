package com.jiawa.wiki.service;

import com.jiawa.wiki.mapper.TestMapper;
import com.jiawa.wiki.pojo.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestMapper testMapper;

    public List<Test> list(){
        return testMapper.list();
    }
}
