const Index = () => import(/* webpackChunkName: 'login' */'../views/Index.vue');
const Auth =() =>  import(/* webpackChunkName: 'login' */'../views/Auth');
const Sync =() =>  import(/* webpackChunkName: 'login' */'../views/Sync');

const routes = [
    {
        path: '/index',
        name: 'Index',
        component: Index
    },
    {
        path: '/auth',
        name: 'Auth',
        component: Auth,
        children: [
            {
                path: 'user',
                name: 'AuthUser',
                component: () => import('../views/Auth/User/index'),
            },
            {
                path: 'role',
                name: 'AuthRole',
                component: () => import('../views/Auth/Role/index')
            },
            {
                path: 'resc',
                name: 'AuthResc',
                component: () => import('../views/Auth/Resc/index')
            },
            {
                path: 'func',
                name: 'AuthFunc',
                component: () => import('../views/Auth/Func/index')
            }
        ]
    },
    {
        path: '/sync',
        name: 'Sync',
        component: Sync,
        children: [
            {
                path: 'plan',
                name: 'SyncPlan',
                component: () => import('../views/Sync/Plan/index'),
            },
            {
                path: 'result',
                name: 'SyncResult',
                component: () => import('../views/Sync/Result/index')
            },
            {
                path: 'msg',
                name: 'SyncMsg',
                component: () => import('../views/Sync/Msg/index')
            },
            {
                path: 'callback',
                name: 'SyncCallback',
                component: () => import('../views/Sync/Callback/index')
            }
        ]
    },
]
export default routes