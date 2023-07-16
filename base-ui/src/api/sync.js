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