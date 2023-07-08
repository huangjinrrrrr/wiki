package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.mapper.ContentMapper;
import com.jiawa.wiki.mapper.DocMapper;
import com.jiawa.wiki.pojo.Content;
import com.jiawa.wiki.pojo.Doc;
import com.jiawa.wiki.pojo.DocExample;
import com.jiawa.wiki.req.DocQueryReq;
import com.jiawa.wiki.req.DocSaveReq;
import com.jiawa.wiki.resp.DocQueryResp;
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
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<DocQueryResp> list(DocQueryReq docReq){

        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        if(!ObjectUtils.isEmpty(docReq.getName())){
            criteria.andNameLike("%"+ docReq.getName() +"%");
        }

        PageHelper.startPage(docReq.getPage(),docReq.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());

        PageResp<DocQueryResp> pageResp = new PageResp<>();

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;

    }

    public List<DocQueryResp> all(){

        DocExample docExample = new DocExample();

        docExample.setOrderByClause("sort asc");

        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;

    }

    public void save(DocSaveReq docReq) {//DocSaveReq 对应两张表doc和content
        Doc doc = CopyUtil.copy(docReq, Doc.class);
        Content content = CopyUtil.copy(docReq, Content.class);

        if (ObjectUtils.isEmpty(doc.getId())){
            //新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //修改
            docMapper.updateByPrimaryKey(doc);

            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0){
                contentMapper.insert(content);
            }
        }
    }


    public void delete(Long id) {

        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);

        docMapper.deleteByExample(docExample);
    }


}
