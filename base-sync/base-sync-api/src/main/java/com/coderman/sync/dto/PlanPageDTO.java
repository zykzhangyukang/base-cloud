package com.coderman.sync.dto;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlanPageDTO extends BaseModel {

    @ApiModelProperty(value = "当前页")
    private Integer currentPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "同步计划编号")
    private String planCode;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "源数据库")
    private String srcDb;

    @ApiModelProperty(value = "目标数据库")
    private String descDb;

    @ApiModelProperty(value = "源系统")
    private String srcProject;

    @ApiModelProperty(value = "目标系统")
    private String destProject;

    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "排序规则")
    private String sortOrder;

}
