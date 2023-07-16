package com.coderman.auth.dto.user;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色分配用户DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRoleUpdateDTO extends BaseModel {

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "角色id集合")
    private List<Integer> roleIdList;
}
