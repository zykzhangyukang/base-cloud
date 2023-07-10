package com.coderman.auth.service.func.impl;

import com.coderman.api.constant.ResultConstant;
import com.coderman.api.util.PageUtil;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.constant.AuthConstant;
import com.coderman.auth.dao.func.FuncDAO;
import com.coderman.auth.dao.func.FuncRescDAO;
import com.coderman.auth.dao.role.RoleFuncDAO;
import com.coderman.auth.dao.user.UserRoleDAO;
import com.coderman.auth.dto.func.FuncPageDTO;
import com.coderman.auth.dto.func.FuncSaveDTO;
import com.coderman.auth.dto.func.FuncUpdateDTO;
import com.coderman.auth.dto.func.FuncUpdateRescBindDTO;
import com.coderman.auth.model.func.FuncExample;
import com.coderman.auth.model.func.FuncModel;
import com.coderman.auth.model.role.RoleFuncExample;
import com.coderman.auth.model.role.RoleFuncModel;
import com.coderman.auth.model.user.UserRoleExample;
import com.coderman.auth.model.user.UserRoleModel;
import com.coderman.auth.service.func.FuncService;
import com.coderman.auth.utils.TreeUtils;
import com.coderman.auth.vo.func.FuncTreeVO;
import com.coderman.auth.vo.func.FuncVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author coderman
 * @Title: 功能服务
 * @Description: TOD
 * @date 2022/3/1915:40
 */
@Service
public class FuncServiceImpl implements FuncService {

    @Resource
    private FuncDAO funcDAO;

    @Resource
    private RoleFuncDAO roleFuncDAO;

    @Resource
    private FuncRescDAO funcRescDAO;

    @Resource
    private UserRoleDAO userRoleDAO;


    @Override
    @LogError(value = "获取功能树")
    public ResultVO<List<FuncTreeVO>> listTree() {

        List<FuncTreeVO> treeVOS = Lists.newArrayList();

        List<FuncTreeVO> funcTreeVos = this.funcDAO.selectAllFuncTreeVO();

        if (CollectionUtils.isNotEmpty(funcTreeVos)) {
            treeVOS = TreeUtils.buildFuncTreeByList(funcTreeVos);
        }

        return ResultUtil.getSuccessList(FuncTreeVO.class, treeVOS);
    }

    @Override
    @LogError(value = "功能列表")
    public ResultVO<PageVO<List<FuncVO>>> page(@LogErrorParam FuncPageDTO funcPageDTO) {

        Map<String, Object> conditionMap = new HashMap<>(5);
        String funcName = funcPageDTO.getFuncName();
        String funcKey = funcPageDTO.getFuncKey();
        String funcType = funcPageDTO.getFuncType();
        Integer parentId = funcPageDTO.getParentId();
        String funcDirStatus = funcPageDTO.getFuncDirStatus();
        String rescUrl = funcPageDTO.getRescUrl();

        Integer currentPage = funcPageDTO.getCurrentPage();
        Integer pageSize = funcPageDTO.getPageSize();

        if (StringUtils.isNotBlank(funcName)) {
            conditionMap.put("funcName", funcName.trim());
        }

        if (StringUtils.isNotBlank(funcType)) {
            conditionMap.put("funcType", funcType);
        }

        if (StringUtils.isNotBlank(funcDirStatus)) {
            conditionMap.put("funcDirStatus", funcDirStatus);
        }

        if (StringUtils.isNotBlank(funcKey)) {
            conditionMap.put("funcKey", funcKey.trim());
        }

        if (StringUtils.isNotBlank(rescUrl)) {
            conditionMap.put("rescUrl", rescUrl.trim());
        }

        if (Objects.nonNull(parentId)) {

            conditionMap.put("parentId", parentId);
        }

        List<FuncVO> funcVOList = new ArrayList<>();
        PageUtil.getConditionMap(conditionMap, currentPage, pageSize);

        Long count = this.funcDAO.countPage(conditionMap);
        if (count > 0) {

            funcVOList = this.funcDAO.page(conditionMap);
        }

        return ResultUtil.getSuccessPage(FuncVO.class, new PageVO<>(count, funcVOList, currentPage, pageSize));
    }

