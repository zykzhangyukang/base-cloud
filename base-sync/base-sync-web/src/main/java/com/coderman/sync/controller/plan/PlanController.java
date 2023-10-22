package com.coderman.sync.controller.plan;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import com.coderman.sync.dto.PlanPageDTO;
import com.coderman.sync.dto.PlanSaveDTO;
import com.coderman.sync.dto.PlanUpdateDTO;
import com.coderman.sync.dto.PlanUpdateStatusDTO;
import com.coderman.sync.service.plan.PlanService;
import com.coderman.sync.vo.PlanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "同步计划", tags = "同步计划")
@RestController
@RequestMapping(value = "/${domain}/plan")
public class PlanController {

    @Resource
    private PlanService planService;

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT, value = "同步计划更新")
    @PutMapping(value = "/update")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"result", "code", "msg"})
    })
    public ResultVO<Void> update(@RequestBody PlanUpdateDTO planUpdateDTO) {

        return this.planService.update(planUpdateDTO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "同步计划添加")
    @PostMapping(value = "/save")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"result", "code", "msg"})
    })
    public ResultVO<Void> save(@RequestBody PlanSaveDTO planSaveDTO) {

        return this.planService.save(planSaveDTO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "同步计划列表")
    @PostMapping(value = "/page")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"result", "code","msg"}),
            @ApiReturnParam(name = "PageVO", value = {"pageRow", "totalPage", "currPage", "totalRow", "dataList"}),
            @ApiReturnParam(name = "PlanVO", value = {"srcProject", "destDb", "destProject", "planContent", "updateTime", "createTime", "srcDb", "uuid", "planCode", "status","description"}),
    })
    public ResultVO<PageVO<List<PlanVO>>> page(@RequestBody PlanPageDTO planPageDTO) {

        return this.planService.page(planPageDTO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "根据uuid获取同步计划信息")
    @GetMapping(value = "/detail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", paramType = SwaggerConstant.PARAM_QUERY, dataType = SwaggerConstant.DATA_STRING, value = "uuid", required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PlanVO", value = {"uuid","planCode","description","srcDb","destDb","srcProject","destProject","createTime","updateTime" , "status", "planContent"})
    })
    public ResultVO<PlanVO> selectPlanByUuid(String uuid) {

        return this.planService.selectPlanByUuid(uuid);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_DELETE, value = "根据uuid删除同步计划信息")
    @DeleteMapping(value = "/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", paramType = SwaggerConstant.PARAM_QUERY, dataType = SwaggerConstant.DATA_STRING, value = "uuid", required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"result", "code", "msg"})
    })
    public ResultVO<Void> delete(String uuid) {

        return this.planService.delete(uuid);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT, value = "根据uuid更新状态")
    @PutMapping(value = "/update/status")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"result", "code", "msg"})
    })
    public ResultVO<Void> updateStatus(@RequestBody PlanUpdateStatusDTO planUpdateStatusDTO) {
        return this.planService.updateStatus(planUpdateStatusDTO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT, value = "刷新同步计划")
    @PutMapping(value = "/refresh")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
    })
    public ResultVO<Void> refreshSyncPlan() {
        return this.planService.refreshSyncPlan();
    }

}
