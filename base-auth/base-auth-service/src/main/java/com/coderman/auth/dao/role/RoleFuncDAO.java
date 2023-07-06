package com.coderman.auth.dao.role;

import com.coderman.auth.model.role.RoleFuncExample;
import com.coderman.auth.model.role.RoleFuncModel;
import com.coderman.mybatis.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleFuncDAO extends BaseDAO<RoleFuncModel,RoleFuncExample> {


    /**
     * 批量查询角色-功能关联
     * @param roleId
     * @param funcKeyList
     */
    void batchInsertByRoleId(@Param(value = "roleId") Integer roleId,@Param(value = "funcKeyList") List<String> funcKeyList);


    /**
     * 删除功能角色关联功能id集合
     * @param funcIdList
     */
    void deleteByFuncIdIn(@Param(value = "funcIdList") List<Integer> funcIdList);


    /**
     * 通过角色id删除
     * @param roleId
     */
    void deleteByRoleId(@Param(value = "roleId") Integer roleId);

    /**
     * 根据角色获取
     *
     * @param roleId
     * @return
     */
    List<RoleFuncModel> selectAllByRoleId(@Param(value = "roleId") Integer roleId);
}