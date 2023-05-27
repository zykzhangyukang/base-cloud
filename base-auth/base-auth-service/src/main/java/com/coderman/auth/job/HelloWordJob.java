package com.coderman.auth.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@JobHandler(value = "helloWordJob")
@Component
@Slf4j
public class HelloWordJob extends IJobHandler {

    @Override
    public ReturnT<String> execute(String param) {


        log.info("HelloWordJob running~~");

        return ReturnT.SUCCESS;
    }
}
