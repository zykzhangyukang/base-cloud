<template>
  <a-layout class="app">
    <a-back-top />
    <app-aside :menuToggle='menuToggle' :menu='menu'></app-aside>
    <a-layout
      :style="{marginLeft: menuToggle ? '80px' : '200px', minHeight: '100vh'}"
    >
      <app-header
        :breadCrumb='breadCrumb' 
        :menuToggle='menuToggle'
        :menuClick='menuClick' 
        :avatar='avatar' :loginOut='loginOut'
      ></app-header>
        <a-layout-content class="content">
                <router-view :key="key"/>
        </a-layout-content>
      <app-footer></app-footer>
    </a-layout>
  </a-layout>
</template>
<style lang="less">
@import '../style/layout.less';
</style>
<script>
import store from '../store';
import AppAside from './AppAside'
import AppHeader from './AppHeader'
import AppFooter from './AppFooter'
import avatar from '@/assets/images/user.png'
import {authUserLogout} from '@/api/auth';
//返回除了首页之外的面包屑
const getBreadCrumb = (pathname,menuTree,crumb) => {
  // 首页返回false
  if(pathname === '/dashboard') return false;
  // 递归遍历远端导航菜单tree
  for(let i = 0; i< menuTree.length; i++){
    // 符合则添加到面包屑中
    if(pathname.search(menuTree[i].path) === 0){
      if(menuTree[i].path === pathname){
        crumb.unshift(menuTree[i]);
        return true;
      }else {
        // 不符合如果有子集继续查找
        if(menuTree[i].children && menuTree[i].children.length>0){
          let state = getBreadCrumb(pathname, menuTree[i].children, crumb);
          if(state){
            crumb.unshift(menuTree[i]);
            return true;
          }
        }
      }
    }
  }
  return false;
};
export default {
  name: 'DefaultLayout',
  components: {
    AppAside,
    AppHeader,
    AppFooter
  },
  computed: {
    key() {
      return this.$route.path
    },
    menu() {
      return store.state.user.info.menus || []
    },
    menuToggle() {
      return store.state.app.menuToggle
    },
    breadCrumb() {
      let breadCrumb = [];
      getBreadCrumb(this.$route.path,this.menu,breadCrumb);
      return breadCrumb;
    },
  },
  data() {
    return {
      avatar,
    }
  },
  mounted() {
  },
  methods: {
    menuClick() {
      store.setAppMenuToggle(!this.menuToggle);
    },
    async loginOut() {
      await authUserLogout();
      localStorage.clear();
      await this.$router.push('/login');
    }
  },
  watch: {
  }
}
</script>
<style>
</style>