const Auth =() =>  import(/* webpackChunkName: 'login' */'../views/auth');
const Sync =() =>  import(/* webpackChunkName: 'login' */'../views/sync');

const routes = [
    {
        path: '/auth',
        name: 'auth',
        component: Auth,
        children: [
            {
                path: 'user',
                name: 'authUser',
                component: () => import('../views/auth/user/index'),
            },
            {
                path: 'role',
                name: 'authRole',
                component: () => import('../views/auth/role/index')
            },
            {
                path: 'resc',
                name: 'authResc',
                component: () => import('../views/auth/resc/index')
            },
            {
                path: 'func',
                name: 'authFunc',
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
                name: 'syncPlan',
                component: () => import('../views/sync/plan/index'),
            },
            {
                path: 'result',
                name: 'syncResult',
                component: () => import('../views/sync/result/index')
            },
            {
                path: 'msg',
                name: 'syncMsg',
                component: () => import('../views/sync/msg/index')
            },
            {
                path: 'callback',
                name: 'syncCallback',
                component: () => import('../views/sync/callback/index')
            }
        ]
    },
]
export default routes