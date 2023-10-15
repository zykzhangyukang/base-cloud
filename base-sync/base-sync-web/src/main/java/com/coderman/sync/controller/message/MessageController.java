package com.coderman.sync.controller.message;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import com.coderman.sync.dto.MessagePageDTO;
import com.coderman.sync.message.MqMessageModel;
import com.coderman.sync.service.message.MessageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/${domain}/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @ApiOperation(value = "本地消息列表查询",httpMethod = SwaggerConstant.METHOD_GET)
    @PostMapping(value = "/page")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO",value = {"pageRow", "totalRow", "currPage", "totalPage", "dataList"}),
            @ApiReturnParam(name = "MqMessageModel",value = {"srcProject", "msgContent", "createTime", "destProject", "sendStatus", "dealStatus", "dealCount",
                    "sendTime", "ackTime", "mid","mqMessageId"})
    })
    public ResultVO<PageVO<List<MqMessageModel>>> selectMessagePage(@RequestBody MessagePageDTO messagePageDTO){

        return this.messageService.selectMessagePage(messagePageDTO);
    }
}
