package com.coderman.auth.vo.role;

import com.coderman.api.model.BaseModel;
import com.coderman.auth.vo.func.FuncTreeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleFuncInitVO extends BaseModel {

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "半选")
    private Map<Integer, Collection<Integer>> halfCheckedMap;

    @ApiModelProperty(value = "全选")
    private Map<Integer, Collection<Integer>> allCheckedMap;

    @ApiModelProperty(value = "功能树")
    private List<FuncTreeVO> allTreeList;

    @ApiModelProperty(value = "用户列表")
    private List<String> usernameList;

}
