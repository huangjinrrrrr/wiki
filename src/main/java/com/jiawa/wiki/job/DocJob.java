package com.jiawa.wiki.job;

import com.jiawa.wiki.service.DocService;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private DocService docService;

    /**
     * 自定义cron表达式跑批
     * 每30秒更新电子书信息
     */
    @Scheduled(cron = "1/30 * * * * ? ")
    public void cron() {
        // 增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));

        LOG.info("更新电子书下的文档信息开始");
        long startTime = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("更新电子书下的文档信息结束，共耗时:{}毫秒",System.currentTimeMillis() - startTime);
    }


}
