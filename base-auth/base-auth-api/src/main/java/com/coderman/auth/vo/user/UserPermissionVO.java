package com.coderman.auth.vo.user;

import com.coderman.api.model.BaseModel;
import com.coderman.auth.vo.func.MenuVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author coderman
 * @Title: 用户权限菜单洗洗脑
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPermissionVO extends BaseModel {

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "部门编号")
    private String deptCode;

    @ApiModelProperty(value = "真实名称")
    private String realName;

    @ApiModelProperty(value = "菜单信息")
    private List<MenuVO> menus;

    @ApiModelProperty(value = "功能key")
    private List<String> funcKeys;

    @ApiModelProperty(value = "角色信息")
    private List<String> roles;
}
