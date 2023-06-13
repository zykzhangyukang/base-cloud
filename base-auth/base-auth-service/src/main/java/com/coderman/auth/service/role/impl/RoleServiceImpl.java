package com.coderman.auth.service.role.impl;

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
import com.coderman.auth.dto.role.RolePageDTO;
import com.coderman.auth.dto.role.RoleSaveDTO;
import com.coderman.auth.dto.role.RoleUpdateDTO;
import com.coderman.auth.model.func.FuncExample;
import com.coderman.auth.model.func.FuncModel;
import com.coderman.auth.model.role.RoleExample;
import com.coderman.auth.model.role.RoleFuncExample;
import com.coderman.auth.model.role.RoleFuncModel;
import com.coderman.auth.model.role.RoleModel;
import com.coderman.auth.model.user.UserModel;
import com.coderman.auth.model.user.UserRoleExample;
import com.coderman.auth.model.user.UserRoleModel;
import com.coderman.auth.service.func.FuncService;
import com.coderman.auth.service.role.RoleService;
import com.coderman.auth.vo.func.FuncTreeVO;
import com.coderman.auth.vo.func.FuncVO;
import com.coderman.auth.vo.role.RoleAssignVO;
import com.coderman.auth.vo.role.RoleAuthCheckVO;
import com.coderman.auth.vo.role.RoleAuthInitVO;
import com.coderman.auth.vo.role.RoleVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

        if (Objects.isNull(currentPage)) {
            currentPage = 1;
        }

        if (Objects.isNull(pageSize)) {
            pageSize = 20;
        }

        if (StringUtils.isNotBlank(roleName)) {

            conditionMap.put("roleName", roleName);
        }

        PageUtil.getConditionMap(conditionMap,currentPage,pageSize);

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

            return ResultUtil.getWarn("当前角色已关联用户无法删除");
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

        if (Objects.nonNull(roleModel) && !Objects.equals(roleModel.getRoleId(),roleId)) {

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
    public ResultVO<RoleAuthInitVO> roleFuncUpdateInit(Integer roleId) {

        RoleAuthInitVO roleAuthInitVO = new  RoleAuthInitVO();

        RoleModel roleModel = this.roleDAO.selectByPrimaryKey(roleId);
        if(null == roleModel){
            throw new BusinessException("角色不存在！");
        }

        // 获取所有功能
        ResultVO<FuncVO> funcVOResultVO = this.funcService.listTree();
        List<FuncTreeVO> treeVOList = funcVOResultVO.getResult().getFuncTreeVOList();
        List<FuncTreeVO> funcVOList = funcVOResultVO.getResult().getFuncVOList();

        // 查询该角色拥有的功能
        RoleFuncExample example = new RoleFuncExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<Integer> hasFuncIdList = this.roleFuncDAO.selectByExample(example).stream().map(RoleFuncModel::getFuncId).collect(Collectors.toList());


        // 功能key
        List<String> keyList = funcVOList.stream().filter(e -> hasFuncIdList.contains(e.getFuncId())).map(FuncTreeVO::getFuncKey).collect(Collectors.toList());

        roleAuthInitVO.setRoleId(roleModel.getRoleId());
        roleAuthInitVO.setRoleName(roleModel.getRoleName());
        roleAuthInitVO.setAllTreeList(treeVOList);
        roleAuthInitVO.setFuncKeyList(keyList);

        // 回显问题: vue-ant-design
        return ResultUtil.getSuccess(RoleAuthInitVO.class,roleAuthInitVO);
    }

    @Override
    @LogError(value = "角色分配功能")
    public ResultVO<Void> roleFuncUpdate(Integer roleId, List<String> funcKeyList) {

        RoleModel roleModel = this.roleDAO.selectByPrimaryKey(roleId);
        if(null == roleModel){
            throw new BusinessException("角色不存在！");
        }

        // 删除之前该角色拥有的功能
        RoleFuncExample example = new RoleFuncExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        this.roleFuncDAO.deleteByExample(example);

        // 插入角色-功能关联
        if(CollectionUtils.isNotEmpty(funcKeyList)){
            this.roleFuncDAO.batchInsertByRoleId(roleId,funcKeyList);
        }

        return ResultUtil.getSuccess();
    }

    @Override
    public ResultVO<RoleAuthCheckVO> authFuncCheck(Integer roleId, List<String> funcKeyList) {

        // param: [1,2,4] new
        // db: [1,2,3] old
        // 交集： [1,2]

        // 3: 删除 old- 交集 =  差集
        // 4: 新增 new - 新增


        // 本次需要分配的功能查出来
        List<FuncModel> needAuthFuncKeyList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(funcKeyList)) {

            FuncExample example = new FuncExample();
            example.createCriteria().andFuncKeyIn(funcKeyList);
            needAuthFuncKeyList = new ArrayList<>(this.funcDAO.selectByExample(example));
        }


        // 查出该角色原本有的功能
        List<FuncModel> historyAuthFuncList = new ArrayList<>();
        RoleFuncExample example = new RoleFuncExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<Integer> funcIds = this.roleFuncDAO.selectByExample(example).stream()
                .map(RoleFuncModel::getFuncId)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(funcIds)) {

            FuncExample funcModelExample = new FuncExample();
            funcModelExample.createCriteria().andFuncIdIn(funcIds);
            historyAuthFuncList = new ArrayList<>(this.funcDAO.selectByExample(funcModelExample));
        }

        // 取交集
        @SuppressWarnings("all")
        Collection<FuncModel> intersection = CollectionUtils.intersection(needAuthFuncKeyList, historyAuthFuncList);

        // 新增的
        @SuppressWarnings("all")
        Collection<FuncModel> addList = CollectionUtils.subtract(needAuthFuncKeyList, intersection);

        // 删除的
        @SuppressWarnings("all")
        Collection<FuncModel> delList = CollectionUtils.subtract(historyAuthFuncList, intersection);


        RoleAuthCheckVO checkVO = new RoleAuthCheckVO();
        checkVO.setInsertList(new ArrayList<>(addList));
        checkVO.setDelList(new ArrayList<>(delList));

        return ResultUtil.getSuccess(RoleAuthCheckVO.class,checkVO);
    }

}
