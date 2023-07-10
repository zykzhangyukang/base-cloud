package com.coderman.auth.service.role;


import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dto.func.RoleAuthorizedDTO;
import com.coderman.auth.dto.role.RolePageDTO;
import com.coderman.auth.dto.role.RoleSaveDTO;
import com.coderman.auth.dto.role.RoleUpdateDTO;
import com.coderman.auth.vo.role.RoleAssignVO;
import com.coderman.auth.vo.role.RoleAuthCheckVO;
import com.coderman.auth.vo.role.RoleAuthorizedInitVO;
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
     * 角色分配初始化
     * @param roleId
     * @return
     */
    ResultVO<RoleAssignVO> roleUserUpdateInit(Integer roleId);

    /**
     * 角色分配用户
     * @param roleId
     * @param assignedIdList
     * @return
     */
    ResultVO<Void> roleUserUpdate(Integer roleId, List<Integer> assignedIdList);




    /**
     * 分配功能初始化
     * @param roleId
     * @return
     */
    ResultVO<RoleAuthorizedInitVO> roleAuthorizedInit(String roleId);


    /**
     * 分配功能
     * @return
     */
    ResultVO<Void> roleAuthorizedUpdate(RoleAuthorizedDTO roleAuthorizedDTO);


    /**
     * 分配功能预先检查
     *
     * @param roleAuthorizedDTO
     * @return
     */
    ResultVO<RoleAuthCheckVO> roleAuthorizedCheck(RoleAuthorizedDTO roleAuthorizedDTO);
}
