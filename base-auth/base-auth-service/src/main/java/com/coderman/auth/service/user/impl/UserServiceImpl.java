package com.coderman.auth.service.user.impl;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.constant.RedisDbConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.util.PageUtil;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.constant.AuthConstant;
import com.coderman.auth.dao.role.RoleDAO;
import com.coderman.auth.dao.user.UserDAO;
import com.coderman.auth.dao.user.UserRoleDAO;
import com.coderman.auth.dto.user.*;
import com.coderman.auth.model.role.RoleModel;
import com.coderman.auth.model.user.UserModel;
import com.coderman.auth.model.user.UserRoleExample;
import com.coderman.auth.model.user.UserRoleModel;
import com.coderman.auth.service.func.FuncService;
import com.coderman.auth.service.resc.RescService;
import com.coderman.auth.service.user.UserService;
import com.coderman.auth.vo.func.FuncTreeVO;
import com.coderman.auth.vo.resc.RescVO;
import com.coderman.auth.vo.user.UserAssignVO;
import com.coderman.auth.vo.user.UserLoginRespVO;
import com.coderman.auth.vo.user.UserPermissionVO;
import com.coderman.auth.vo.user.UserVO;
import com.coderman.erp.vo.AuthUserVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import com.coderman.service.redis.RedisService;
import com.coderman.service.service.BaseService;
import com.coderman.service.util.HttpContextUtil;
import com.coderman.service.util.MD5Utils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author coderman
 * @Title: 用户服务实现
 * @Description: TOD
 * @date 2022/2/2711:41
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Resource
    private UserDAO userDAO;

    @Resource
    private RoleDAO roleDAO;

    @Resource
    private UserRoleDAO userRoleDAO;

    @Resource
    private RedisService redisService;

    @Resource
    private RescService rescService;

    @Resource
    private FuncService funcService;


    @Override
    @LogError(value = "用户登录")
    public ResultVO<UserLoginRespVO> login(@LogErrorParam UserLoginDTO userLoginDTO) {

        try {

            String username = userLoginDTO.getUsername();
            String password = userLoginDTO.getPassword();

            if (StringUtils.isBlank(username)) {

                return ResultUtil.getWarn("用户名不能为空！");
            }

            if (StringUtils.isBlank(password)) {

                return ResultUtil.getWarn("登录密码不能为空！");
            }

            UserVO dbUser = this.userDAO.selectByUsernameVos(username);
            if (Objects.isNull(dbUser)) {

                return ResultUtil.getWarn("用户名或密码错误！");
            }

            if (!StringUtils.equals(MD5Utils.md5Hex(password.getBytes()), dbUser.getPassword())) {

                return ResultUtil.getWarn("用户名或密码错误！");
            }

            if (Objects.equals(dbUser.getUserStatus(), AuthConstant.USER_STATUS_DISABLE)) {

                return ResultUtil.getWarn("用户已被锁定！");
            }

            int expiredSecond = AuthConstant.AUTH_EXPIRED_SECOND;

            String token = RandomStringUtils.randomAlphanumeric(32);
            AuthUserVO authUserVO = new AuthUserVO();
            authUserVO.setUserId(dbUser.getUserId());
            authUserVO.setUsername(username);
            authUserVO.setToken(token);
            authUserVO.setDeptCode(dbUser.getDeptCode());
            authUserVO.setRealName(dbUser.getRealName());
            authUserVO.setDeptName(dbUser.getDeptName());
            authUserVO.setRescIdList(getUserRescIds(dbUser.getUsername()));
            authUserVO.setExpiredTime(DateUtils.addSeconds(new Date(), expiredSecond).getTime());

            // 写会话到redis
            this.redisService.setObject(this.getRedisKey(token), authUserVO, expiredSecond, RedisDbConstant.REDIS_DB_AUTH);

            return ResultUtil.getSuccess(UserLoginRespVO.class, this.convertUserLoginRespVO(authUserVO));

        } catch (Exception e) {

            logger.error("用户登录失败,username:{},msg:{}", userLoginDTO.getUsername(), e.getMessage(), e);

            return ResultUtil.getFail("登录失败,请联系技术人员处理.");
        }

    }

    private UserLoginRespVO convertUserLoginRespVO(AuthUserVO authUserVO) {

        Assert.notNull(authUserVO, "authUserVO is null");

        UserLoginRespVO userLoginRespVO = new UserLoginRespVO();
        userLoginRespVO.setUsername(authUserVO.getUsername());
        userLoginRespVO.setToken(authUserVO.getToken());
        userLoginRespVO.setDeptCode(authUserVO.getDeptCode());
        userLoginRespVO.setToken(authUserVO.getToken());
        userLoginRespVO.setRealName(authUserVO.getRealName());
        userLoginRespVO.setDeptName(authUserVO.getDeptName());

        return userLoginRespVO;
    }

    /**
     * 获取用户拥有的资源id
     *
     * @param username 用户名
     * @return
     */
    @LogError(value = "获取用户拥有的资源id")
    private List<Integer> getUserRescIds(String username) {
        return this.rescService.selectRescListByUsername(username).stream()
                .map(RescVO::getRescId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @LogError(value = "获取用户信息")
    public ResultVO<UserPermissionVO> info(String defaultToken) {

        HttpServletRequest httpServletRequest = HttpContextUtil.getHttpServletRequest();
        HttpServletResponse httpServletResponse = HttpContextUtil.getHttpServletResponse();
        String token = StringUtils.defaultString(httpServletRequest.getHeader(CommonConstant.USER_TOKEN_NAME), defaultToken);

        try {

            // 校验用户登录的token
            if (StringUtils.isBlank(token) || StringUtils.length(token.trim()) != 32) {

                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                return ResultUtil.getFail(ResultConstant.RESULT_CODE_401, "登录令牌不存在！");
            }

            // 获取会话信息
            ResultVO<AuthUserVO> resultVO = this.getUserByToken(token);
            AuthUserVO authUserVO = resultVO.getResult();

            if (!ResultConstant.RESULT_CODE_200.equals(resultVO.getCode()) || Objects.isNull(authUserVO)) {
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                return ResultUtil.getFail(ResultConstant.RESULT_CODE_401, resultVO.getMsg());
            }

            // 获取用户信息
            String username = authUserVO.getUsername();
            ResultVO<UserVO> voResultVO = this.selectUserByName(username);
            UserVO userVO = voResultVO.getResult();

            if (!ResultConstant.RESULT_CODE_200.equals(voResultVO.getCode()) || Objects.isNull(userVO) || !AuthConstant.USER_STATUS_ENABLE.equals(userVO.getUserStatus())) {

                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                return ResultUtil.getFail(ResultConstant.RESULT_CODE_401, String.format("登录用户[%s]不存在！", username));
            }

            // 查询菜单
            ResultVO<List<FuncTreeVO>> r1 = this.funcService.selectMenusTreeByUserId(userVO.getUserId());
            if (!ResultConstant.RESULT_CODE_200.equals(r1.getCode())) {

                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                return ResultUtil.getFail(ResultConstant.RESULT_CODE_401, "获取菜单失败！");
            }

            // 查询功能
            ResultVO<List<String>> vo = this.funcService.selectFuncKeyListByUserId(userVO.getUserId());
            if (!ResultConstant.RESULT_CODE_200.equals(vo.getCode())) {

                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                return ResultUtil.getFail(ResultConstant.RESULT_CODE_401, "获取功能失败！");
            }

            UserPermissionVO userPermissionVO = new UserPermissionVO();
            userPermissionVO.setUserId(authUserVO.getUserId());
            userPermissionVO.setUsername(username);
            userPermissionVO.setDeptCode(authUserVO.getDeptCode());
            userPermissionVO.setDeptName(authUserVO.getDeptName());
            userPermissionVO.setRealName(authUserVO.getRealName());
            userPermissionVO.setExpiredTime(new Date(authUserVO.getExpiredTime()));
            userPermissionVO.setMenus(r1.getResult());
            userPermissionVO.setButtons(vo.getResult());
            return ResultUtil.getSuccess(UserPermissionVO.class, userPermissionVO);

        } catch (Exception e) {

            logger.error("获取用户信息异常:{},token:{}", e.getMessage(), token, e);

            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return ResultUtil.getFail(ResultConstant.RESULT_CODE_401, "获取用户信息异常！");
        }
    }

    @Override
    @LogError(value = "根据token获取用户信息")
    public ResultVO<AuthUserVO> getUserByToken(String token) {

        AuthUserVO authUserVO = this.redisService.getObject(this.getRedisKey(token), AuthUserVO.class, RedisDbConstant.REDIS_DB_AUTH);

        if (Objects.isNull(authUserVO)) {

            return ResultUtil.getWarn("用户登录会话已过期！");
        }

        return ResultUtil.getSuccess(AuthUserVO.class, authUserVO);
    }

    @Override
    @LogError(value = "用户退出登录")
    public ResultVO<Void> logout(@LogErrorParam String token) {

        if (StringUtils.isNotBlank(token)) {

            this.redisService.del(this.getRedisKey(token), RedisDbConstant.REDIS_DB_AUTH);
        }

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "用户刷新登录")
    public ResultVO<String> refreshLogin(String oldToken) {

        ResultVO<UserPermissionVO> permissionByToken = this.info(oldToken);

        if (!ResultConstant.RESULT_CODE_200.equals(permissionByToken.getCode())) {

            return ResultUtil.getFail(permissionByToken.getMsg());
        }

        UserPermissionVO userPermissionVO = permissionByToken.getResult();

        // 生成新token
        String newToken = RandomStringUtils.randomAlphanumeric(32);

        int expiredSecond = AuthConstant.AUTH_EXPIRED_SECOND;

        AuthUserVO authUserVO = new AuthUserVO();
        authUserVO.setUserId(userPermissionVO.getUserId());
        authUserVO.setDeptName(userPermissionVO.getDeptName());
        authUserVO.setUsername(userPermissionVO.getUsername());
        authUserVO.setDeptCode(userPermissionVO.getDeptCode());
        authUserVO.setRealName(userPermissionVO.getRealName());
        authUserVO.setRescIdList(getUserRescIds(userPermissionVO.getUsername()));
        authUserVO.setToken(newToken);
        authUserVO.setExpiredTime(DateUtils.addSeconds(new Date(), expiredSecond).getTime());

        // 签发新的token
        this.redisService.setObject(this.getRedisKey(newToken), authUserVO, expiredSecond, RedisDbConstant.REDIS_DB_AUTH);
        // 删除当前token
        this.redisService.del(this.getRedisKey(oldToken), RedisDbConstant.REDIS_DB_AUTH);

        return ResultUtil.getSuccess(String.class, newToken);
    }

    private String getRedisKey(String userToken) {

        Assert.isTrue(StringUtils.isNotBlank(userToken), "userToken is blank");

        return AuthConstant.AUTH_TOKEN_NAME + userToken;
    }

    /**
     * 用户列表
     *
     * @param queryVO
     * @return
     */
    @Override
    @LogError(value = "查询用户列表")
    public ResultVO<PageVO<List<UserVO>>> page(@LogErrorParam UserPageDTO queryVO) {

        Map<String, Object> conditionMap = new HashMap<>(4);

        Integer pageSize = queryVO.getPageSize();
        Integer currentPage = queryVO.getCurrentPage();

        if (StringUtils.isNotBlank(queryVO.getUsername())) {
            conditionMap.put("username", queryVO.getUsername());
        }

        if (StringUtils.isNotBlank(queryVO.getRealName())) {
            conditionMap.put("realName", queryVO.getRealName());
        }

        if (Objects.nonNull(queryVO.getUserStatus())) {
            conditionMap.put("userStatus", queryVO.getUserStatus());
        }

        if (StringUtils.isNotBlank(queryVO.getDeptCode())) {
            conditionMap.put("deptCode", queryVO.getDeptCode());
        }

        PageUtil.getConditionMap(conditionMap, currentPage, pageSize);

        // 总条数
        Long count = this.userDAO.countPage(conditionMap);

        List<UserVO> userVOList = new ArrayList<>();

        if (count > 0) {

            userVOList = this.userDAO.selectPage(conditionMap);
        }


        return ResultUtil.getSuccessPage(UserVO.class, PageUtil.getPageVO(count, userVOList, currentPage, pageSize));
    }

    /**
     * 用户创建
     *
     * @param userSaveDTO
     * @return
     */
    @Override
    @LogError(value = "新增用户信息")
    public ResultVO<Void> save(@LogErrorParam UserSaveDTO userSaveDTO) {

        String username = userSaveDTO.getUsername();
        String realName = userSaveDTO.getRealName();
        String password = userSaveDTO.getPassword();
        Integer userStatus = userSaveDTO.getUserStatus();
        String deptCode = userSaveDTO.getDeptCode();
        Date currentDate = new Date();

        if (StringUtils.isBlank(username)) {

            return ResultUtil.getWarn("用户账号不能为空");
        }

        if (StringUtils.length(username) < 4 || StringUtils.length(username) > 15) {

            return ResultUtil.getWarn("用户账号4-15个字符!");
        }

        if (StringUtils.isBlank(realName)) {

            return ResultUtil.getWarn("真实姓名不能为空！");
        }

        if (StringUtils.length(realName) < 2 || StringUtils.length(realName) > 10) {

            return ResultUtil.getWarn("真实姓名2-10个字符！");
        }

        if (StringUtils.isBlank(password)) {

            return ResultUtil.getWarn("登录密码不能为空！");
        }

        if (StringUtils.length(password) < 5 || StringUtils.length(password) > 15) {

            return ResultUtil.getWarn("登录密码5-15个字符！");
        }

        if (StringUtils.isBlank(deptCode)) {

            return ResultUtil.getWarn("所属部门不能为空！");
        }

        if (Objects.isNull(userStatus)) {

            return ResultUtil.getWarn("用户状态不能为空！");
        }

        // 校验是否存在账号
        UserVO userVO = this.userDAO.selectByUsernameVos(username);

        if (Objects.nonNull(userVO)) {

            return ResultUtil.getFail("存在重复的账号【" + username + "】!");
        }

        // 新增用户
        UserModel insertModel = new UserModel();
        insertModel.setUsername(username);
        insertModel.setRealName(realName);
        insertModel.setPassword(MD5Utils.md5Hex(password.getBytes()));
        insertModel.setCreateTime(currentDate);
        insertModel.setUpdateTime(currentDate);
        insertModel.setUserStatus(userStatus);
        insertModel.setDeptCode(deptCode);

        this.userDAO.insert(insertModel);

        return ResultUtil.getSuccess();
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @Override
    @LogError(value = "删除用户信息")
    public ResultVO<Void> delete(Integer userId) {

        UserModel dbUserModel = this.userDAO.selectByPrimaryKey(userId);
        if (dbUserModel == null) {

            return ResultUtil.getFail("用户信息不存在！");
        }

        if (AuthConstant.USER_STATUS_ENABLE.equals(dbUserModel.getUserStatus())) {

            return ResultUtil.getFail("请删除禁用状态的记录！");
        }

        // 删除用户-角色关联
        this.userRoleDAO.deleteByUserId(userId);

        // 删除用户
        this.userDAO.deleteByPrimaryKey(userId);
        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "更新用户信息")
    public ResultVO<Void> update(UserUpdateDTO userUpdateDTO) {

        Integer userId = userUpdateDTO.getUserId();
        String realName = userUpdateDTO.getRealName();
        String deptCode = userUpdateDTO.getDeptCode();
        Integer userStatus = userUpdateDTO.getUserStatus();

        if (StringUtils.isBlank(deptCode)) {

            return ResultUtil.getWarn("所属部门不能为空！");
        }

        if (Objects.isNull(userStatus)) {

            return ResultUtil.getWarn("用户状态不能为空！");
        }

        if (StringUtils.isBlank(realName)) {

            return ResultUtil.getWarn("真实姓名不能为空！");
        }

        if (StringUtils.length(realName) < 2 || StringUtils.length(realName) > 10) {

            return ResultUtil.getWarn("真实姓名2-10个字符！");
        }

        // 更新
        UserModel updateModel = new UserModel();
        updateModel.setUserId(userId);
        updateModel.setUpdateTime(new Date());
        updateModel.setRealName(realName);
        updateModel.setUserStatus(userStatus);
        updateModel.setDeptCode(deptCode);

        this.userDAO.updateByPrimaryKeySelective(updateModel);
        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "获取用户信息")
    public ResultVO<UserVO> selectUserById(Integer userId) {

        if (Objects.isNull(userId)) {

            return ResultUtil.getWarn("用户id不能为空！");
        }

        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);
        if (null == userModel) {

            return ResultUtil.getWarn("用户不存在！");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        userVO.setPassword(null);
        return ResultUtil.getSuccess(UserVO.class, userVO);
    }

    @Override
    @LogError(value = "根据用户名获取用户信息")
    public ResultVO<UserVO> selectUserByName(String username) {

        if (StringUtils.isBlank(username)) {

            return ResultUtil.getFail(UserVO.class, null, "用户名参数必传！");
        }

        UserVO userVO = this.userDAO.selectByUsernameVos(username);
        if (userVO == null) {

            return ResultUtil.getFail(UserVO.class, null, "用户信息不存在,username=" + username);
        }

        // 查询用户角色
        List<String> roleNames = this.roleDAO.selectUserRoleList(userVO.getUserId()).stream()
                .map(RoleModel::getRoleName)
                .collect(Collectors.toList());
        userVO.setRoleList(roleNames);

        return ResultUtil.getSuccess(UserVO.class, userVO);
    }

    @Override
    @LogError(value = "启用用户")
    public ResultVO<Void> updateEnable(Integer userId) {

        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);
        if (Objects.isNull(userModel)) {

            return ResultUtil.getFail("用户不存在！");
        }

        if (AuthConstant.USER_STATUS_ENABLE.equals(userModel.getUserStatus())) {

            return ResultUtil.getWarn("已经是启用状态！");
        }

        UserModel updateModel = new UserModel();
        updateModel.setUserId(userId);
        updateModel.setUserStatus(AuthConstant.USER_STATUS_ENABLE);
        this.userDAO.updateByPrimaryKeySelective(updateModel);
        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "用户禁用")
    public ResultVO<Void> updateDisable(Integer userId) {

        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);
        if (Objects.isNull(userModel)) {

            return ResultUtil.getFail("用户不存在！");
        }

        if (AuthConstant.USER_STATUS_DISABLE.equals(userModel.getUserStatus())) {

            return ResultUtil.getWarn("用户已经是禁用状态");
        }

        UserModel updateModel = new UserModel();
        updateModel.setUserId(userId);
        updateModel.setUserStatus(AuthConstant.USER_STATUS_DISABLE);
        this.userDAO.updateByPrimaryKeySelective(updateModel);
        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "用户分配初始化")
    public ResultVO<UserAssignVO> selectAssignInit(@LogErrorParam Integer userId) {
        UserAssignVO userAssignVO = new UserAssignVO();

        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);
        if (Objects.isNull(userModel)) {

            return ResultUtil.getFail("用户不存在！");
        }

        userAssignVO.setUserId(userId);

        // 查询全部角色信息
        List<RoleModel> roleModels = this.roleDAO.selectByExample(null);
        userAssignVO.setRoleList(roleModels);

        // 查询用户已有的角色
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserRoleModel> userRoleModels = this.userRoleDAO.selectByExample(example);
        List<Integer> userRoleIds = userRoleModels.stream().map(UserRoleModel::getRoleId).collect(Collectors.toList());

        userAssignVO.setAssignedIdList(userRoleIds);

        return ResultUtil.getSuccess(UserAssignVO.class, userAssignVO);
    }

    @Override
    @LogError(value = "用户分配角色")
    public ResultVO<Void> updateAssign(@LogErrorParam UserAssignDTO userAssignDTO) {

        Integer userId = userAssignDTO.getUserId();
        List<Integer> roleIdList = userAssignDTO.getRoleIdList();

        if (Objects.isNull(userId)) {

            return ResultUtil.getWarn("用户id不能为空！");
        }

        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);

        if (userModel == null) {

            return ResultUtil.getWarn("用户不存在！");
        }

        // 清空之前的权限
        this.userRoleDAO.deleteByUserId(userId);

        // 批量新增
        if (!CollectionUtils.isEmpty(roleIdList)) {

            this.userRoleDAO.insertBatchByUserId(userId, roleIdList);
        }

        return ResultUtil.getSuccess();
    }


    @Override
    @LogError(value = "用户更新密码")
    public ResultVO<Void> updatePassword(@LogErrorParam UserUpdatePwdDTO userUpdatePwdDTO) {

        Integer userId = userUpdatePwdDTO.getUserId();
        String password = userUpdatePwdDTO.getPassword();

        if (Objects.isNull(userId)) {

            return ResultUtil.getWarn("用户id不能为空！");
        }

        if (StringUtils.isBlank(password)) {

            return ResultUtil.getWarn("登录密码不能为空！");
        }

        if (StringUtils.isBlank(password)) {

            return ResultUtil.getWarn("登录密码不能为空！");
        }

        if (StringUtils.length(password) < 5 || StringUtils.length(password) > 15) {

            return ResultUtil.getWarn("登录密码5-15个字符！");
        }

        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);

        if (userModel == null) {

            return ResultUtil.getWarn("用户不存在！");
        }

        UserModel record = new UserModel();
        record.setUserId(userId);
        record.setPassword(MD5Utils.md5Hex(password.getBytes()));
        this.userDAO.updateByPrimaryKeySelective(record);
        return ResultUtil.getSuccess();
    }


}
