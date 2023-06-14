package com.coderman.auth.vo.func;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coderman
 * @Title: 菜单信息
 * @Description: TOD
 * @date 2022/5/312:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MenuVO")
public class MenuVO extends BaseModel {

    @ApiModelProperty(value = "功能id")
    private Integer funcId;

    @ApiModelProperty(value = "父级id")
    private Integer parentId;

    @ApiModelProperty(value = "功能名称")
    private String funcName;

    @ApiModelProperty(value = "功能key")
    private String funcKey;

    @ApiModelProperty(value = "功能icon")
    private String funcIcon;

    @ApiModelProperty(value = "菜单显示状态")
    private String funcDirStatus;

    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;

    List<MenuVO> children =  new ArrayList<>();
}
