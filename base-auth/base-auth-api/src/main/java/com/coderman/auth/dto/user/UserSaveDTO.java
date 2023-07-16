package com.coderman.auth.dto.user;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户添加DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserSaveDTO extends BaseModel {

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "真实名称")
    private String realName;

    @ApiModelProperty(value = "部门编号")
    private String deptCode;

    @ApiModelProperty(value = "状态")
    private Integer userStatus;
}
