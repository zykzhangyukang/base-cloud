<template>
  <a-sub-menu>
    <template v-slot:title>
      <span><component v-if='item.funcIcon' :is="item.funcIcon" ></component><span>{{item.funcName}}</span></span>
    </template>
    <template v-for='(itemChild) of item.children'>
      <sub-menu 
        v-if="itemChild.children && itemChild.children.length > 0 && itemChild.children.filter(v=>v.funcDirStatus === 'hide').length!==itemChild.children.length"
        :key='itemChild.funcId'
        :item='itemChild'
      ></sub-menu>
      <menu-item v-else :key='itemChild.funcId' :item='itemChild'></menu-item>
    </template>
  </a-sub-menu>
</template>
<script>
import * as Icon from '@ant-design/icons-vue';
import MenuItem from './MenuItem.vue'
export default {
  name: 'SubMenu',
  components:{
    MenuItem,
    ...Icon
  },
  props: {
    item: {
      required: true,
      type: Object
    },
  },
  data(){
    return {
    }
  },
}
</script>