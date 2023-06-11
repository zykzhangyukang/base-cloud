import router from './routers'
import ruleRoutes from './routers/ruleRoutes'
import store from './store'
import { message } from 'ant-design-vue'
import {sendUserInfo} from './api/login';
import {$iscode} from './utils/app';

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

//转化当前用户导航菜单权限tree为一维数组list
const getMenuList = (menuTree,menuList) => {
  for(let i = 0; i< menuTree.length; i++){
    let route = ruleRoutes.find(r => r.name === menuTree[i].name);
    if (route) {
      menuList.push(route);
      store.setMenuItem(menuTree[i], 'key', route.path);
    }
    if(menuTree[i].subs && menuTree[i].subs.length>0){
      getMenuList(menuTree[i].subs,menuList);
    }
  }
};
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
        try {
          let res = await sendUserInfo()
          if($iscode(res)){
            store.setUserInfo(res.result);
            let routesMap = getRoutes(res.result.menus)
            router.$addRoutes(routesMap);
            next({ ...to, replace: true });
          }else {
            localStorage.clear();
            next(`/login`)
          }
        } catch (error) {
          message.error(error || 'Has Error')
          localStorage.clear();
          next(`/login`)
        }
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
