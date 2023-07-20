package com.coderman.auth.vo.role;

import com.coderman.api.model.BaseModel;
import com.coderman.auth.model.user.UserModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author coderman
 * @Title: 角色分配VO
 * @Description: TOD
 * @date 2022/3/2012:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleUserInitVO extends BaseModel {

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "全部用户列表")
    private List<UserModel> userList;

    @ApiModelProperty(value = "用户id集合")
    private List<Integer> userIdList;
}
