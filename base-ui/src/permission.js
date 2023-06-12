import router from './routers'
import ruleRoutes from './routers/ruleRoutes'
import store from './store'
import {authUserInfo,authConstAll} from './api/auth';

// 后端返回的菜单来匹配对应的路由
function recursionRouter(userRouter = [], allRouter = [],parentPath="") {
  const realRouters = [];
  allRouter.forEach((v, i) => {
    userRouter.forEach((item, index) => {
      if (v.name === item.funcKey) {
        if (item.children && item.children.length > 0) {
          v.children = recursionRouter(item.children, v.children,v.path);
        }
        let pth = v.path;
        if(parentPath){
          pth = parentPath+'/'+v.path;
        }

        store.setMenuItem(item, 'key', pth);
        realRouters.push(v);
      }
    })
  })
  return realRouters;
}


//获取当前用户所有可以访问的路由权限
const getRoutes = (menuTree) => {
  return recursionRouter(menuTree,ruleRoutes,"")
};
const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  const userToken = localStorage.getItem('token');
  if (userToken) {
    // 有token
    // 路径为登录页则跳转到首页
    if (to.path === '/login') {
      next({ path: '/index' })
    } else {
      let userInfo = store.state.user.info;
      if (userInfo && userInfo.menus && userInfo.menus.length>0) {
        next()
      } else {

        // 获取用户信息
        let res = await authUserInfo();
        store.setUserInfo(res.result);
        let routesMap = getRoutes(res.result.menus)
        router.$addRoutes(routesMap);

        // 常量数据获取
        let constList = await authConstAll();
        store.setConstList(constList);

        next({ ...to, replace: true });
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login`)
    }
  }
})

router.afterEach(async(to, from) => {
  // finish progress bar
  if (to.path === '/login') {
    // 跳转登录页清除仓库用户信息，token取自localStorage，需要先行删除
    store.setUserToken('');
    store.setUserInfo(null);
  }
})
