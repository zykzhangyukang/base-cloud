package com.coderman.auth.dao.role;

import com.coderman.auth.model.role.RoleExample;
import com.coderman.auth.model.role.RoleModel;
import com.coderman.auth.vo.role.RoleVO;
import com.coderman.mybatis.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleDAO extends BaseDAO<RoleModel, RoleExample> {


    /**
     * 角色列表
     *
     * @return
     */
    List<RoleVO> page(Map<String,Object> conditionMap);

    /**
     * 分页条数
     *
     * @param conditionMap
     * @return
     */
    Long countPage(Map<String, Object> conditionMap);


    /**
     * 根据角色id查询用户
     * @param roleId
     * @return
     */
    List<String> selectUserByRoleId(@Param(value = "roleId") Integer roleId);


    /**
     * 根据角色名查询角色
     *
     * @param roleName 角色名称
     * @return
     */
    RoleModel selectByRoleName(@Param(value = "roleName") String roleName);

    /**
     * 查看用户拥有的角色
     *
     * @param userId
     * @return
     */
    List<RoleModel> selectUserRoleList(@Param(value = "userId") Integer userId);
}