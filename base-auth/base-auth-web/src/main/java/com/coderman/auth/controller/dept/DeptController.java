package com.coderman.auth.controller.dept;

import com.coderman.api.vo.ResultVO;
import com.coderman.auth.model.dept.DeptModel;
import com.coderman.auth.service.dept.DeptService;
import io.swagger.annotations.Api;
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

    @GetMapping(value = "/list")
    public ResultVO<List<DeptModel>> list() {
        return this.deptService.list();
    }

}
