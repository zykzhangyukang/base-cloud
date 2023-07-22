package com.coderman.sync.dto;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResultPageDTO extends BaseModel {

    @ApiModelProperty(value = "当前页")
    private Integer currentPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "同步计划编号")
    private String planCode;

    @ApiModelProperty(value = "同步状态")
    private String syncStatus;

    @ApiModelProperty(value = "消息来源")
    private String msgSrc;

    @ApiModelProperty(value = "源系统")
    private String srcProject;

    @ApiModelProperty(value = "目标系统")
    private String destProject;

    @ApiModelProperty(value = "重试次数")
    private Integer repeatCount;

}
