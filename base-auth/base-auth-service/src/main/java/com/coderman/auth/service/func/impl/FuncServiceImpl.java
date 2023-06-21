package com.coderman.auth.service.func.impl;

import com.coderman.api.constant.ResultConstant;
import com.coderman.api.exception.BusinessException;
import com.coderman.api.util.PageUtil;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.constant.AuthConstant;
import com.coderman.auth.dao.func.FuncDAO;
import com.coderman.auth.dao.func.FuncRescDAO;
import com.coderman.auth.dao.role.RoleFuncDAO;
import com.coderman.auth.dao.user.UserRoleDAO;
import com.coderman.auth.dto.func.*;
import com.coderman.auth.model.func.FuncExample;
import com.coderman.auth.model.func.FuncModel;
import com.coderman.auth.model.func.FuncRescExample;
import com.coderman.auth.model.resc.RescModel;
import com.coderman.auth.model.role.RoleFuncExample;
import com.coderman.auth.model.role.RoleFuncModel;
import com.coderman.auth.model.user.UserRoleExample;
import com.coderman.auth.model.user.UserRoleModel;
import com.coderman.auth.service.func.FuncService;
import com.coderman.auth.vo.func.FuncTreeVO;
import com.coderman.auth.vo.func.FuncVO;
import com.coderman.auth.vo.func.MenuVO;
import com.coderman.auth.vo.resc.RescVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import org.apache.commons.collections4.CollectionUtils;
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

        // 获取所有功能转成VO
        List<FuncTreeVO> voList = this.funcDAO.selectAllFuncTreeVO();

        Map<Integer, FuncTreeVO> funcVOMap = voList.stream()
                .collect(Collectors.toMap(FuncTreeVO::getFuncId, e -> e));

        for (FuncTreeVO treeVo : voList) {

            Integer parentId = treeVo.getParentId();

            if (funcVOMap.containsKey(parentId)) {

                FuncTreeVO funcTreeVO = funcVOMap.get(parentId);
                List<FuncTreeVO> childrenList = Optional.ofNullable(funcTreeVO.getChildrenList()).orElse(new ArrayList<>());
                funcTreeVO.setChildrenList(childrenList);

                // 添加子节点并进行排序
                childrenList.add(treeVo);
                childrenList.sort(Comparator.comparing(FuncTreeVO::getFuncSort));
            }
        }

        // 设置根节点
        List<FuncTreeVO> rootFunVoList = voList.stream()
                .filter(e -> !funcVOMap.containsKey(e.getParentId())).collect(Collectors.toList());
        return ResultUtil.getSuccessList(FuncTreeVO.class, rootFunVoList);
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

        if (Objects.isNull(currentPage)) {

            currentPage = 1;
        }

        if (Objects.isNull(pageSize)) {

            pageSize = 20;
        }

        if (StringUtils.isNotBlank(funcName)) {
            conditionMap.put("funcName", funcName);
        }

        if (StringUtils.isNotBlank(funcType)) {
            conditionMap.put("funcType", funcType);
        }

        if (StringUtils.isNotBlank(funcDirStatus)) {
            conditionMap.put("funcDirStatus", funcDirStatus);
        }

        if (StringUtils.isNotBlank(funcKey)) {
            conditionMap.put("funcKey", funcKey);
        }

        if (StringUtils.isNotBlank(rescUrl)) {
            conditionMap.put("rescUrl", rescUrl);
        }

        if (Objects.nonNull(parentId)) {

            conditionMap.put("parentId", parentId);
        }

        PageUtil.getConditionMap(conditionMap, currentPage, pageSize);

        Long count = this.funcDAO.countPage(conditionMap);

        List<FuncVO> funcVOList = new ArrayList<>();

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

        if (parentId == null) {

            return ResultUtil.getWarn("父级功能不能为空！");
        }

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

        if (AuthConstant.FUNC_TYPE_DIR.equals(funcType) && StringUtils.isBlank(funcDirStatus)) {

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
        insert.setParentId(parentId);
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

            throw new BusinessException("存在重复的功能key！");
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

        if(Objects.isNull(funcId)){

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
    public ResultVO<List<MenuVO>> selectMenusTreeByUserId(@LogErrorParam Integer userId) {

        // 获取所有的菜单类型的功能
        List<MenuVO> allMenus = this.funcDAO.selectAllMenusByUserId(userId);

        // 转成Map结构
        Map<Integer, MenuVO> funcVOMap = allMenus.stream().collect(Collectors.toMap(MenuVO::getFuncId, e -> e));
        for (MenuVO menuVO : allMenus) {

            menuVO.setHidden(AuthConstant.FUNC_DIR_STATUS_HIDE.equals(menuVO.getFuncDirStatus()));

            Integer parentId = menuVO.getParentId();

            if (funcVOMap.containsKey(parentId)) {

                MenuVO menuNode = funcVOMap.get(parentId);
                List<MenuVO> childrenList = Optional.ofNullable(menuNode.getChildren()).orElse(new ArrayList<>());
                menuNode.setChildren(childrenList);

                // 添加子节点并进行排序
                childrenList.add(menuVO);
                childrenList.sort(Comparator.comparingInt(MenuVO::getFuncSort));
            }
        }

        // 获取所有父级节点
        List<MenuVO> rootFunVoList = allMenus.stream()
                .sorted(Comparator.comparingInt(MenuVO::getFuncSort))
                .filter(e -> !funcVOMap.containsKey(e.getParentId())).collect(Collectors.toList());
        return ResultUtil.getSuccessList(MenuVO.class, rootFunVoList);
    }


    @Override
    @LogError(value = "查询功能按钮key")
    public ResultVO<List<String>> selectFuncKeyListByUserId(@LogErrorParam Integer userId) {
        List<String> funcKeys = this.funcDAO.selectFuncKeyListByUserId(userId);
        return ResultUtil.getSuccessList(String.class, funcKeys);
    }

    @Override
    @LogError(value = "功能设置资源")
    public ResultVO<Void> updateResourceBind(@LogErrorParam FuncUpdateRescBindDTO rescBindDTO) {

        List<FuncUpdateRescBindDTO.RescBindItem> rescVOList = rescBindDTO.getRescVOList();
        Integer funcId = rescBindDTO.getFuncId();

        if(Objects.isNull(funcId)){

            return ResultUtil.getWarn("资源id不能为空！");
        }

        this.funcRescDAO.deleteByFuncId(funcId);

        if(CollectionUtils.isNotEmpty(rescVOList)){

            List<Integer> rescIdList = rescVOList.stream().map(FuncUpdateRescBindDTO.RescBindItem::getKey).distinct()
                    .collect(Collectors.toList());

            this.funcRescDAO.insertBatchByFuncId(funcId,rescIdList);
        }

        return ResultUtil.getSuccess();
    }
}