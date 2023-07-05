package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.mapper.CategoryMapper;
import com.jiawa.wiki.pojo.Category;
import com.jiawa.wiki.pojo.CategoryExample;
import com.jiawa.wiki.req.CategoryQueryReq;
import com.jiawa.wiki.req.CategorySaveReq;
import com.jiawa.wiki.resp.CategoryQueryResp;
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
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq categoryReq){

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if(!ObjectUtils.isEmpty(categoryReq.getName())){
            criteria.andNameLike("%"+ categoryReq.getName() +"%");
        }

        PageHelper.startPage(categoryReq.getPage(),categoryReq.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());

        PageResp<CategoryQueryResp> pageResp = new PageResp<>();

        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;

    }

    public List<CategoryQueryResp> all(){

        CategoryExample categoryExample = new CategoryExample();

        categoryExample.setOrderByClause("sort asc");

        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        return list;

    }

    public void save(CategorySaveReq categoryReq) {
        Category category =CopyUtil.copy(categoryReq, Category.class);
        if (ObjectUtils.isEmpty(category.getId())){
            //新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            //修改
            categoryMapper.updateByPrimaryKey(category);
        }
    }


    public void delete(Long id) {

        categoryMapper.deleteByPrimaryKey(id);
    }


}
