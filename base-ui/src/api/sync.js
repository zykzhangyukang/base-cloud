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