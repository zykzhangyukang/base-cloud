package com.coderman.sync.dto;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResultValidDataDTO extends BaseModel {

    @ApiModelProperty(value = "消息内容")
    private String msgContent;
}
