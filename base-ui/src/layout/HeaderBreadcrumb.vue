<template>
  <a-breadcrumb>
    <a-breadcrumb-item><router-link to='/index'>首页</router-link></a-breadcrumb-item>
    <template v-for='item of arr'>
      <a-breadcrumb-item v-if="item.key" :key='item.key'>
        <router-link :to='item.key'>{{item.title}}</router-link>
      </a-breadcrumb-item>
      <a-breadcrumb-item v-else :key='item' >{{item}}</a-breadcrumb-item>
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