const authUser = () => import(/* webpackChunkName: 'login' */'../views/auth/user/index');
const authRole = () => import(/* webpackChunkName: 'login' */'../views/auth/role/index');
const authRoleAuthorized = ()=> import(/* webpackChunkName: 'login' */'../views/auth/role/roleAuthorized');
const authResc = () => import(/* webpackChunkName: 'login' */'../views/auth/resc/index');
const authFunc = () => import(/* webpackChunkName: 'login' */'../views/auth/func/index');

const syncPlan = () => import(/* webpackChunkName: 'login' */'../views/sync/plan/index');
const syncResult = () => import(/* webpackChunkName: 'login' */'../views/sync/result/index');
const syncMsg = () => import(/* webpackChunkName: 'login' */'../views/sync/msg/index');
const syncCallback = () => import(/* webpackChunkName: 'login' */'../views/sync/callback/index');

const routes = [

    {path: '/auth/user', name: 'authUser', component: authUser},
    {path: '/auth/role', name: 'authRole', component: authRole},
    { path: '/auth/role/authorized' ,name: 'authRoleAuthorized', component: authRoleAuthorized},
    {path: '/auth/resc', name: 'authResc', component: authResc},
    {path: '/auth/func', name: 'authFunc', component: authFunc},

    {path: '/sync/plan', name: 'syncPlan', component: syncPlan},
    {path: '/sync/result', name: 'syncResult', component: syncResult},
    {path: '/sync/msg', name: 'syncMsg', component: syncMsg},
    {path: '/sync/callback', name: 'syncCallback', component: syncCallback},

]

export default routes