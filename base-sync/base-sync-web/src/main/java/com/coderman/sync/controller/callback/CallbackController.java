package com.coderman.sync.controller.callback;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.sync.callback.CallbackModel;
import com.coderman.sync.dto.CallbackPageDTO;
import com.coderman.sync.service.callback.CallbackService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/${domain}/callback")
public class CallbackController {

    @Resource
    private CallbackService callbackService;

    @RequestMapping(value = "/page")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO",value = {"pageRow", "totalRow", "currPage", "totalPage", "dataList"}),
            @ApiReturnParam(name = "CallbackModel",value = {"srcProject", "msgContent", "createTime", "destProject", "sendStatus", "dealStatus", "dealCount",
                    "sendTime", "ackTime", "mid","remark", "uuid", "status", "repeatCount","msgId"})
    })
    public ResultVO<PageVO<List<CallbackModel>>> selectCallbackPage(@RequestBody CallbackPageDTO callbackPageDTO){

        return this.callbackService.selectCallbackPage(callbackPageDTO);
    }

}
