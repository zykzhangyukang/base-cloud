package com.coderman.sync.dto;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlanUpdateStatusDTO extends BaseModel {

    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "状态")
    private String status;
}