    @Override
    @LogError(value = "保存功能")
    public ResultVO<Void> save(@LogErrorParam FuncSaveDTO funcSaveDTO) {

        Integer parentId = funcSaveDTO.getParentId();
        String funcName = funcSaveDTO.getFuncName();
        String funcKey = funcSaveDTO.getFuncKey();
        String funcType = funcSaveDTO.getFuncType();
        Integer funcSort = funcSaveDTO.getFuncSort();
        String funcDirStatus = funcSaveDTO.getFuncDirStatus();
        String funcIcon = funcSaveDTO.getFuncIcon();

        if (StringUtils.isBlank(funcName) || StringUtils.length(funcName) > 15) {

            return ResultUtil.getWarn("功能名称不能为空，且在15个字符之内！");
        }

        if (StringUtils.isBlank(funcKey) || StringUtils.length(funcKey) > 30) {

            return ResultUtil.getWarn("功能key不能为空,且在30个字符之内！");
        }

        if (StringUtils.isBlank(funcType)) {

            return ResultUtil.getWarn("功能类型不能为空！");
        }

        if (funcSort == null || funcSort < 0 || funcSort > 100) {

            return ResultUtil.getWarn("功能排序不能为空，请输入0-100之间的整数！");
        }

        if (StringUtils.equals(AuthConstant.FUNC_TYPE_DIR, funcType) && StringUtils.isBlank(funcDirStatus)) {

            return ResultUtil.getWarn("请选择目录是显示还是隐藏！");
        }

        FuncModel funcModel = this.funcDAO.selectByFuncKey(funcKey);

        if (Objects.nonNull(funcModel)) {

            return ResultUtil.getWarn("存在重复功能key！");
        }

        FuncModel insert = new FuncModel();
        insert.setFuncKey(funcKey);
        insert.setFuncName(funcName);
        insert.setCreateTime(new Date());
        insert.setParentId(parentId == null ? 0 : parentId);
        insert.setFuncType(funcType);
        insert.setFuncSort(funcSort);
        insert.setFuncDirStatus(funcDirStatus);
        insert.setFuncIcon(funcIcon);
        insert.setUpdateTime(new Date());

        this.funcDAO.insertSelectiveReturnKey(insert);

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "更新功能")
    public ResultVO<Void> update(@LogErrorParam FuncUpdateDTO funcUpdateDTO) {

        Integer funcId = funcUpdateDTO.getFuncId();
        String funcName = funcUpdateDTO.getFuncName();
        String funcKey = funcUpdateDTO.getFuncKey();
        String funcType = funcUpdateDTO.getFuncType();
        Integer funcSort = funcUpdateDTO.getFuncSort();
        String funcDirStatus = funcUpdateDTO.getFuncDirStatus();
        String funcIcon = funcUpdateDTO.getFuncIcon();

        if(Objects.isNull(funcId)){

            return ResultUtil.getWarn("功能id不能为空！");
        }

        FuncModel funcModel = this.funcDAO.selectByPrimaryKey(funcId);
        if (null == funcModel) {

            return ResultUtil.getWarn("功能id不能为空！");
        }

        if (StringUtils.isBlank(funcKey) || StringUtils.length(funcKey) > 30) {

            return ResultUtil.getWarn("功能key不能为空,且在30个字符之内！");
        }

        if (StringUtils.isBlank(funcName) || StringUtils.length(funcName) > 15) {

            return ResultUtil.getWarn("功能名称不能为空，且在15个字符之内！");
        }

        if (funcSort == null || funcSort < 0 || funcSort > 100) {

            return ResultUtil.getWarn("功能排序不能为空，请输入0-100之间的整数！");
        }

        if (StringUtils.isBlank(funcType)) {

            return ResultUtil.getWarn("功能类型不能为空！");
        }

        if (AuthConstant.FUNC_TYPE_DIR.equals(funcType) && StringUtils.isBlank(funcDirStatus)) {

            return ResultUtil.getWarn("请选择目录是显示还是隐藏！");
        }

        // 功能key唯一性校验
        FuncModel dbFuncModel = this.funcDAO.selectByFuncKey(funcKey);

        if (Objects.nonNull(dbFuncModel) && !funcId.equals(dbFuncModel.getFuncId())) {

            return ResultUtil.getWarn("存在重复的功能key！");
        }

        // 更新功能
        FuncModel update = new FuncModel();
        update.setFuncId(funcId);
        update.setFuncKey(funcKey);
        update.setFuncName(funcName);
        update.setFuncType(funcType);
        update.setFuncSort(funcSort);
        update.setFuncDirStatus(funcDirStatus);
        update.setFuncIcon(funcIcon);
        update.setUpdateTime(new Date());
        this.funcDAO.updateByPrimaryKeySelective(update);

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "删除功能")
    public ResultVO<Void> delete(Integer funcId) {

        if (Objects.isNull(funcId)) {

            return ResultUtil.getWarn("功能id不能为空！");
        }

        FuncModel funcModel = this.funcDAO.selectByPrimaryKey(funcId);
        if (null == funcModel) {

            return ResultUtil.getWarn("功能不存在！");
        }

        // 校验功能是否存在子功能
        Long childrenCount = this.funcDAO.countChildrenByParentId(funcId);
        if (childrenCount > 0) {

            return ResultUtil.getWarn("功能存在子功能,无法删除！");
        }

        // 校验是否有功能-资源关联
        Long rescCount = this.funcRescDAO.countByFuncId(funcId);
        if (rescCount > 0) {

            return ResultUtil.getWarn("请先清空绑定的资源！");
        }

        // 校验是否有用户绑定了该功能
        RoleFuncExample roleFuncModelExample = new RoleFuncExample();
        roleFuncModelExample.createCriteria().andFuncIdEqualTo(funcId);
        List<Integer> roleIds = this.roleFuncDAO.selectByExample(roleFuncModelExample).stream().map(RoleFuncModel::getRoleId)
                .distinct().collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(roleIds)) {

            UserRoleExample userRoleModelExample = new UserRoleExample();
            userRoleModelExample.createCriteria().andRoleIdIn(roleIds);
            List<UserRoleModel> userRoleModels = this.userRoleDAO.selectByExample(userRoleModelExample);
            if (userRoleModels.size() > 0) {
                return ResultUtil.getWarn("功能已经授权给了用户,请先解绑用户！");
            }
        }

