package com.coderman.auth.dto.func;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleAuthorizedDTO extends BaseModel {

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "功能id")
    private List<Integer> funcIdList;
}
