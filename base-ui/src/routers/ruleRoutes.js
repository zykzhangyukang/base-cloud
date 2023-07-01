const authUser = () => import(/* webpackChunkName: 'login' */'../views/auth/user/index');
const authRole = () => import(/* webpackChunkName: 'login' */'../views/auth/role/index');
const authRoleAuthorized = ()=> import(/* webpackChunkName: 'login' */'../views/auth/role/RoleAuthorized');
const authResc = () => import(/* webpackChunkName: 'login' */'../views/auth/resc/index');
const authFunc = () => import(/* webpackChunkName: 'login' */'../views/auth/func/index');

const syncPlan = () => import(/* webpackChunkName: 'login' */'../views/sync/plan/index');
const syncResult = () => import(/* webpackChunkName: 'login' */'../views/sync/result/index');
const syncMsg = () => import(/* webpackChunkName: 'login' */'../views/sync/msg/index');
const syncCallback = () => import(/* webpackChunkName: 'login' */'../views/sync/callback/index');

const routes = [

    {path: '/auth/user', name: 'AuthUser', component: authUser},
    {path: '/auth/role', name: 'AuthRole', component: authRole},
    { path: '/auth/role/authorized' ,name: 'AuthRoleAuthorized', component: authRoleAuthorized},
    {path: '/auth/resc', name: 'AuthResc', component: authResc},
    {path: '/auth/func', name: 'AuthFunc', component: authFunc},

    {path: '/sync/plan', name: 'SyncPlan', component: syncPlan},
    {path: '/sync/result', name: 'SyncResult', component: syncResult},
    {path: '/sync/msg', name: 'SyncMsg', component: syncMsg},
    {path: '/sync/callback', name: 'SyncCallback', component: syncCallback},

]

export default routes