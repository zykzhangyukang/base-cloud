package com.coderman.sync.callback.meta;

import com.coderman.sync.task.base.BaseTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CallbackTask extends BaseTask {

    @ApiModelProperty(value = "回调记录uuid")
    private String uuid;

    @ApiModelProperty(value = "回调消息")
    private String msg;

    @ApiModelProperty(value = "数据库")
    private String db;

    @ApiModelProperty(value = "回调目标项目")
    private String project;

    @ApiModelProperty(value = "是否为首次回调")
    private boolean first;
}
