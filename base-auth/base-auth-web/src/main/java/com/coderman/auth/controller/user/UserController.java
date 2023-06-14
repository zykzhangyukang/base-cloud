package com.coderman.auth.controller.user;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dto.user.*;
import com.coderman.auth.service.user.UserService;
import com.coderman.auth.vo.user.UserAssignVO;
import com.coderman.auth.vo.user.UserLoginRespVO;
import com.coderman.auth.vo.user.UserPermissionVO;
import com.coderman.auth.vo.user.UserVO;
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

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "注销登录")
    @PostMapping(value = "/logout")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = SwaggerConstant.PARAM_HEADER, dataType = SwaggerConstant.DATA_STRING, value = "用户token")
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "AuthUserVO",value = {"realName", "deptCode", "username", "token"})
    })
    public ResultVO<Void> logout(@RequestHeader(value = CommonConstant.USER_TOKEN_NAME,required = false) String token) {
        return this.userService.logout(token);
    }

    @PostMapping(value = "/info")
    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST,value = "获取用户菜单与权限")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserPermissionVO", value = {"realName", "deptCode","deptName", "username", "token", "userId","roles","funcKeys","menus"}),
    })
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
    @GetMapping(value = "/{userId}/assign/init")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserAssignVO", value = {"assignedIdList", "roleList", "userId"})
    })
    public ResultVO<UserAssignVO> selectAssignInit(@PathVariable(value = "userId") Integer userId) {
        return this.userService.selectAssignInit(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT, value = "用户分配角色")
    @PutMapping(value = "/assign/update")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> updateAssign(@RequestBody UserAssignDTO userAssignDTO) {
        return this.userService.updateAssign(userAssignDTO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "根据ID获取用户信息")
    @GetMapping(value = "/{userId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_PATH, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserVO", value = {"createTime", "updateTime", "username", "realName", "userStatus", "deptCode", "userId"})
    })
    public ResultVO<UserVO> selectUserById(@PathVariable(value = "userId") Integer userId) {
        return this.userService.selectUserById(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "查询用户列表")
    @PostMapping(value = "/page")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO", value = {"dataList", "pageRow", "totalRow", "currPage", "totalPage"}),
            @ApiReturnParam(name = "UserVO", value = {"realName", "deptName", "password", "userStatus", "createTime",
                    "updateTime", "userId", "deptCode", "username"})
    })
    public ResultVO<PageVO<List<UserVO>>> page(@RequestBody UserPageDTO queryVO) {
        return this.userService.page(queryVO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT, value = "更新密码")
    @PutMapping(value = "/update/password")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
    })
    public ResultVO<Void> updatePassword(@RequestBody UserUpdatePwdDTO userUpdatePwdDTO) {

        return this.userService.updatePassword(userUpdatePwdDTO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT, value = "启用用户")
    @PutMapping(value = "/{userId}/enable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_PATH, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> updateEnable(@PathVariable(value = "userId") Integer userId) {
        return this.userService.updateEnable(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT, value = "禁用用户")
    @PutMapping(value = "/{userId}/disable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_PATH, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> disable(@PathVariable(value = "userId") Integer userId) {
        return this.userService.updateDisable(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "新增用户")
    @PostMapping(value = "/save")
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

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_PUT, value = "更新用户")
    @PutMapping(value = "/update")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> update(@RequestBody UserUpdateDTO userUpdateDTO) {
        return this.userService.update(userUpdateDTO);
    }


}
