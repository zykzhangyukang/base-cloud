package com.coderman.auth.service.resc.impl;

import com.coderman.api.constant.ResultConstant;
import com.coderman.api.exception.BusinessException;
import com.coderman.api.util.PageUtil;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dao.func.FuncRescDAO;
import com.coderman.auth.dao.resc.RescDAO;
import com.coderman.auth.dto.resc.RescPageDTO;
import com.coderman.auth.dto.resc.RescSaveDTO;
import com.coderman.auth.dto.resc.RescUpdateDTO;
import com.coderman.auth.model.func.FuncRescExample;
import com.coderman.auth.model.resc.RescExample;
import com.coderman.auth.model.resc.RescModel;
import com.coderman.auth.service.resc.RescService;
import com.coderman.auth.vo.resc.RescVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author coderman
 * @date 2022/3/199:13
 */
@Service
public class RescServiceImpl implements RescService {


    @Resource
    private RescDAO rescDAO;


    @Resource
    private FuncRescDAO funcRescDAO;

    @Override
    @LogError(value = "资源列表")
    public ResultVO<PageVO<List<RescVO>>> page(@LogErrorParam RescPageDTO rescPageDTO) {

        Integer currentPage = rescPageDTO.getCurrentPage();
        Integer pageSize = rescPageDTO.getPageSize();
        String rescUrl = rescPageDTO.getRescUrl();
        String rescName = rescPageDTO.getRescName();
        String rescDomain = rescPageDTO.getRescDomain();
        String methodType = rescPageDTO.getMethodType();

        Map<String, Object> conditionMap = new HashMap<>(6);

        if (StringUtils.isNotBlank(rescUrl)) {
            conditionMap.put("rescUrl", rescUrl.trim());
        }

        if (StringUtils.isNotBlank(rescName)) {
            conditionMap.put("rescName", rescName.trim());
        }

        if (StringUtils.isNotBlank(rescDomain)) {
            conditionMap.put("rescDomain", rescDomain);
        }

        if (StringUtils.isNotBlank(methodType)) {
            conditionMap.put("methodType", methodType);
        }

        PageUtil.getConditionMap(conditionMap, currentPage, pageSize);

        Long count = this.rescDAO.countPage(conditionMap);

        List<RescVO> rescVOList = new ArrayList<>();

        if (count > 0) {

            rescVOList = this.rescDAO.page(conditionMap);
        }

        PageVO<List<RescVO>> pageVO = new PageVO<>(count, rescVOList, currentPage, pageSize);
        return ResultUtil.getSuccessPage(RescVO.class, pageVO);
    }

