import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../layout/index'

Vue.use(VueRouter)

const routes = [
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
            }
        ]
    }
]

const router = new VueRouter({
    routes
})

export default router
