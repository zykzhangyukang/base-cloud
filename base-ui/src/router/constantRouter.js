import Layout from "../layouts";
import Login from '../views/login'

/**
 * 通用路由表
 * 不需要权限就可以访问的路由
 */
export const constantRouter = [
    {
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('../layouts/main/redirect')
            }
        ]
    },
    {
        path: '/',
        component: Layout,
        redirect: '/dashboard',
        hidden: true,
        meta: { title: '首页', affix: true, icon: 'dashboard' },
        children: [
            {
                path: 'dashboard',
                component: () => import('../views/dashboard'),
                name: 'Dashboard',
                meta: { title: '首页', icon: 'el-icon-menu', affix: true }
            }
        ]
    },
    {
        path: '/login',
        component: Login,
        hidden: true,
        name: 'login',
        meta: { title: '登录' }
    },

]