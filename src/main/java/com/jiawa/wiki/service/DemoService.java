package com.jiawa.wiki.service;

import com.jiawa.wiki.mapper.DemoMapper;
import com.jiawa.wiki.pojo.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {

    @Autowired
    private DemoMapper demoMapper;

    public List<Demo> list(){
        return demoMapper.selectByExample(null);
    }
}
