import http from "@/utils/request";

/**
 * 同步回调列表
 */
export const syncCallbackRepeat = data => {
    return http({
        url: `/sync/callback/repeat`,
        method: 'post',
        data: data
    })
}

/**
 * 同步回调列表
 */
export const syncCallbackPage = data => {
    return http({
        url: `/sync/callback/page`,
        method: 'post',
        data: data
    })
}

/**
 * 同步消息列表
 */
export const syncMsgPage = data => {
    return http({
        url: `/sync/message/page`,
        method: 'post',
        data: data
    })
}


/**
 * 数据校验
 */
export const syncResultValidData = data => {
    return http({
        url: `/sync/result/valid/data`,
        method: 'post',
        data
    })
}


/**
 * 重新同步记录
 */
export const syncResultRepeatSync = uuid => {
    return http({
        url: `/sync/result/repeat/sync?uuid=${uuid}`,
        method: 'put',
    })
}

/**
 * 标记同步记录成功
 */
export const syncResultSignSuccess = uuid => {
    return http({
        url: `/sync/result/sign/success?uuid=${uuid}`,
        method: 'put',
    })
}

/**
 * 同步结果列表
 */
export const syncResultPage = data => {
    return http({
        url: `/sync/result/page`,
        method: 'post',
        data: data
    })
}


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
 * 刷新同步计划
 */
export const syncPlanRefresh = data => {
    return http({
        url: `/sync/plan/refresh`,
        method: 'put',
        data: data
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
 * 更新同步计划
 */
export const syncPlanUpdate = data => {
    return http({
        url: `/sync/plan/update`,
        method: 'put',
        data: data
    })
}

/**
 * 更新同步计划状态
 */
export const syncPlanUpdateStatus = data => {
    return http({
        url: `/sync/plan/update/status`,
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