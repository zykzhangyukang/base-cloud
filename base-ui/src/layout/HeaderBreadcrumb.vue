<template>
  <a-breadcrumb>
    <a-breadcrumb-item><router-link to='/index'>首页</router-link></a-breadcrumb-item>
    <template v-for='item of arr'>
      <a-breadcrumb-item v-if="item.path" :key='item.path'>
        <router-link :to='item.path'>{{item.funcName}}</router-link>
      </a-breadcrumb-item>
      <a-breadcrumb-item v-else :key='item.path' >{{item.funcName}}</a-breadcrumb-item>
    </template>
  </a-breadcrumb>
</template>
<script>
export default {
  props: {
    arr: {
      type:Array,
      default(){
        return []
      }
    }
  },
  setup (props) {
    
  }
}
</script>