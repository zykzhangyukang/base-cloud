package com.coderman.sync.jobhandler;

import com.coderman.sync.es.EsService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 清空同步记录冗余数据
 */
@JobHandler(value = "cleanResultHandler")
@Component
@Slf4j
public class CleanResultHandler extends IJobHandler {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private EsService esService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        // 保存近7天的同步记录
        Date ltTime = DateUtils.addDays(new Date(), -7);
        // 总共删除10000条数
        int deleteSize = 10000;

        int rowDbCount = 0;
        try {
            rowDbCount = this.jdbcTemplate.update("delete from sync_result where status = 'success' and msg_create_time < ?  limit ?", ltTime, deleteSize);
        } catch (Exception e) {
            log.error("删除db中的同步记录失败:{}", e.getMessage(), e);
        }

        int rowEsCount = 0;
        try {

            rowEsCount = this.esService.batchDeleteSyncResult(ltTime, deleteSize);

        } catch (Exception e) {
            log.error("删除es中的同步记录失败:{}", e.getMessage(), e);
        }

        XxlJobLogger.log("删除冗余同步记录 - db数据总数:{} , es冗余总数:{}  ", rowDbCount, rowEsCount);
        log.info("删除冗余同步记录 - db数据总数:{} , es冗余总数:{}  ", rowDbCount, rowEsCount);

        return ReturnT.SUCCESS;
    }

}
