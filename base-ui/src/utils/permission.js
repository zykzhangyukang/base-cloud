import router from '@/routers'
import ruleRoutes from '@/routers/ruleRoutes'
import store from '@/store'
import {authUserInfo,authConstAll} from '@/api/auth';


//获取当前用户所有可以访问的路由权限
const getRoutes = (menuTree) => {
  let userRouterList =[];
  getMenuList(menuTree,userRouterList);
  return userRouterList;
};

const getMenuList = (menuTree,menuList) => {
  for(let i = 0; i< menuTree.length; i++){
    let route = ruleRoutes.find(r => r.path === menuTree[i].funcKey);
    if (route) {
      menuList.push(route);
      store.setMenuItem(menuTree[i], 'path', route.path);
    }
    if(menuTree[i].children && menuTree[i].children.length>0){
      getMenuList(menuTree[i].children,menuList);
    }
  }
};


const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  const userToken = localStorage.getItem('token');
  if (userToken) {
    // 有token
    // 路径为登录页则跳转到首页
    if (to.path === '/login') {
      next({ path: '/dashboard' })
    } else {
      let userInfo = store.state.user.info;
      if (userInfo && userInfo.menus) {
        next()
      } else {

        // 保存用户信息
        let res = await authUserInfo();
        store.setUserInfo(res.result);

        // 根据用户权限匹配路由信息
        let routesMap = getRoutes(res.result.menus);
        router.$addRoutes(routesMap);

        // 常量数据获取
        let {result: list} = await authConstAll();
        store.setConstList(list);

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
  if (to.path === '/login') {
    store.setUserToken('');
    store.setUserInfo(null);
  }
})


export default {
  install(app) {
    app.directive('permission', {
      mounted(el, binding) {
        const permission = binding.value;
        const hasPermission = checkPermission(permission);
        if (!hasPermission) {
          el.disabled = true;
          el.style.display = 'none';
        }
      },
    });
  },
};

function checkPermission(permission) {
  let userInfo = store.state.user.info;
  return userInfo && userInfo.buttons && userInfo.buttons.length > 0 && userInfo.buttons.indexOf(permission) !== -1
}