package com.coderman.auth.dto.user;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserPageDTO extends BaseModel {

    @ApiModelProperty(value = "当前页")
    private Integer currentPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "真实名称")
    private String realName;

    @ApiModelProperty(value = "部门编号")
    private String deptCode;

    @ApiModelProperty(value = "状态")
    private Integer userStatus;

}
