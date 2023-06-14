package com.coderman.auth.dto.resc;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RescSaveDTO extends BaseModel {

    @ApiModelProperty("资源名称")
    private String rescName;

    @ApiModelProperty("资源url")
    private String rescUrl;

    @ApiModelProperty("资源所属系统")
    private String rescDomain;

    @ApiModelProperty("请求方式")
    private String methodType;

}
