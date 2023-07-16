package com.coderman.auth.dto.func;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@ApiModel(value = "FuncRescUpdateDTO",description = "功能分配资源DTO")
@EqualsAndHashCode(callSuper = true)
@Data
public class FuncRescUpdateDTO extends BaseModel {

    @ApiModelProperty(value = "功能id")
    private Integer funcId;

    @ApiModelProperty(value = "资源集合")
    private List<FuncRescUpdateItem> rescVOList;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class FuncRescUpdateItem extends BaseModel{

        @ApiModelProperty(value = "资源id")
        private Integer key;

        @ApiModelProperty(value = "标签")
        private String label;
    }
}


