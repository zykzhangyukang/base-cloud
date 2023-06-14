import axios from 'axios'
import {message} from 'ant-design-vue'
import router from "@/routers";

const http = axios.create({
    baseURL: 'http://127.0.0.1:8989',
    timeout: 10000,
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

        } else if (response.data.code === 403) {

            message.error('您没有访问该资源的权限');

        } else if (response.data.code === 401) {

            localStorage.clear();

            router.push('/login').then(() => {

                message.error('用户会话已过期');
            })

        } else if (response.data.code === 404) {

            message.error('您请求的资源不存在');

        } else if (response.data.code === 405) {

            message.warn(response.data.msg);

        } else {
            message.error(response.data.msg ? response.data.msg : '接口其他错误');
        }

        return Promise.reject(response.data);
    },
    (error) => {

        if (!error.response) {

            message.error({
                content: '连接服务器失败！',
                type: 'error',
                duration: 3 * 1000
            })

        } else {

            if (error.response.data.statusCode === 400) {

                message.error({
                    content: '请求错误！',
                    type: 'error',
                    duration: 5 * 1000
                })

            } else {

                console.log('err' + error) // for debug

                if (error.toString().search('timeout') > -1) {
                    message.error('请求超时，请重试或联系管理员！');
                }

                if (error.toString().search('Network Error') > -1) {
                    message.error('请连接网络！');
                } else {
                    // 其他未知错误不提示
                    message.error({
                        content: error.message,
                        type: 'error',
                        duration: 5 * 1000
                    })
                }
            }
        }


        return Promise.reject(error)
    }
)
export default http