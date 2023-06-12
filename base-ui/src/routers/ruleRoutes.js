const Auth =() =>  import(/* webpackChunkName: 'login' */'../views/auth');
const Sync =() =>  import(/* webpackChunkName: 'login' */'../views/sync');

const routes = [
    {
        path: '/auth',
        name: 'Auth',
        component: Auth,
        children: [
            {
                path: 'user',
                name: 'AuthUser',
                component: () => import('../views/auth/user/index'),
            },
            {
                path: 'role',
                name: 'AuthRole',
                component: () => import('../views/auth/role/index')
            },
            {
                path: 'resc',
                name: 'AuthResc',
                component: () => import('../views/auth/resc/index')
            },
            {
                path: 'func',
                name: 'AuthFunc',
                component: () => import('../views/auth/func/index')
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
                component: () => import('../views/sync/plan/index'),
            },
            {
                path: 'result',
                name: 'SyncResult',
                component: () => import('../views/sync/result/index')
            },
            {
                path: 'msg',
                name: 'SyncMsg',
                component: () => import('../views/sync/msg/index')
            },
            {
                path: 'callback',
                name: 'SyncCallback',
                component: () => import('../views/sync/callback/index')
            }
        ]
    },
]
export default routes