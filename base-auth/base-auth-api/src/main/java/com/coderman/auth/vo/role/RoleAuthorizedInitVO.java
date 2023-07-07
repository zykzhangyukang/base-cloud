package com.coderman.auth.vo.role;

import com.coderman.api.model.BaseModel;
import com.coderman.auth.vo.func.FuncTreeVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
     * 分配的功能id集合
     */
    private List<Integer> funcIdList;

    /**
     * 每个根节点选中项
     */
    // 半选
    private Map<Integer, Collection<Integer>> halfCheckedMap;
    // 全选
    private Map<Integer, Collection<Integer>> allCheckedMap;

    /**
     * 所有的功能树
     */
    private List<FuncTreeVO> allTreeList;

}
