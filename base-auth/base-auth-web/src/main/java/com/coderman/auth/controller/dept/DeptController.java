package com.coderman.auth.controller.dept;

import com.coderman.api.vo.ResultVO;
import com.coderman.auth.model.dept.DeptModel;
import com.coderman.auth.service.dept.DeptService;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author coderman
 * @date 2022/3/1216:12
 */
@Api(value = "部门管理", tags = "部门管理")
@RestController
@RequestMapping(value = "/${domain}/dept")
public class DeptController {

    @Resource
    private DeptService deptService;

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "查询部门列表")
    @GetMapping(value = "/list")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "DeptModel",value = {"deptId", "deptCode", "deptName", "createTime","updateTime"})
    })
    public ResultVO<List<DeptModel>> list() {
        return this.deptService.list();
    }

}
