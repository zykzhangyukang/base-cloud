package com.coderman.sync.service.plan;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.sync.dto.PlanPageDTO;
import com.coderman.sync.vo.PlanVO;

import java.util.List;

public interface PlanService {

    /**
     * 同步计划分页查询
     *
     * @param planPageDTO 查询参数
     * @return
     */
    ResultVO<PageVO<List<PlanVO>>> page(PlanPageDTO planPageDTO);


    /**
     * 查看同步内容
     *
     * @param uuid uuid
     * @return
     */
    ResultVO<String> selectContent(String uuid);


    /**
     * 同步计划更新
     *
     * @param planVO 参数对象
     * @return
     */
    ResultVO<Void> updatePlan(PlanVO planVO);


    /**
     * 启用/禁用 同步内容
     *
     * @param uuid uuid
     * @return
     */
    ResultVO<Void> updateStatus(String uuid);


    /**
     * 同步计划新增
     *
     * @param planVO 参数对象
     * @return
     */
    ResultVO<Void> savePlan(PlanVO planVO);


    /**
     * 同步计划删除
     *
     * @param uuid uuid
     * @return
     */
    ResultVO<Void> deletePlan(String uuid);
}
