package com.coderman.auth.controller.resc;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dto.resc.RescPageDTO;
import com.coderman.auth.dto.resc.RescSaveDTO;
import com.coderman.auth.dto.resc.RescUpdateDTO;
import com.coderman.auth.service.resc.RescService;
import com.coderman.auth.vo.resc.RescVO;
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
 * @Title: 资源
 * @Description: TOD
 * @date 2022/3/199:02
 */
@Api(value = "资源管理", tags = "资源管理")
@RestController
@RequestMapping(value = "/${domain}/resc")
public class RescController {

    @Resource
    private RescService rescService;


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "资源列表")
    @PostMapping(value = "/page")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO", value = {"dataList", "pageRow", "totalRow", "currPage", "totalPage"}),
            @ApiReturnParam(name = "RescVO", value = {"rescId","rescName", "rescUrl", "rescDomain", "createTime", "updateTime", "methodType"})
    })
    public ResultVO<PageVO<List<RescVO>>> page(@RequestBody RescPageDTO rescPageDTO) {
        return this.rescService.page(rescPageDTO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "新增资源")
    @PostMapping(value = "/save")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> save(@RequestBody RescSaveDTO rescSaveDTO) {
        return this.rescService.save(rescSaveDTO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT, value = "更新资源")
    @PutMapping(value = "/update")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> update(@RequestBody RescUpdateDTO rescUpdateDTO) {
        return this.rescService.update(rescUpdateDTO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "搜索资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "关键字", required = false)
    })
    @GetMapping(value = "/search")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "RescVO", value = {"rescName", "rescUrl", "rescDomain", "createTime", "updateTime", "methodType"})
    })
    public ResultVO<List<RescVO>> search(@RequestParam(value = "keyword") String keyword) {

        return this.rescService.search(keyword);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_DELETE, value = "删除资源")
    @DeleteMapping(value = "/delete/{rescId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rescId", paramType = SwaggerConstant.PARAM_PATH, dataType = SwaggerConstant.DATA_INT, value = "资源Id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> delete(@PathVariable(value = "rescId") Integer rescId) {
        return this.rescService.delete(rescId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "获取资源")
    @GetMapping(value = "/{rescId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rescId", paramType = SwaggerConstant.PARAM_PATH, dataType = SwaggerConstant.DATA_INT, value = "资源Id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "RescVO", value = {"rescUrl", "rescName", "rescId", "rescDomain", "createTime", "updateTime", "methodType"})
    })
    public ResultVO<RescVO> select(@PathVariable(value = "rescId") Integer rescId) {
        return this.rescService.selectById(rescId);
    }


}
