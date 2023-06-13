package com.coderman.auth.service.user;


import com.coderman.api.exception.BusinessException;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dto.user.*;
import com.coderman.auth.vo.user.*;

import java.util.List;

/**
 * @author coderman
 * @date 2022/2/2711:41
 */
public interface UserService {

    /**
     * 用户列表
     *
     * @return
     */
    ResultVO<PageVO<List<UserVO>>> page(UserPageDTO queryVO);

    /**
     * 用户新增
     *
     * @param userSaveDTO
     * @return
     */
    ResultVO<Void> save(UserSaveDTO userSaveDTO);


    /**
     * 用户删除
     *
     * @param userId
     * @return
     */
    ResultVO<Void> delete(Integer userId);

    /**
     * 更新用户
     *
     * @param userUpdateDTO
     * @return
     */
    ResultVO<Void> update(UserUpdateDTO userUpdateDTO);


    /**
     * 用户详情
     *
     * @param userId
     * @return
     */
    ResultVO<UserVO> selectUserById(Integer userId);


    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    ResultVO<UserVO> selectUserByName(String username);


    /**
     * 启用用户
     *
     * @param userId
     * @return
     */
    ResultVO<Void> updateEnable(Integer userId);


    /**
     * 禁用用户
     *
     * @param userId
     * @return
     */
    ResultVO<Void> updateDisable(Integer userId);


    /**
     * 用户分配初始化
     *
     * @param userId
     * @return
     */
    ResultVO<UserAssignVO> selectAssignInit(Integer userId);

    /**
     * 用户分配角色
     *
     * @param userId
     * @param assignedIdList
     * @return
     */
    ResultVO<Void> updateAssign(Integer userId, List<Integer> assignedIdList);

    /**
     * 查看用户拥有的角色信息
     *
     * @param userId
     * @return
     */
    ResultVO<List<String>> selectRoleNames(Integer userId);


    /**
     * 设置密码
     *
     * @return
     */
    ResultVO<Void> updatePassword(UserUpdatePwdDTO userUpdatePwdDTO);


    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    ResultVO<UserLoginRespVO> login(UserLoginDTO userLoginDTO) throws BusinessException;


    /**
     * 获取用户信息
     *
     * @return
     */
    ResultVO<UserPermissionVO> info(String token);


    /**
     * 根据token获取用户信息
     *
     * @param token token
     * @return
     */
    ResultVO<AuthUserVO> getUserByToken(String token);


    /**
     * 用户注销登录
     *
     * @param token 令牌
     * @return
     */
    ResultVO<Void> logout(String token);


    /**
     * 用户刷新登录
     *
     * @return
     */
    ResultVO<String> refreshLogin(String token);
}
