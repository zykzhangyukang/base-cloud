package com.coderman.sync.dto;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlanSaveDTO extends BaseModel {

    @ApiModelProperty(value = "计划内容")
    private String planContent;

}
