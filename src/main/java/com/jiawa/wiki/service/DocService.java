package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.exception.BusinessException;
import com.jiawa.wiki.exception.BusinessExceptionCode;
import com.jiawa.wiki.mapper.ContentMapper;
import com.jiawa.wiki.mapper.DocMapper;
import com.jiawa.wiki.mapper.DocMapperCust;
import com.jiawa.wiki.pojo.Content;
import com.jiawa.wiki.pojo.Doc;
import com.jiawa.wiki.pojo.DocExample;
import com.jiawa.wiki.req.DocQueryReq;
import com.jiawa.wiki.req.DocSaveReq;
import com.jiawa.wiki.resp.DocQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.util.CopyUtil;
import com.jiawa.wiki.util.RedisUtil;
import com.jiawa.wiki.util.RequestContext;
import com.jiawa.wiki.util.SnowFlake;
import com.jiawa.wiki.websocket.WebSocketServer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private DocMapperCust docMapperCust;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WsService wsService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


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

    public List<DocQueryResp> all(Long ebookId){

        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");

        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;

    }

    @Transactional
    public void save(DocSaveReq docReq) {//DocSaveReq 对应两张表doc和content
        Doc doc = CopyUtil.copy(docReq, Doc.class);
        Content content = CopyUtil.copy(docReq, Content.class);

        if (ObjectUtils.isEmpty(doc.getId())){
            //新增
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
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


    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);

        //文档阅读数+1
        docMapperCust.increaseViewCount(id);

        if (ObjectUtils.isEmpty(content)){
            return "";
        } else {
            return content.getContent();
        }
    }


    public void vote(Long id) {
//        文档点赞数+1
//        docMapperCust.increaseVoteCount(id);

        String ip = RequestContext.getRemoteAddr();
//        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 24)){
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 5)){
            docMapperCust.increaseVoteCount(id);
        } else {//点过赞了
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        // 推送消息
        Doc docDB = docMapper.selectByPrimaryKey(id);
        String logId = MDC.get("LOG_ID");
//        wsService.sendInfo("【" + docDB.getName() + "】被点赞！",logId);
        rocketMQTemplate.convertAndSend("VOTE_TOPIC","【" + docDB.getName() + "】被点赞！");
    }

    public void updateEbookInfo(){
        docMapperCust.updateEbookInfo();
    }


}
