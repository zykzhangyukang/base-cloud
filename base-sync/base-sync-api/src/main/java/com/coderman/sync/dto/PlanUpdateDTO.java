package com.coderman.sync.dto;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlanUpdateDTO extends BaseModel {

    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "计划内容")
    private String planContent;

}
