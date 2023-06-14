package com.coderman.auth.dto.func;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FuncPageDTO extends BaseModel {

    @ApiModelProperty(value = "当前页")
    private Integer currentPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "资源url")
    private String rescUrl;

    @ApiModelProperty("功能名称")
    private String funcName;

    @ApiModelProperty("功能key")
    private String funcKey;

    @ApiModelProperty("功能类型(目录/功能)")
    private String funcType;

    @ApiModelProperty("父级功能id")
    private Integer parentId;
}
