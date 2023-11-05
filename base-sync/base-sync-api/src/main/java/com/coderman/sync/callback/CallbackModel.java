package com.coderman.sync.callback;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class CallbackModel extends BaseModel {

    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "消息id")
    private String msgId;

    @ApiModelProperty(value = "消息内容")
    private String msgContent;

    @ApiModelProperty(value = "源系统")
    private String srcProject;

    @ApiModelProperty(value = "目标系统")
    private String destProject;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "发送时间")
    private Date sendTime;

    @ApiModelProperty(value = "ack时间")
    private Date ackTime;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "重试次数")
    private Integer repeatCount;
}
