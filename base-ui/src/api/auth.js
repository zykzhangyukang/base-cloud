import http from '../utils/request'

/************************************ 资源 *********************************/

/**
 * 根据id获取资源信息
 */
export const authRescSelectById = (id) => {
    return http({
        url: `/auth/resc/${id}`,
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
        url: `/auth/resc/delete/${id}`,
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




/************************************ 角色 *********************************/

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
        url: `/auth/role/${id}`,
        method: 'get',
    })
}

/**
 * 删除角色
 */
export const authRoleDelete = id => {
    return http({
        url: `/auth/role/delete/${id}`,
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



/************************************ 用户 *********************************/

/**
 *  用户分配角色初始化
 */
export const authUserAssignUpdate = (data) => {
    return http({
        url: `/auth/user/assign/update`,
        method: 'put',
        data
    })
}


/**
 *  用户分配角色初始化
 */
export const authUserAssignInit = (id) => {
    return http({
        url: `/auth/user/${id}/assign/init`,
        method: 'get',
    })
}

/**
 * 根据id获取用户信息
 */
export const authUserUpdatePwd = (data) => {
    return http({
        url: `/auth/user/update/password`,
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
        url: `/auth/user/${id}`,
        method: 'get',
    })
}

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