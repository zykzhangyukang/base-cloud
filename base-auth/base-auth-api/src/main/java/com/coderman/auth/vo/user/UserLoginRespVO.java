package com.coderman.auth.vo.user;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginRespVO extends BaseModel {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "部门编号")
    private String deptCode;

    @ApiModelProperty(value = "真实名称")
    private String realName;

    @ApiModelProperty(value = "令牌")
    private String token;
}
