package com.coderman.sync.service.plan;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.sync.dto.PlanPageDTO;
import com.coderman.sync.dto.PlanSaveDTO;
import com.coderman.sync.dto.PlanUpdateDTO;
import com.coderman.sync.dto.PlanUpdateStatusDTO;
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
    ResultVO<PlanVO> selectPlanByUuid(String uuid);


    /**
     * 同步计划更新
     *
     * @param planUpdateDTO 参数对象
     * @return
     */
    ResultVO<Void> update(PlanUpdateDTO planUpdateDTO);


    /**
     * 启用/禁用 同步内容
     *
     * @param planUpdateStatusDTO uuid
     * @return
     */
    ResultVO<Void> updateStatus(PlanUpdateStatusDTO planUpdateStatusDTO);


    /**
     * 同步计划新增
     *
     * @param planSaveDTO 参数对象
     * @return
     */
    ResultVO<Void> save(PlanSaveDTO planSaveDTO);


    /**
     * 同步计划删除
     *
     * @param uuid uuid
     * @return
     */
    ResultVO<Void> delete(String uuid);

    /**
     * 刷新同步计划
     * @return
     */
    ResultVO<Void> refreshSyncPlan();
}