        // 删除功能
        this.funcDAO.deleteByPrimaryKey(funcId);
        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "获取功能")
    public ResultVO<FuncVO> selectById(@LogErrorParam Integer funcId) {

        if (Objects.isNull(funcId)) {

            return ResultUtil.getWarn("功能id不能为空！");
        }

        FuncVO funcVO = this.funcDAO.selectFuncInfo(funcId);
        if (null == funcVO) {

            return ResultUtil.getWarn("功能不存在！");
        }

        return ResultUtil.getSuccess(FuncVO.class, funcVO);
    }


    @Override
    @LogError(value = "功能解绑用户")
    public ResultVO<Void> deleteUserBind(@LogErrorParam Integer funcId) {

        // 递归查询出所有的功能id,包括子功能id
        List<Integer> funcIdList = new ArrayList<>();
        ResultVO<Void> resultVO = this.getDeepFuncIds(funcIdList, funcId);

        if (!ResultConstant.RESULT_CODE_200.equals(resultVO.getCode())) {

            return resultVO;
        }

        // 所谓功能解绑用户,即删除所有该功能-角色的绑定
        if (CollectionUtils.isNotEmpty(funcIdList)) {

            this.roleFuncDAO.deleteByFuncIdIn(funcIdList);
        }

        return ResultUtil.getSuccess();
    }

    /**
     * 递归查询出所有子功能
     *
     * @param funcIdList 功能id集合
     * @param rootFuncId 父级id
     * @return
     */
    private ResultVO<Void> getDeepFuncIds(List<Integer> funcIdList, Integer rootFuncId) {

        FuncModel rootNode = this.funcDAO.selectByPrimaryKey(rootFuncId);

        if (StringUtils.equals(rootNode.getFuncType(), AuthConstant.FUNC_TYPE_DIR)) {

            return ResultUtil.getWarn("目录功能不支持解绑用户！");
        }

        if (AuthConstant.FUNC_ROOT_PARENT_ID.equals(rootNode.getParentId())) {

            return ResultUtil.getWarn("不允许解绑最顶级的功能！");
        }

        funcIdList.add(rootFuncId);
        FuncExample example = new FuncExample();
        example.createCriteria().andParentIdEqualTo(rootFuncId);
        List<FuncModel> funcModels = this.funcDAO.selectByExample(example);

        if (!CollectionUtils.isEmpty(funcModels)) {

            for (FuncModel funcModel : funcModels) {

                getDeepFuncIds(funcIdList, funcModel.getFuncId());
            }
        }

        return ResultUtil.getSuccess();
    }


    @Override
    @LogError(value = "功能解绑资源")
    public ResultVO<Void> deleteResourceBind(@LogErrorParam Integer funcId) {

        if (Objects.isNull(funcId)) {

            return ResultUtil.getWarn("功能id不能为空！");
        }

        FuncVO funcVO = this.funcDAO.selectFuncInfo(funcId);
        if (null == funcVO) {

            return ResultUtil.getWarn("功能不存在");
        }

        // 所谓功能解绑资源,即删除所有该功能-资源的绑定
        this.funcRescDAO.deleteByFuncId(funcId);

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "查询菜单树")
    public ResultVO<List<FuncTreeVO>> selectMenusTreeByUserId(@LogErrorParam Integer userId) {

        if (Objects.isNull(userId)) {

            return ResultUtil.getWarn("用户id不能为空！");
        }

        // 查询目录类型的功能
        List<FuncTreeVO> treeVOs = new ArrayList<>();
        List<FuncTreeVO> funcTreeVOList = this.funcDAO.selectAllByUserIdAndFuncType(userId,AuthConstant.FUNC_TYPE_DIR);

        if (CollectionUtils.isNotEmpty(funcTreeVOList)) {

            treeVOs = TreeUtils.buildFuncTreeByList(funcTreeVOList);
        }

        return ResultUtil.getSuccessList(FuncTreeVO.class, treeVOs);
    }


    @Override
    @LogError(value = "查询功能按钮key")
    public ResultVO<List<String>> selectFuncKeyListByUserId(@LogErrorParam Integer userId) {

        if (Objects.isNull(userId)) {

            return ResultUtil.getWarn("用户id不能为空！");
        }

        List<String> buttonKeys = this.funcDAO.selectAllByUserIdAndFuncType(userId, AuthConstant.FUNC_TYPE_FUNC).stream()
                .map(FuncTreeVO::getFuncKey)
                .distinct().collect(Collectors.toList());

        return ResultUtil.getSuccessList(String.class, buttonKeys);
    }

    @Override
    @LogError(value = "功能设置资源")
    public ResultVO<Void> updateResourceBind(@LogErrorParam FuncUpdateRescBindDTO rescBindDTO) {

        List<FuncUpdateRescBindDTO.RescBindItem> rescVOList = rescBindDTO.getRescVOList();
        Integer funcId = rescBindDTO.getFuncId();

        if (Objects.isNull(funcId)) {

            return ResultUtil.getWarn("资源id不能为空！");
        }

        this.funcRescDAO.deleteByFuncId(funcId);

        if (CollectionUtils.isNotEmpty(rescVOList)) {

            List<Integer> rescIdList = rescVOList.stream().map(FuncUpdateRescBindDTO.RescBindItem::getKey).distinct()
                    .collect(Collectors.toList());

            this.funcRescDAO.insertBatchByFuncId(funcId, rescIdList);
        }

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "根据角色id查询功能列表")
    public ResultVO<List<FuncModel>> selectByRoleId(@LogErrorParam Integer roleId) {

        if(Objects.isNull(roleId)){
            return ResultUtil.getWarn("角色id不能为空！");
        }

        List<FuncModel> funcModelList  = this.funcDAO.selectByRoleId(roleId);
        return ResultUtil.getSuccessList(FuncModel.class,funcModelList);
    }
}