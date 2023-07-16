package com.coderman.auth.service.role;


import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dto.func.RoleFuncUpdateDTO;
import com.coderman.auth.dto.role.RolePageDTO;
import com.coderman.auth.dto.role.RoleSaveDTO;
import com.coderman.auth.dto.role.RoleUpdateDTO;
import com.coderman.auth.vo.role.RoleFuncCheckVO;
import com.coderman.auth.vo.role.RoleFuncInitVO;
import com.coderman.auth.vo.role.RoleUserInitVO;
import com.coderman.auth.vo.role.RoleVO;

import java.util.List;

/**
 * @author coderman
 * @date 2022/2/2711:41
 */
public interface RoleService {

    /**
     * 角色列表
     *
     * @return
     */
    ResultVO<PageVO<List<RoleVO>>> page(RolePageDTO rolePageDTO);

    /**
     * 角色新增
     *
     * @param roleSaveDTO
     * @return
     */
    ResultVO<Void> save(RoleSaveDTO roleSaveDTO);


    /**
     * 角色删除
     *
     * @param roleId
     * @return
     */
    ResultVO<Void> delete(Integer roleId);

    /**
     * 更新角色
     *
     * @param roleUpdateDTO
     * @return
     */
    ResultVO<Void> update(RoleUpdateDTO roleUpdateDTO);


    /**
     * 角色获取
     *
     * @param roleId
     * @return
     */
    ResultVO<RoleVO> selectRoleById(Integer roleId);


    /**
     * 角色分配用户 - 初始化
     * @param roleId
     * @return
     */
    ResultVO<RoleUserInitVO> selectRoleUserInit(Integer roleId);

    /**
     * 角色分配用户
     * @param roleId
     * @param assignedIdList
     * @return
     */
    ResultVO<Void> updateRoleUser(Integer roleId, List<Integer> assignedIdList);



    /**
     * 角色分配功能 - 初始化
     * @param roleId
     * @return
     */
    ResultVO<RoleFuncInitVO> selectRoleFuncInit(String roleId);


    /**
     * 角色分配功能
     *
     * @return
     */
    ResultVO<Void> updateRoleFunc(RoleFuncUpdateDTO roleFuncUpdateDTO);


    /**
     * 角色分配功能预先检查
     *
     * @param roleFuncUpdateDTO
     * @return
     */
    ResultVO<RoleFuncCheckVO> roleFuncBeforeCheck(RoleFuncUpdateDTO roleFuncUpdateDTO);
}
