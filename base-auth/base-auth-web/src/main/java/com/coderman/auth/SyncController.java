package com.coderman.auth;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import com.coderman.sync.util.MsgBuilder;
import com.coderman.sync.util.ProjectEnum;
import com.coderman.sync.util.SyncUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping(value = "/${domain}/sync")
public class SyncController {

    @GetMapping(value = "/send")
    public ResultVO<Void> send(){

        SyncUtil.sync(
                MsgBuilder.create("insert_demo_market_user", ProjectEnum.DEMO, ProjectEnum.MARKET)
                        .addIntList("insert_demo_market_user", Collections.singletonList(1))
                        .build()
        );

        return ResultUtil.getSuccess();
    }
}
