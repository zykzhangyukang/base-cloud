import http from '../utils/request'


/**
 * 根据id获取资源信息
 */
export const authFuncSelectById = (id) => {
    return http({
        url: `/auth/func/detail?funcId=${id}`,
        method: 'get',
    })
}


/**
 *  功能解绑用户
 */
export const autFuncUserBindDelete = (funcId) => {
    return http({
        url: `/auth/func/user/remove?funcId=${funcId}`,
        method: 'put',
    })
}

/**
 * 功能解绑资源
 */
export const authFuncRescBindDelete = (funcId) => {
    return http({
        url: `/auth/func/resc/remove?funcId=${funcId}`,
        method: 'delete',
    })
}


/**
 *更新功能信息
 */
export const authFuncRescBindUpdate = (data) => {
    return http({
        url: `/auth/func/resc/update`,
        method: 'put',
        data
    })
}

/**
 *更新功能信息
 */
export const authFuncUpdate = (data) => {
    return http({
        url: `/auth/func/update`,
        method: 'put',
        data
    })
}


/**
 * 删除功能
 */
export const authFuncDelete = id => {
    return http({
        url: `/auth/func/delete?funcId=${id}`,
        method: 'delete',
    })
}

/**
 * 新增功能
 */
export const authFucSave = data => {
    return http({
        url: `/auth/func/save`,
        method: 'post',
        data
    })
}

/**
 * 功能列表
 */
export const authFuncPage = data => {
    return http({
        url: `/auth/func/page`,
        method: 'post',
        data: data
    })
}

/**
 * 功能列表
 */
export const authFuncTree = data => {
    return http({
        url: `/auth/func/tree`,
        method: 'get',
        data: data
    })
}


/**
 * 根据关键字获取资源信息
 */
export const authRescSearchByKeyword = (keyword) => {
    return http({
        url: `/auth/resc/search?keyword=${keyword}`,
        method: 'get',
    })
}


/**
 * 根据id获取资源信息
 */
export const authRescSelectById = (id) => {
    return http({
        url: `/auth/resc/detail?rescId=${id}`,
        method: 'get',
    })
}

/**
 * 更新资源
 */
export const authRescUpdate = (data) => {
    return http({
        url: `/auth/resc/update`,
        method: 'put',
        data
    })
}


/**
 * 删除资源
 */
export const authRescDelete = id => {
    return http({
        url: `/auth/resc/delete?rescId=${id}`,
        method: 'delete',
    })
}

/**
 * 新增资源
 */
export const authRescSave = data => {
    return http({
        url: `/auth/resc/save`,
        method: 'post',
        data
    })
}


/**
 * 资源列表
 */
export const authRescPage = data => {
    return http({
        url: `/auth/resc/page`,
        method: 'post',
        data: data
    })
}


/**
 * 更新角色信息
 */
export const authRoleUpdate = (data) => {
    return http({
        url: `/auth/role/update`,
        method: 'put',
        data
    })
}

/**
 * 根据id获取角色信息
 */
export const authRoleSelectById = (id) => {
    return http({
        url: `/auth/role/detail?roleId=${id}`,
        method: 'get',
    })
}

/**
 * 角色授权初始化
 */
export const authRoleAuthorizedInit = (id) => {
    return http({
        url: `/auth/role/func/init?roleId=${id}`,
        method: 'get',
    })
}

/**
 * 角色授权预校验
 */
export const authRoleAuthorizedCheck = (data) => {
    return http({
        url: `/auth/role/func/check`,
        method: 'post',
        data
    })
}

/**
 * 角色授权初始化
 */
export const authRoleAuthorizedUpdate = (data) => {
    return http({
        url: `/auth/role/func/update`,
        method: 'put',
        data
    })
}


/**
 * 删除角色
 */
export const authRoleDelete = id => {
    return http({
        url: `/auth/role/delete?roleId=${id}`,
        method: 'delete',
    })
}

/**
 * 新增角色
 */
export const authRoleSave = data => {
    return http({
        url: `/auth/role/save`,
        method: 'post',
        data
    })
}

/**
 * 角色列表
 */
export const authRolePage = data => {
    return http({
        url: `/auth/role/page`,
        method: 'post',
        data: data
    })
}


/**
 *  用户分配角色初始化
 */
export const authUserRoleUpdate = (data) => {
    return http({
        url: `/auth/user/role/update`,
        method: 'put',
        data
    })
}


/**
 *  用户分配角色初始化
 */
export const authUserRoleInit = (id) => {
    return http({
        url: `/auth/user/role/init?userId=${id}`,
        method: 'get',
    })
}

/**
 * 根据id获取用户信息
 */
export const authUserUpdatePwd = (data) => {
    return http({
        url: `/auth/user/pwd/update`,
        method: 'put',
        data
    })
}


/**
 *更新角色信息
 */
export const authUserUpdate = (data) => {
    return http({
        url: `/auth/user/update`,
        method: 'put',
        data
    })
}


/**
 * 根据id获取用户信息
 */
export const authUserSelectById = (id) => {
    return http({
        url: `/auth/user/detail?userId=${id}`,
        method: 'get',
    })
}

/**
 * 启用用户
 */
export const authUserEnable = (id) => {
    return http({
        url: `/auth/user/enable?userId=${id}`,
        method: 'put',
    })
}


/**
 * 禁用用户
 */
export const authUserDisable = (id) => {
    return http({
        url: `/auth/user/disable?userId=${id}`,
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
        url: `/auth/user/delete?userId=${id}`,
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
export const authConstAll = () => {
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
 * 根据id获取资源信息
 */
export const authUserRefreshLogin = () => {
    return http({
        url: `/auth/user/refresh/login`,
        method: 'post',
    })
}

/**
 * 根据id获取资源信息
 */
export const authUserSwitchLogin = (data) => {
    return http({
        url: `/auth/user/switch/login`,
        method: 'post',
        data
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
        method: 'get'
    })
}
