package com.coderman.sync.controller.result;

import com.coderman.api.vo.PageVO;
import com.coderman.erp.util.AuthUtil;
import com.coderman.swagger.annotation.ApiReturnIgnore;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import com.coderman.sync.dto.ResultPageDTO;
import com.coderman.sync.dto.ResultValidDataDTO;
import com.coderman.sync.result.ResultModel;
import com.coderman.sync.service.result.ResultService;
import com.coderman.sync.vo.CompareVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/${domain}/result")
public class ResultController {

    @Resource
    private ResultService resultService;

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "同步记录查询")
    @PostMapping(value = "/page")
    @ApiReturnIgnore
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO", value = {"pageRow", "totalRow", "currPage", "totalPage", "dataList"}),
            @ApiReturnParam(name = "ResultModel", value = {"syncToEs", "msgSrc", "syncTime", "errorMsg", "uuid", "planCode", "msgId", "planName", "mqId", "planUuid", "status", "repeatCount", "msgCreateTime",
                    "msgContent", "destProject", "srcProject", "syncContent", "remark"})
    })
    public com.coderman.api.vo.ResultVO<PageVO<List<ResultModel>>> page(@RequestBody ResultPageDTO resultPageDTO) throws Exception {
        return this.resultService.page(resultPageDTO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT, value = "重新同步")
    @PutMapping(value = "/repeat/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", paramType = SwaggerConstant.PARAM_QUERY, dataType = SwaggerConstant.DATA_STRING, value = "uuid", required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
    })
    public com.coderman.api.vo.ResultVO<Void> repeatSync(String uuid) {

        return this.resultService.repeatSync(uuid);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT, value = "手动标记成功")
    @PutMapping(value = "/sign/success")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", paramType = SwaggerConstant.PARAM_QUERY, dataType = SwaggerConstant.DATA_STRING, value = "uuid", required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
    })
    public com.coderman.api.vo.ResultVO<Void> signSuccess(String uuid) throws IOException {

        return this.resultService.signSuccess(uuid, AuthUtil.getCurrent().getRealName() + "手动标记成功");
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "校验同步结果")
    @PostMapping(value = "/valid/data")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "CompareVO", value = {"srcUnique", "destColumnList", "srcResultList", "destResultList", "srcColumnList", "destUnique", "destTable", "srcTable"}),
    })
    public com.coderman.api.vo.ResultVO<List<CompareVO>> validResultData(@RequestBody ResultValidDataDTO resultValidDataDTO) throws Throwable {

        String msgContent = resultValidDataDTO.getMsgContent();
        Assert.notNull(msgContent, "msgContent is null");

        return this.resultService.selectTableData(msgContent, true);
    }

}
