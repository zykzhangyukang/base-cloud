import http from "@/utils/request";

/**
 * 同步计划列表
 */
export const syncPlanPage = data => {
    return http({
        url: `/sync/plan/page`,
        method: 'post',
        data: data
    })
}


/**
 * 同步计划详情
 */
export const syncPlanDetail = uuid => {
    return http({
        url: `/sync/plan/detail?uuid=${uuid}`,
        method: 'get',
    })
}


/**
 * 删除同步计划
 */
export const syncPlanDelete = uuid => {
    return http({
        url: `/sync/plan/delete?uuid=${uuid}`,
        method: 'delete',
    })
}

/**
 * 新增同步计划
 */
export const syncPlanUpdate = data => {
    return http({
        url: `/sync/plan/update`,
        method: 'put',
        data: data
    })
}

/**
 * 新增同步计划
 */
export const syncPlanSave = data => {
    return http({
        url: `/sync/plan/save`,
        method: 'post',
        data: data
    })
}