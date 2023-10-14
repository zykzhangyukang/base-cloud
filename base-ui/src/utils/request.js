import axios from 'axios'
import {message} from 'ant-design-vue'
import router from "@/routers";

const http = axios.create({
    baseURL: process.env.VUE_APP_API,
    timeout: 20000,
    headers: {
        post: {
            'Content-Type': 'application/json;charset=utf-8'
        }
    }
})

http.interceptors.request.use(
    config => {
        config.headers['Authorization'] = localStorage.getItem('token') || '';
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

http.interceptors.response.use(
    (response) => {

        if (response.data.code === 200) {

            return Promise.resolve(response.data);

        } else if (response.data.code === 405 || response.data.code === 429) {

            message.warn(response.data.msg);

        } else {
            message.error(response.data.msg ? response.data.msg : '接口其他错误！');
        }

        return Promise.reject(response.data);
    },
    (error) => {

        if (!error.response) {

            message.error('请检查您的网络连接！');

        } else {

            if (error.response.status === 400) {

                message.error('请求方式不支持，请使用正确的请求方式！');

            } else if (error.response.status === 401) {

                // 清空token
                localStorage.clear();
                router.push('/login').then(() => {
                    message.error('会话已过期，请重新登录！');
                })

            } else if (error.response.status === 403) {

                message.error('您没有访问该资源的权限！');

            } else if (error.response.status === 404) {

                message.error(' 您访问的资源不存在！（404）');

            } else if (error.response.status === 429) {

                message.error(' 您的请求过于频繁！（429）');

            } else if (error.response.status === 503) {

                message.error(' 网关转发异常，请稍后再试！');

            }else if (error.response.status === 500){

                message.error('抱歉，服务器内部异常！');

            }  else {

                message.error('unknown error！');
            }
        }

        return Promise.reject(error)
    }
)
export default http