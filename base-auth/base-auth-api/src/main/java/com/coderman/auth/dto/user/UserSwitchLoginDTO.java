package com.coderman.auth.dto.user;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 切换用户登录DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserSwitchLoginDTO extends BaseModel {

    @ApiModelProperty(value = "登录账号")
    private String username;
}