    @Override
    @LogError(value = "新增资源 ")
    public ResultVO<Void> save(@LogErrorParam RescSaveDTO rescSaveDTO) {

        String rescName = rescSaveDTO.getRescName();
        String rescUrl = rescSaveDTO.getRescUrl();
        String rescDomain = rescSaveDTO.getRescDomain();
        String methodType = rescSaveDTO.getMethodType();

        if (StringUtils.isBlank(rescName) || StringUtils.length(rescName) > 20) {

            return ResultUtil.getWarn("资源名称不能为空,且在20个字符以内！");
        }

        if (StringUtils.isBlank(rescUrl) || StringUtils.length(rescUrl) > 50) {

            return ResultUtil.getWarn("资源url不能为空,且在50个字符以内！");
        }

        if (StringUtils.isBlank(rescDomain)) {

            return ResultUtil.getWarn("所属项目不能为空！");
        }

        if (StringUtils.isBlank(methodType)) {

            return ResultUtil.getWarn("请求方式不能为空！");
        }

        // 资源url唯一性校验
        RescVO rescVO = this.rescDAO.selectByRescUrl(rescUrl);
        if (Objects.nonNull(rescVO)) {

            return ResultUtil.getWarn("资源: [ " + rescUrl + " ],已经存在！");
        }

        RescModel insert = new RescModel();
        insert.setCreateTime(new Date());
        insert.setUpdateTime(new Date());
        insert.setRescDomain(rescDomain);
        insert.setRescUrl(rescUrl.trim());
        insert.setRescName(rescName);
        insert.setMethodType(methodType);

        this.rescDAO.insertSelective(insert);
        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "更新资源")
    public ResultVO<Void> update(@LogErrorParam RescUpdateDTO rescUpdateDTO) {

        String rescName = rescUpdateDTO.getRescName();
        Integer rescId = rescUpdateDTO.getRescId();
        String rescUrl = rescUpdateDTO.getRescUrl();
        String rescDomain = rescUpdateDTO.getRescDomain();
        String methodType = rescUpdateDTO.getMethodType();

        if (Objects.isNull(rescId)) {

            return ResultUtil.getWarn("资源id不能为空！");
        }

        if (StringUtils.isBlank(rescDomain)) {

            return ResultUtil.getWarn("所属项目不能为空！");
        }

        if (StringUtils.isBlank(rescName) || StringUtils.length(rescName) > 20) {

            return ResultUtil.getWarn("资源名称不能为空,且在20个字符以内！");
        }

        if (StringUtils.isBlank(rescUrl) || StringUtils.length(rescUrl) > 50) {

            return ResultUtil.getWarn("资源url不能为空,且在50个字符以内！");
        }

        if (StringUtils.isBlank(methodType)) {

            return ResultUtil.getWarn("请求方式不能为空！");
        }

        RescVO rescVO = this.rescDAO.selectByRescUrl(rescUrl);

        if (Objects.nonNull(rescVO) && !rescId.equals(rescVO.getRescId())) {

            return ResultUtil.getFail("资源: [ " + rescUrl + " ],已经存在！");
        }

        RescModel update = new RescModel();
        update.setRescId(rescId);
        update.setRescName(rescName);
        update.setRescDomain(rescDomain);
        update.setRescUrl(rescUrl);
        update.setUpdateTime(new Date());
        update.setMethodType(methodType);

        this.rescDAO.updateByPrimaryKeySelective(update);
        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "删除资源")
    public ResultVO<Void> delete(Integer rescId) {

        if (Objects.isNull(rescId)) {

            return ResultUtil.getWarn("资源id不能为空！");
        }

        // 校验该资源是否绑定了功能.
        Long count = this.funcRescDAO.countByRescId(rescId);

        if (count > 0) {

            return ResultUtil.getWarn("资源已被功能引用 ！");
        }

        this.rescDAO.deleteByPrimaryKey(rescId);

        return ResultUtil.getSuccess();
    }


    @Override
    @LogError(value = "查询资源信息")
    public ResultVO<RescVO> selectById(Integer rescId) {

        if (Objects.isNull(rescId)) {

            return ResultUtil.getWarn("资源id不能为空！");
        }

        RescModel resourceModel = this.rescDAO.selectByPrimaryKey(rescId);

        if (Objects.isNull(resourceModel)) {

            return ResultUtil.getWarn("资源不存在！");
        }

        RescVO rescVO = new RescVO();
        rescVO.setRescId(resourceModel.getRescId());
        rescVO.setRescName(resourceModel.getRescName());
        rescVO.setRescUrl(resourceModel.getRescUrl());
        rescVO.setRescDomain(resourceModel.getRescDomain());
        rescVO.setMethodType(resourceModel.getMethodType());

        return ResultUtil.getSuccess(RescVO.class, rescVO);
    }


    @Override
    public ResultVO<List<RescVO>> search(String keyword) {
        List<RescVO> rescVOList = this.rescDAO.selectByKeyword(keyword);
        return ResultUtil.getSuccessList(RescVO.class, rescVOList);
    }

    @Override
    public List<RescVO> selectRescListByUsername(String username) {
        return this.rescDAO.selectRescListByUsername(username);
    }

    @Override
    public ResultVO<Map<String, Set<Integer>>> getSystemAllRescMap(String project) {

        Map<String, Set<Integer>> map = new HashMap<>();

        RescExample example = new RescExample();

        if (StringUtils.isNotBlank(project)) {
            example.createCriteria().andRescDomainEqualTo(project);
        }

        List<RescModel> resourceModels = this.rescDAO.selectByExample(example);
        Set<Integer> rescIds;

        for (RescModel resc : resourceModels) {


            if (map.containsKey(resc.getRescUrl())) {

                rescIds = map.get(resc.getRescUrl());
            } else {

                rescIds = new HashSet<>();
            }


            rescIds.add(resc.getRescId());
            map.put(resc.getRescUrl(), rescIds);

        }

        ResultVO<Map<String, Set<Integer>>> resultVO = new ResultVO<>();
        resultVO.setCode(ResultConstant.RESULT_CODE_200);
        resultVO.setResult(map);

        return resultVO;
    }
}
