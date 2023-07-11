package com.coderman.auth.service.role.impl;

import com.coderman.api.constant.ResultConstant;
import com.coderman.api.exception.BusinessException;
import com.coderman.api.util.PageUtil;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dao.func.FuncDAO;
import com.coderman.auth.dao.role.RoleDAO;
import com.coderman.auth.dao.role.RoleFuncDAO;
import com.coderman.auth.dao.user.UserDAO;
import com.coderman.auth.dao.user.UserRoleDAO;
import com.coderman.auth.dto.func.RoleAuthorizedDTO;
import com.coderman.auth.dto.role.RolePageDTO;
import com.coderman.auth.dto.role.RoleSaveDTO;
import com.coderman.auth.dto.role.RoleUpdateDTO;
import com.coderman.auth.model.func.FuncExample;
import com.coderman.auth.model.func.FuncModel;
import com.coderman.auth.model.role.RoleFuncExample;
import com.coderman.auth.model.role.RoleFuncModel;
import com.coderman.auth.model.role.RoleModel;
import com.coderman.auth.model.user.UserModel;
import com.coderman.auth.model.user.UserRoleExample;
import com.coderman.auth.model.user.UserRoleModel;
import com.coderman.auth.service.func.FuncService;
import com.coderman.auth.service.role.RoleService;
import com.coderman.auth.utils.TreeUtils;
import com.coderman.auth.vo.func.FuncTreeVO;
import com.coderman.auth.vo.role.RoleAssignVO;
import com.coderman.auth.vo.role.RoleAuthCheckVO;
import com.coderman.auth.vo.role.RoleAuthorizedInitVO;
import com.coderman.auth.vo.role.RoleVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author coderman
 * @Title: 角色服务实现
 * @date 2022/2/2711:58
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDAO roleDAO;

    @Resource
    private UserRoleDAO userRoleDAO;

    @Resource
    private UserDAO userDAO;

    @Resource
    private FuncService funcService;

    @Resource
    private FuncDAO funcDAO;

    @Resource
    private RoleFuncDAO roleFuncDAO;


    @Override
    @LogError(value = "角色列表")
    public ResultVO<PageVO<List<RoleVO>>> page(@LogErrorParam RolePageDTO rolePageDTO) {

        Integer currentPage = rolePageDTO.getCurrentPage();
        Integer pageSize = rolePageDTO.getPageSize();
        String roleName = rolePageDTO.getRoleName();

        Map<String, Object> conditionMap = new HashMap<>(1);

        if (StringUtils.isNotBlank(roleName)) {

            conditionMap.put("roleName", roleName);
        }

        PageUtil.getConditionMap(conditionMap, currentPage, pageSize);

        List<RoleVO> roleVOS = new ArrayList<>();

        Long count = this.roleDAO.countPage(conditionMap);

        if (count > 0) {

            roleVOS = this.roleDAO.page(conditionMap);
        }

        return ResultUtil.getSuccessPage(RoleVO.class, new PageVO<>(count, roleVOS, currentPage, pageSize));
    }

    @Override
    @Transactional
    public ResultVO<Void> save(RoleSaveDTO roleSaveDTO) {

        String roleName = roleSaveDTO.getRoleName();
        String roleDesc = roleSaveDTO.getRoleDesc();
        Date currentDate = new Date();

        if (StringUtils.isBlank(roleName)) {

            return ResultUtil.getWarn("角色名称不能为空！");
        }

        if (StringUtils.isBlank(roleDesc)) {

            return ResultUtil.getWarn("角色描述不能为空！");
        }

        if (StringUtils.length(roleName) > 15) {

            return ResultUtil.getWarn("角色名称最多15个字符！");
        }

        if (StringUtils.length(roleDesc) > 20) {

            return ResultUtil.getWarn("角色描述最多20个字符！");
        }

        // 角色名称唯一性校验
        RoleModel roleModel = this.roleDAO.selectByRoleName(roleName);

        if (Objects.nonNull(roleModel)) {

            return ResultUtil.getFail("存在重复的角色:" + roleName);
        }

        RoleModel insert = new RoleModel();
        insert.setRoleName(roleName);
        insert.setRoleDesc(roleDesc);
        insert.setCreateTime(currentDate);
        insert.setUpdateTime(currentDate);

        this.roleDAO.insertSelective(insert);

        return ResultUtil.getSuccess();
    }

    @Override
    @Transactional
    public ResultVO<Void> delete(Integer roleId) {

        // 查询当前角色是否有关联用户
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        long count = this.userRoleDAO.countByExample(example);

        if (count > 0) {

            return ResultUtil.getWarn("角色已关联用户 ！");
        }

        // 删除角色-功能关联
        RoleFuncExample roleFuncModelExample = new RoleFuncExample();
        roleFuncModelExample.createCriteria().andRoleIdEqualTo(roleId);
        this.roleFuncDAO.deleteByExample(roleFuncModelExample);

        // 删除角色
        this.roleDAO.deleteByPrimaryKey(roleId);
        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "更新角色")
    public ResultVO<Void> update(@LogErrorParam RoleUpdateDTO roleUpdateDTO) {

        Integer roleId = roleUpdateDTO.getRoleId();
        String roleName = roleUpdateDTO.getRoleName();
        String roleDesc = roleUpdateDTO.getRoleDesc();

        if (StringUtils.length(roleName) > 15) {

            return ResultUtil.getWarn("角色名称最多15个字符！");
        }

        if (StringUtils.isBlank(roleName)) {

            return ResultUtil.getWarn("角色名称不能为空！");
        }

        if (StringUtils.length(roleDesc) > 20) {

            return ResultUtil.getWarn("角色描述最多20个字符！");
        }

        if (StringUtils.isBlank(roleDesc)) {

            return ResultUtil.getWarn("角色描述不能为空！");
        }

        // 角色名称唯一性校验
        RoleModel roleModel = this.roleDAO.selectByRoleName(roleName);

        if (Objects.nonNull(roleModel) && !Objects.equals(roleModel.getRoleId(), roleId)) {

            return ResultUtil.getWarn("存在重复的角色:" + roleName);
        }

        // 更新角色
        RoleModel update = new RoleModel();
        update.setRoleId(roleId);
        update.setRoleName(roleName);
        update.setRoleDesc(roleDesc);
        update.setUpdateTime(new Date());
        this.roleDAO.updateByPrimaryKeySelective(update);

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "查询角色信息")
    public ResultVO<RoleVO> selectRoleById(Integer roleId) {

        RoleModel roleModel = this.roleDAO.selectByPrimaryKey(roleId);

        if (null == roleModel) {

            return ResultUtil.getWarn("角色不存在！");
        }

        RoleVO roleVO = new RoleVO();
        roleVO.setRoleDesc(roleModel.getRoleDesc());
        roleVO.setRoleId(roleModel.getRoleId());
        roleVO.setRoleName(roleModel.getRoleName());
        return ResultUtil.getSuccess(RoleVO.class, roleVO);
    }

    @LogError(value = "角色分配用户初始化")
    public ResultVO<RoleAssignVO> roleUserUpdateInit(Integer roleId) {

        RoleAssignVO roleAssignVO = new RoleAssignVO();

        RoleModel roleModel = this.roleDAO.selectByPrimaryKey(roleId);
        if (roleModel == null) {
            throw new BusinessException("需要分配的角色不存在!");
        }

        roleAssignVO.setRoleId(roleId);

        // 查询全部角色信息
        List<UserModel> userModelList = this.userDAO.selectByExample(null);
        roleAssignVO.setUserList(userModelList);

        // 查询角色已有的用户
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<UserRoleModel> userRoleModels = this.userRoleDAO.selectByExample(example);
        List<Integer> roleUserIds = userRoleModels.stream().map(UserRoleModel::getUserId).collect(Collectors.toList());

        roleAssignVO.setAssignedIdList(roleUserIds);

        return ResultUtil.getSuccess(RoleAssignVO.class, roleAssignVO);
    }

    @Override
    @LogError(value = "角色分配用户")
    public ResultVO<Void> roleUserUpdate(Integer roleId, List<Integer> assignedIdList) {

        RoleModel roleModel = this.roleDAO.selectByPrimaryKey(roleId);
        if (roleModel == null) {
            throw new BusinessException("需要分配的角色不存在!");
        }

        // 清空之前的权限
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        this.userRoleDAO.deleteByExample(example);


        // 批量新增
        if (CollectionUtils.isNotEmpty(assignedIdList)) {
            this.userRoleDAO.insertBatchByRoleId(roleId, assignedIdList);
        }

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "角色授权初始化")
    public ResultVO<RoleAuthorizedInitVO> roleAuthorizedInit(@LogErrorParam String roleIdStr) {

        Integer roleId = null;

        try {
            roleId = Integer.parseInt(roleIdStr);
        } catch (Exception ignored) {
        }

        if (Objects.isNull(roleId)) {

            return ResultUtil.getWarn("角色id不能为空！");
        }

        RoleModel roleModel = this.roleDAO.selectByPrimaryKey(roleId);
        if (Objects.isNull(roleModel)) {

            return ResultUtil.getFail("角色不存在！");
        }

        RoleAuthorizedInitVO roleAuthInitVO = new RoleAuthorizedInitVO();
        Map<Integer, Collection<Integer>> halfCheckedMap = new HashMap<>();
        Map<Integer, Collection<Integer>> allCheckedMap = new HashMap<>();

        // 功能树查询
        ResultVO<List<FuncTreeVO>> listResultVO = this.funcService.listTree();
        if (!ResultConstant.RESULT_CODE_200.equals(listResultVO.getCode())) {

            return ResultUtil.getWarn(listResultVO.getMsg());
        }

        List<FuncTreeVO> treeVOList = listResultVO.getResult();
        if (CollectionUtils.isEmpty(treeVOList)) {

            return ResultUtil.getWarn("暂无可分配的功能！");
        }

        // 查询该角色拥有的功能
        List<Integer> ownerFuncIdList = this.roleFuncDAO.selectAllByRoleId(roleId).stream()
                .map(RoleFuncModel::getFuncId).distinct().collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(ownerFuncIdList)) {
            for (FuncTreeVO funcTreeVO : treeVOList) {

                List<Integer> tempList = new ArrayList<>();
                List<Integer> tempList2 = new ArrayList<>();

                // 半选
                TreeUtils.getDeepFuncIdList(tempList, funcTreeVO);
                if (CollectionUtils.isNotEmpty(tempList)) {
                    Collection<Integer> checkedIdList = CollectionUtils.intersection(tempList, ownerFuncIdList);
                    halfCheckedMap.putIfAbsent(funcTreeVO.getFuncId(), checkedIdList);
                }

                // 全选
                TreeUtils.getAllFuncIdList(tempList2, funcTreeVO);
                if (CollectionUtils.isNotEmpty(tempList2)) {
                    Collection<Integer> checkedIdList = CollectionUtils.intersection(tempList2, ownerFuncIdList);
                    allCheckedMap.putIfAbsent(funcTreeVO.getFuncId(), checkedIdList);
                }
            }
        }


        roleAuthInitVO.setRoleId(roleModel.getRoleId());
        roleAuthInitVO.setRoleName(roleModel.getRoleName());
        roleAuthInitVO.setAllTreeList(treeVOList);
        roleAuthInitVO.setHalfCheckedMap(halfCheckedMap);
        roleAuthInitVO.setAllCheckedMap(allCheckedMap);
        return ResultUtil.getSuccess(RoleAuthorizedInitVO.class, roleAuthInitVO);
    }

    @Override
    @LogError(value = "角色分配功能")
    public ResultVO<Void> roleAuthorizedUpdate(RoleAuthorizedDTO roleAuthorizedDTO) {

        Integer roleId = null;

        try {
            roleId = Integer.parseInt(roleAuthorizedDTO.getRoleId());
        } catch (Exception ignored) {
        }

        if (Objects.isNull(roleId)) {
            return ResultUtil.getWarn("角色id不能为空！");
        }

        List<Integer> funcIdList = roleAuthorizedDTO.getFuncIdList();

        RoleModel roleModel = this.roleDAO.selectByPrimaryKey(roleId);
        if (null == roleModel) {

            return ResultUtil.getWarn("角色不存在！");
        }

        // 删除之前该角色拥有的功能
        this.roleFuncDAO.deleteByRoleId(roleId);

        // 插入角色-功能关联
        if (CollectionUtils.isNotEmpty(funcIdList)) {

            this.roleFuncDAO.batchInsertByRoleId(roleId, funcIdList);
        }

        return ResultUtil.getSuccess();
    }

    @Override
    public ResultVO<RoleAuthCheckVO> roleAuthorizedCheck(RoleAuthorizedDTO roleAuthorizedDTO) {

        Integer roleId = null;
        try {
            roleId = Integer.parseInt(roleAuthorizedDTO.getRoleId());
        } catch (Exception ignored) {
        }

        List<Integer> funcIdList = roleAuthorizedDTO.getFuncIdList();
        if (Objects.isNull(roleId)) {
            return ResultUtil.getWarn("角色ID不能为空！");
        }

        // 本次需要分配的功能查出来
        List<FuncModel> needAuthFuncKeyList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(funcIdList)) {

            FuncExample example = new FuncExample();
            example.createCriteria().andFuncIdIn(funcIdList);
            needAuthFuncKeyList = new ArrayList<>(this.funcDAO.selectByExample(example));
        }

        // 查出该角色原本有的功能
        ResultVO<List<FuncModel>> listResultVO = this.funcService.selectByRoleId(roleId);
        if (!ResultConstant.RESULT_CODE_200.equals(listResultVO.getCode())) {
            return ResultUtil.getWarn(listResultVO.getMsg());
        }

        List<FuncModel> historyAuthFuncList = listResultVO.getResult();

        // 取交集
        Collection<FuncModel> intersection = CollectionUtils.intersection(needAuthFuncKeyList, historyAuthFuncList);
        // 新增的
        Collection<FuncModel> addList = CollectionUtils.subtract(needAuthFuncKeyList, intersection);
        // 删除的
        Collection<FuncModel> delList = CollectionUtils.subtract(historyAuthFuncList, intersection);

        RoleAuthCheckVO checkVO = new RoleAuthCheckVO();
        checkVO.setInsertList(new ArrayList<>(addList));
        checkVO.setDelList(new ArrayList<>(delList));

        return ResultUtil.getSuccess(RoleAuthCheckVO.class, checkVO);
    }

}
