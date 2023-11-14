package com.coderman.auth.service.user.impl;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.constant.RedisDbConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.util.PageUtil;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.constant.AuthConstant;
import com.coderman.auth.constant.RedisConstant;
import com.coderman.auth.constant.WebSocketChannelEnum;
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
import com.coderman.auth.service.websocket.WebSocketService;
import com.coderman.auth.utils.PasswordUtils;
import com.coderman.auth.vo.func.FuncTreeVO;
import com.coderman.auth.vo.resc.RescVO;
import com.coderman.auth.vo.user.UserLoginRespVO;
import com.coderman.auth.vo.user.UserPermissionVO;
import com.coderman.auth.vo.user.UserRoleInitVO;
import com.coderman.auth.vo.user.UserVO;
import com.coderman.erp.util.AuthUtil;
import com.coderman.erp.vo.AuthUserVO;
import com.coderman.redis.service.RedisService;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import com.coderman.service.service.BaseService;
import com.coderman.service.util.HttpContextUtil;
import com.coderman.service.util.UUIDUtils;
import com.coderman.sync.util.MsgBuilder;
import com.coderman.sync.util.ProjectEnum;
import com.coderman.sync.util.SyncUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    @Resource
    private WebSocketService webSocketService;


    @Override
    @LogError(value = "切换用户登录")
    public ResultVO<UserLoginRespVO> switchLogin(@LogErrorParam UserSwitchLoginDTO userSwitchLoginDTO) {

        String username = userSwitchLoginDTO.getUsername();
        AuthUserVO current = AuthUtil.getCurrent();

        Assert.isTrue(StringUtils.isNotBlank(username), "登录账号不能为空！");

        ResultVO<UserVO> resultVO = this.selectUserByName(username);
        if (!ResultConstant.RESULT_CODE_200.equals(resultVO.getCode())) {

            return ResultUtil.getFail(resultVO.getMsg());
        }

        UserVO dbUser = resultVO.getResult();

        if (Objects.isNull(dbUser)) {

            return ResultUtil.getWarn("用户不存在！");
        }

        if (!AuthConstant.USER_STATUS_ENABLE.equals(dbUser.getUserStatus())) {

            return ResultUtil.getWarn("用户已被锁定！");
        }

        UserLoginRespVO response = this.generateAndStoreToken(dbUser);

        // 删除当前token
        this.redisService.del(this.getRedisKey(current.getToken()), RedisDbConstant.REDIS_DB_AUTH);

        return ResultUtil.getSuccess(UserLoginRespVO.class, response);
    }

    /**
     * 签发token并保存到redis中
     *
     * @param dbUser
     * @return
     */
    private UserLoginRespVO generateAndStoreToken(UserVO dbUser) {

        Assert.isTrue(Objects.nonNull(dbUser), "userVO is null");

        int expiredSecond = AuthConstant.AUTH_EXPIRED_SECOND;

        String token = RandomStringUtils.randomAlphanumeric(32);

        AuthUserVO authUserVO = new AuthUserVO();
        authUserVO.setToken(token);
        authUserVO.setUserId(dbUser.getUserId());
        authUserVO.setUsername(dbUser.getUsername());
        authUserVO.setDeptCode(dbUser.getDeptCode());
        authUserVO.setRealName(dbUser.getRealName());
        authUserVO.setDeptName(dbUser.getDeptName());
        authUserVO.setRescIdList(getUserRescIds(dbUser.getUsername()));
        authUserVO.setExpiredTime(DateUtils.addSeconds(new Date(), expiredSecond).getTime());

        // 写会话到redis
        this.redisService.setObject(this.getRedisKey(token), authUserVO, expiredSecond, RedisDbConstant.REDIS_DB_AUTH);

        // 组装响应给前台的对象
        return this.assembleUserInfo(authUserVO);
    }

    @Override
    @LogError(value = "用户登录")
    public ResultVO<UserLoginRespVO> login(@LogErrorParam UserLoginDTO userLoginDTO) {

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

        if (!StringUtils.equals(PasswordUtils.encryptSHA256(password), dbUser.getPassword())) {
            return ResultUtil.getWarn("用户名或密码错误！");
        }
        if (Objects.equals(dbUser.getUserStatus(), AuthConstant.USER_STATUS_DISABLE)) {
            return ResultUtil.getWarn("用户已被锁定！");
        }

        // 签发token
        UserLoginRespVO response = this.generateAndStoreToken(dbUser);
        return ResultUtil.getSuccess(UserLoginRespVO.class, response);
    }

    /**
     * 组装响应给前台的用户信息
     *
     * @param authUserVO
     * @return
     */
    private UserLoginRespVO assembleUserInfo(AuthUserVO authUserVO) {

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

            return ResultUtil.getFail(ResultConstant.RESULT_CODE_500, "获取菜单失败！");
        }

        // 查询功能
        ResultVO<List<String>> vo = this.funcService.selectFuncKeyListByUserId(userVO.getUserId());
        if (!ResultConstant.RESULT_CODE_200.equals(vo.getCode())) {

            return ResultUtil.getFail(ResultConstant.RESULT_CODE_500, "获取功能失败！");
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

        UserVO userVO = this.userDAO.selectByUsernameVos(userPermissionVO.getUsername());

        Assert.isTrue(Objects.nonNull(userVO), "userVO is null");

        // 签发token
        UserLoginRespVO response = this.generateAndStoreToken(userVO);

        // 删除当前token
        this.redisService.del(this.getRedisKey(oldToken), RedisDbConstant.REDIS_DB_AUTH);

        return ResultUtil.getSuccess(String.class, response.getToken());
    }

    @Override
    @LogError(value = "用户离线消息拉取")
    public ResultVO<List<Object>> pullNotify(Integer userId) {

        Assert.notNull(userId , "userId is null");

        // 离线消息key
        String target = String.format(WebSocketChannelEnum.USER_SYS_MSG.getSubscribeUrl(), userId);
        String listKey = RedisConstant.REDIS_UNREAD_MSG_PREFIX + ":" + userId + ":" + target;
        // 拉取消息
        List<Object> list = this.redisService.getListData(listKey, Object.class, RedisDbConstant.REDIS_DB_DEFAULT);
        // 删除消息
        this.redisService.del(listKey,RedisDbConstant.REDIS_DB_DEFAULT);

        return ResultUtil.getSuccessList(Object.class, list);
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
        List<UserVO> userVOList = new ArrayList<>();
        Long count = this.userDAO.countPage(conditionMap);
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
        insertModel.setPassword(PasswordUtils.encryptSHA256(password));
        insertModel.setCreateTime(currentDate);
        insertModel.setUpdateTime(currentDate);
        insertModel.setUserStatus(userStatus);
        insertModel.setDeptCode(deptCode);

        this.userDAO.insertReturnKey(insertModel);

        SyncUtil.sync(
                MsgBuilder.createOrderlyMsg("insert_auth_demo_user", ProjectEnum.AUTH, ProjectEnum.AUTH_SYNC, "user:" + insertModel.getUserId())
                        .addIntList("insert_auth_demo_user", Collections.singletonList(insertModel.getUserId()))
                        .build()
        );



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

            return ResultUtil.getWarn("请删除禁用状态的记录！");
        }

        // 删除用户-角色关联
        this.userRoleDAO.deleteByUserId(userId);

        // 删除用户
        this.userDAO.deleteByPrimaryKey(userId);

        SyncUtil.sync(
                MsgBuilder.createOrderlyMsg("delete_auth_demo_user", ProjectEnum.AUTH, ProjectEnum.AUTH_SYNC, "user:" + userId)
                        .addIntList("delete_auth_demo_user", Collections.singletonList(userId))
                        .build()
        );

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

        SyncUtil.sync(
                MsgBuilder.createOrderlyMsg("update_auth_demo_user", ProjectEnum.AUTH, ProjectEnum.AUTH_SYNC, "user:" + userId)
                        .addIntList("update_auth_demo_user", Collections.singletonList(userId))
                        .build()
        );


        Set<String> singleton = Collections.singleton("您收到一条系统消息，请注意查收！" + UUIDUtils.getPrimaryValue());
        webSocketService.sendToUser(-1, userId , singleton);

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
        updateModel.setUpdateTime(new Date());
        updateModel.setUserId(userId);
        updateModel.setUserStatus(AuthConstant.USER_STATUS_ENABLE);
        this.userDAO.updateByPrimaryKeySelective(updateModel);

        SyncUtil.sync(
                MsgBuilder.createOrderlyMsg("update_auth_demo_user", ProjectEnum.AUTH, ProjectEnum.AUTH_SYNC, "user:" + userId)
                        .addIntList("update_auth_demo_user_status", Collections.singletonList(userId))
                        .build()
        );


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
        updateModel.setUpdateTime(new Date());
        this.userDAO.updateByPrimaryKeySelective(updateModel);

        SyncUtil.sync(
                MsgBuilder.createOrderlyMsg("update_auth_demo_user", ProjectEnum.AUTH, ProjectEnum.AUTH_SYNC, "user:" + userId)
                        .addIntList("update_auth_demo_user_status", Collections.singletonList(userId))
                        .build()
        );


        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "用户分配角色初始化")
    public ResultVO<UserRoleInitVO> selectUserRoleInit(@LogErrorParam Integer userId) {

        Assert.isTrue(Objects.nonNull(userId), "userId is null");

        UserRoleInitVO userRoleInitVO = new UserRoleInitVO();
        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);
        if (Objects.isNull(userModel)) {

            return ResultUtil.getFail("用户不存在！");
        }

        userRoleInitVO.setUserId(userId);

        // 查询全部角色信息
        List<RoleModel> roleModels = this.roleDAO.selectByExample(null);
        userRoleInitVO.setRoleList(roleModels);

        // 查询用户已有的角色
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserRoleModel> userRoleModels = this.userRoleDAO.selectByExample(example);
        List<Integer> userRoleIds = userRoleModels.stream().map(UserRoleModel::getRoleId).collect(Collectors.toList());

        userRoleInitVO.setAssignedIdList(userRoleIds);

        return ResultUtil.getSuccess(UserRoleInitVO.class, userRoleInitVO);
    }

    @Override
    @LogError(value = "用户分配角色")
    public ResultVO<Void> updateUserRole(@LogErrorParam UserRoleUpdateDTO userRoleUpdateDTO) {

        Integer userId = userRoleUpdateDTO.getUserId();
        List<Integer> roleIdList = userRoleUpdateDTO.getRoleIdList();

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
    public ResultVO<Void> updateUserPwd(@LogErrorParam UserPwdUpdateDTO userPwdUpdateDTO) {

        Integer userId = userPwdUpdateDTO.getUserId();
        String password = userPwdUpdateDTO.getPassword();

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
        record.setPassword(PasswordUtils.encryptSHA256(password));
        record.setUpdateTime(new Date());
        this.userDAO.updateByPrimaryKeySelective(record);

        SyncUtil.sync(
                MsgBuilder.createOrderlyMsg("update_auth_demo_user", ProjectEnum.AUTH, ProjectEnum.AUTH_SYNC, "user:" + userId)
                        .addIntList("update_auth_demo_user_pwd", Collections.singletonList(userId))
                        .build()
        );

        return ResultUtil.getSuccess();
    }


}
