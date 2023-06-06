import Layout from "../layout/index";

export const constantRouter = [
    {
        path: '/redirect',
        name: 'Layout',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('../layout/BasicLayout/components/main/redirect')
            }
        ]
    },

    {
        path: '/',
        component: Layout,
        redirect: '/dashboard',
        hidden: true,
        meta: {title: '首页', affix: true, icon: 'dashboard'},
        children: [
            {
                path: 'dashboard',
                component: () => import('../views/dashboard/index'),
                name: 'Dashboard',
                meta: {title: '首页', icon: 'el-icon-menu', affix: true}
            },
            {
                path: '404',
                component: () => import('../views/404'),
                hidden: true,
                meta: { title: '页面异常', icon: 'el-icon-menu', hidden: true }
            }
        ]
    },
    {
        path: '/login',
        component: () => import('../views/login/index'),
        hidden: true,
        name: 'login',
        meta: {title: '登录'}
    }
]