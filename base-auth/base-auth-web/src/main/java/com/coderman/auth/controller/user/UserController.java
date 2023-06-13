package com.coderman.auth.controller.user;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dto.user.UserLoginDTO;
import com.coderman.auth.dto.user.UserPageDTO;
import com.coderman.auth.dto.user.UserSaveDTO;
import com.coderman.auth.service.user.UserService;
import com.coderman.auth.vo.user.*;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author coderman
 * @Title: 用户模块
 * @date 2022/2/2711:37
 */
@Api(value = "用户管理", tags = "用户管理")
@RestController
@RequestMapping(value = "/${domain}/user")
public class UserController {

    @Resource
    private UserService userService;


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "用户登录")
    @PostMapping(value = "/login")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserLoginRespVO",value = {"realName", "deptCode", "username", "token","deptName"})
    })
    public ResultVO<UserLoginRespVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        return this.userService.login(userLoginDTO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "刷新会话")
    @PostMapping(value = "/refresh/login")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "AuthUserVO",value = {"realName", "deptCode", "username", "token"})
    })
    public ResultVO<String> refreshLogin(@RequestHeader(value = CommonConstant.USER_TOKEN_NAME) String token) {
        return this.userService.refreshLogin(token);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "注销登入")
    @PostMapping(value = "/logout")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "AuthUserVO",value = {"realName", "deptCode", "username", "token"})
    })
    public ResultVO<Void> logout(@RequestHeader(value = CommonConstant.USER_TOKEN_NAME,required = false) String token) {
        return this.userService.logout(token);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST,value = "获取用户信息")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserPermissionVO", value = {"realName", "deptCode","deptName", "username", "token", "userId","roles","funcKeys","menus"}),
    })
    @PostMapping(value = "/info")
    public ResultVO<UserPermissionVO> info(@RequestHeader(value = CommonConstant.USER_TOKEN_NAME,required = false) String token){
        return this.userService.info(token);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "获取用户拥有的角色名称")
    @GetMapping(value = "/select/role/names")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<List<String>> selectRoleNames(@RequestParam(value = "userId") Integer userId) {
        return this.userService.selectRoleNames(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "用户分配初始化")
    @GetMapping(value = "/role/update/init")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserAssignVO", value = {"assignedIdList", "roleList", "userId"})
    })
    public ResultVO<UserAssignVO> selectAssignInit(@RequestParam(value = "userId") Integer userId) {
        return this.userService.selectAssignInit(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "用户角色分配")
    @PostMapping(value = "/role/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true),
            @ApiImplicitParam(name = "assignedIdList", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_OBJECT, value = "分配的角色id集合", required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> updateAssign(@RequestParam(value = "userId") Integer userId,
                                       @RequestParam(value = "assignedIdList") List<Integer> assignedIdList) {
        return this.userService.updateAssign(userId, assignedIdList);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "获取用户信息")
    @GetMapping(value = "/select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserVO", value = {"createTime", "updateTime", "username", "realName", "userStatus", "deptCode", "userId"})
    })
    public ResultVO<UserVO> select(@RequestParam(value = "userId") Integer userId) {
        return this.userService.select(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "查询用户列表")
    @PostMapping(value = "/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_INT, value = "当前页码", required = true, defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_INT, value = "每页显示条数", required = true, defaultValue = "20"),
            @ApiImplicitParam(name = "username", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_STRING, value = "用户名"),
            @ApiImplicitParam(name = "realName", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_STRING, value = "真实姓名"),
            @ApiImplicitParam(name = "deptCode", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_STRING, value = "部门编号"),
            @ApiImplicitParam(name = "userStatus", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_STRING, value = "用户状态"),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO", value = {"dataList", "pageRow", "totalRow", "currPage", "totalPage"}),
            @ApiReturnParam(name = "UserVO", value = {"realName", "deptName", "password", "userStatus", "createTime",
                    "updateTime", "userId", "deptCode", "username"})
    })
    public ResultVO<PageVO<List<UserVO>>> page(@RequestBody UserPageDTO queryVO) {
        return this.userService.page(queryVO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "更新密码")
    @PostMapping(value = "/update/password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true),
            @ApiImplicitParam(name = "password", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "密码", required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
    })
    public ResultVO<Void> updatePassword(Integer userId, String password) {

        return this.userService.updatePassword(userId, password);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "启用用户")
    @GetMapping(value = "/update/enable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> updateEnable(@RequestParam(value = "userId") Integer userId) {
        return this.userService.updateEnable(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "禁用用户")
    @GetMapping(value = "/update/disable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> disable(@RequestParam(value = "userId") Integer userId) {
        return this.userService.updateDisable(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "新增用户")
    @PostMapping(value = "/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_STRING, value = "用户账号", required = true),
            @ApiImplicitParam(name = "password", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_STRING, value = "密码", required = true),
            @ApiImplicitParam(name = "realName", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_STRING, value = "用户姓名", required = true),
            @ApiImplicitParam(name = "userStatus", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_INT, value = "用户状态", required = true),
            @ApiImplicitParam(name = "deptCode", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_STRING, value = "部门编号")
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> save(@RequestBody UserSaveDTO userSaveDTO) {
        return this.userService.save(userSaveDTO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_DELETE, value = "删除用户")
    @DeleteMapping(value = "/delete/{userId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_PATH, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> delete(@PathVariable(value = "userId") Integer userId) {
        return this.userService.delete(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "更新用户")
    @PostMapping(value = "/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "用户账号", required = true),
            @ApiImplicitParam(name = "realName", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "用户姓名", required = true),
            @ApiImplicitParam(name = "userStatus", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户状态", required = true),
            @ApiImplicitParam(name = "deptCode", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "部门编号", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> update(@ApiIgnore UserVO userVO) {
        return this.userService.update(userVO);
    }


}
