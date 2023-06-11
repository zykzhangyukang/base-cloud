import request from '../utils/request'

export const sendLogin = (data) => request({url: `/auth/user/login`, method: 'post', data});
export const sendLogout = (data) => request({url: `/auth/user/logout`, method: 'post', data});
export const sendUserInfo = (data) => request({url: `/auth/user/info`, method: 'post', data});
