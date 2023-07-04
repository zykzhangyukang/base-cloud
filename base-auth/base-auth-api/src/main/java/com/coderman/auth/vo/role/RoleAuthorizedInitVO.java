package com.coderman.auth.vo.role;

import com.coderman.api.model.BaseModel;
import com.coderman.auth.vo.func.FuncTreeVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleAuthorizedInitVO extends BaseModel {

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;


    /**
     * 分配的功能key计划
     */
    private List<String> funcKeyList;

    /**
     * 所有的功能树
     */
    private List<FuncTreeVO> allTreeList;

}