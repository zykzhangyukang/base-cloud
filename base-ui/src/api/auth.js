import http from '../utils/request'

/**
 * 启用用户
 */
export const authUserEnable = (id) => {
    return http({
        url: `/auth/user/${id}/enable`,
        method: 'put',
    })
}


/**
 * 禁用用户
 */
export const authUserDisable = (id) => {
    return http({
        url: `/auth/user/${id}/disable`,
        method: 'put',
    })
}


/**
 * 删除用户
 */
export const authDeptList = param => {
    return http({
        url: `/auth/dept/list`,
        method: 'get',
        param
    })
}

/**
 * 删除用户
 */
export const authUserDelete = id => {
    return http({
        url: `/auth/user/delete/${id}`,
        method: 'delete',
    })
}

/**
 * 新增用户
 */
export const authUserSave = data => {
    return http({
        url: `/auth/user/save`,
        method: 'post',
        data
    })
}


/**
 * 加载常量
 */
export const authConstAll = data => {
    return http({
        url: `/auth/const/all`,
        method: 'get',
    })
}

/**
 * 用户登录
 */
export const authUserLogin = data => {
    return http({
        url: `/auth/user/login`,
        method: 'post',
        data: data
    })
}

/**
 * 用户列表
 */
export const authUserPage = data => {
    return http({
        url: `/auth/user/page`,
        method: 'post',
        data: data
    })
}

/**
 * 退出登录
 */
export const authUserLogout = data => {
    return http({
        url: `/auth/user/logout`,
        method: 'post',
        data: data
    })
}

/**
 * 用户信息
 */
export const authUserInfo = data => {
    return http({
        url: `/auth/user/info`,
        method: 'post',
        data: data
    })
}
