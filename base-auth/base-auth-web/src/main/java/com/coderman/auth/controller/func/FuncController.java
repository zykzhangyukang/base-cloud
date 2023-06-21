package com.coderman.auth.controller.func;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dto.func.FuncPageDTO;
import com.coderman.auth.dto.func.FuncSaveDTO;
import com.coderman.auth.dto.func.FuncUpdateDTO;
import com.coderman.auth.dto.func.FuncUpdateRescBindDTO;
import com.coderman.auth.service.func.FuncService;
import com.coderman.auth.vo.func.FuncTreeVO;
import com.coderman.auth.vo.func.FuncVO;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author coderman
 * @Title: 功能
 * @date 2022/3/1915:34
 */
@Api(value = "功能管理", tags = "功能管理")
@RestController
@RequestMapping(value = "/${domain}/func")
public class FuncController {

    @Resource
    private FuncService funcService;

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "获取功能树")
    @GetMapping(value = "/tree")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "FuncVO",value = {"funcTreeVOList","funcVOList"}),
            @ApiReturnParam(name = "FuncTreeVO",value = {"funcName", "funcKey", "createTime", "updateTime", "childrenList", "funcId", "parentId"})
    })
    public ResultVO<List<FuncTreeVO>> listTree(){

        return this.funcService.listTree();
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "功能解绑用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "funcId",paramType = SwaggerConstant.PARAM_PATH,dataType = SwaggerConstant.DATA_INT,value = "功能id",required = true)
    })
    @GetMapping(value = "/delete/user/bind")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> deleteUserBind(@RequestParam(value = "funcId") Integer funcId){

        return this.funcService.deleteUserBind(funcId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT,value = "功能解绑资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "funcId",paramType = SwaggerConstant.PARAM_PATH,dataType = SwaggerConstant.DATA_INT,value = "功能id",required = true)
    })
    @PutMapping(value = "/delete/{funcId}/resc/bind")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> deleteResourceBind(@PathVariable(value = "funcId") Integer funcId){

        return this.funcService.deleteResourceBind(funcId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT,value = "功能设置资源")
    @PutMapping(value = "/update/resc/bind")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> updateResourceBind(@RequestBody FuncUpdateRescBindDTO rescBindDTO){

        return this.funcService.updateResourceBind(rescBindDTO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST,value = "功能列表")
    @PostMapping(value = "/page")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO",value = {"dataList",  "pageRow", "totalRow", "currPage", "totalPage"}),
            @ApiReturnParam(name = "FuncVO",value = {"funcSort","funcDirStatus","userVOList","rescVOList","funcName", "funcKey", "createTime","funcType","funcIcon",
                    "updateTime", "childrenList", "funcId", "parentId","rescIdList"})
    })
    public ResultVO<PageVO<List<FuncVO>>> page(@RequestBody FuncPageDTO funcPageDTO){

        return this.funcService.page(funcPageDTO);
    }


    @PostMapping(value = "/save")
    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST,value = "保存功能")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> save(@RequestBody FuncSaveDTO funcSaveDTO) {
        return this.funcService.save(funcSaveDTO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT,value = "更新功能")
    @PutMapping(value = "/update")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> update(@RequestBody FuncUpdateDTO funcUpdateDTO) {
        return this.funcService.update(funcUpdateDTO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_DELETE,value = "删除资源")
    @DeleteMapping(value = "/delete/{funcId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "funcId",paramType = SwaggerConstant.PARAM_PATH,dataType = SwaggerConstant.DATA_INT,value = "功能id",required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> delete(@PathVariable(value = "funcId") Integer funcId) {
        return this.funcService.delete(funcId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "获取资源")
    @GetMapping(value = "/{funcId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "funcId",paramType = SwaggerConstant.PARAM_PATH,dataType = SwaggerConstant.DATA_INT,value = "资源Id",required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "FuncVO",value = {"funcSort","funcDirStatus","userVOList","rescVOList","rescIdList",
                    "funcName","funcIcon", "funcKey", "createTime", "updateTime", "childrenList", "funcId", "parentId","funcType"})
    })
    public ResultVO<FuncVO> selectById(@PathVariable(value = "funcId") Integer funcId) {
        return this.funcService.selectById(funcId);
    }




}
