package com.coderman.auth.vo.func;

import com.coderman.api.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author coderman
 * @Title: 功能树
 * @Description: TOD
 * @date 2022/3/1915:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FuncTreeVO extends BaseModel {

    @JsonProperty(value = "value")
    @ApiModelProperty(value = "功能id")
    private Integer funcId;

    @JsonProperty(value = "title")
    @ApiModelProperty(value = "功能名称")
    private String funcName;

    @JsonProperty(value = "key")
    @ApiModelProperty(value = "功能key")
    private String funcKey;

    @ApiModelProperty(value = "功能排序")
    private Integer funcSort;

    @ApiModelProperty(value = "父级id")
    private Integer parentId;

    @ApiModelProperty(value = "功能类型")
    private String funcType;

    /**
     * 子功能
     */
    @JsonProperty(value = "children")
    private List<FuncTreeVO> childrenList;
}
