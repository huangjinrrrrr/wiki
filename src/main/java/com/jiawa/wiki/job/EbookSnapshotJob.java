package com.jiawa.wiki.job;

import com.jiawa.wiki.service.EbookSnapshotService;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EbookSnapshotJob {

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private EbookSnapshotService ebookSnapshotService;

    /**
     * 自定义cron表达式跑批
     * 每30秒更新电子书信息
     */
    @Scheduled(cron = "0/5 * * * * ? ")
    public void cron() {
        // 增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));

        LOG.info("生成今日电子书快照开始");
        Long startTime = System.currentTimeMillis();
        ebookSnapshotService.genSnapshot();
        LOG.info("生成今日电子书快照结束，共耗时:{}毫秒",System.currentTimeMillis() - startTime);
    }


}
