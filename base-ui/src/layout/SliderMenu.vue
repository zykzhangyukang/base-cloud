<template>
  <a-menu
    :inline-collapsed='menuToggle'
    v-model:openKeys='openKeys'
    mode='inline'
    :theme='theme'
    :selectedKeys='selectedKeys'
    @click='onSelectChange'
    @openChange='onOpenChange'
  >
    <template v-for='(item) of menu'>
      <sub-menu 
        v-if="item.children && item.children.length > 0 && item.children.filter(v=>v.hidden).length!==item.children.length"
        :key='item.funcId'
        :item='item'
      ></sub-menu>
      <menu-item v-else :key='item.funcId' :item='item'></menu-item>
    </template>
  </a-menu>
</template>
<script>
import {clearPageState} from '@/utils/pageState'
import MenuItem from './MenuItem.vue'
import SubMenu from './SubMenu.vue'
const getPathName = (pathname, menuTree,parentName) => {
  for(let i = 0; i < menuTree.length; i++){
    //路径包含
    if (menuTree[i].path) {
      //路径相等
      if(pathname === menuTree[i].path){
        //隐藏菜单去父路径
        if(menuTree[i].hidden){
          return parentName;
        }else {
          if (parentName) {
            return menuTree[i].funcId
          } else {
            return {
              opens: [],
              select: menuTree[i].funcId
            };
          }
        }
      }
    }
    if (menuTree[i].children && menuTree[i].children.length>0) {
      let str = getPathName(pathname, menuTree[i].children , menuTree[i].key);
      if(typeof str == 'object'){
        return {
          opens: str.opens.concat([menuTree[i].funcId]),
          select: str.select
        };
      } else if (typeof str == 'string') {
        return {
          opens: [menuTree[i].funcId],
          select: str
        };
      }
    }
  }
}
export default {
  name: 'SliderMenu',
  components:{
    MenuItem,
    SubMenu
  },
  props: {
    menuToggle: {
      required: true,
      type: Boolean
    },
    theme: String,
    menu: {
      required: true,
      type: Array
    }
  },
  data(){
    return {
      openKeys:[],
      selectedKeys:[],
    }
  },
  mounted() {
    let d = getPathName(this.$route.path, this.menu);
    if(d){
      this.openKeys = d.opens;
      this.selectedKeys = [d.select];
    }
  },
  watch:{
    menuToggle(newVal) {
      if (newVal) {
        this.openKeys = [];
      } else {
        let d = getPathName(this.$route.path, this.menu);
        if(d){
          this.openKeys = d.opens;
          this.selectedKeys = [d.select];
        }
      }
    },
    '$route'(newVal){
      let d = getPathName(this.$route.path, this.menu);
      if(d) {
        this.openKeys = d.opens;
        this.selectedKeys = [d.select];
      }
    },
    'menu'(newVal){
      let d = getPathName(this.$route.path, this.menu);
      if(d){
        this.openKeys = d.opens;
        this.selectedKeys = [d.select];
      }
    }
  },
  methods: {
    onOpenChange(v) {
      if (v.length === 0 || v.length === 1) {
          this.openKeys = v;
          return void 0;
      }
      const latestOpenKey = v[v.length - 1]
      // 这里与定义的路由规则有关
      if (latestOpenKey.toString().includes(v[0])) {
          this.openKeys = v;
      } else {
          this.openKeys = [latestOpenKey];
      }
    },
    onSelectChange({ key }){
      clearPageState(key);
      this.selectedKeys = [key];
    }
  }
}
</script>