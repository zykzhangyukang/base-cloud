package com.coderman.auth.dto.func;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@ApiModel(value = "RoleFuncUpdateDTO",description = "角色分配功能DTO")
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleFuncUpdateDTO extends BaseModel {

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "功能id")
    private List<Integer> funcIdList;
}
