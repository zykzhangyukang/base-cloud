package com.coderman.auth.dto.resc;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RescPageDTO extends BaseModel {

    @ApiModelProperty(value = "当前页")
    private Integer currentPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "资源名称")
    private String rescName;

    @ApiModelProperty(value = "资源url")
    private String rescUrl;

    @ApiModelProperty(value = "资源所属系统")
    private String rescDomain;

    @ApiModelProperty(value = "请求方式")
    private String methodType;

}
