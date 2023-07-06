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
    private Integer roleId;

    @ApiModelProperty(value = "功能key")
    private List<String> funcKeys;



}
