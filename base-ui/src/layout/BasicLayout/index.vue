<template>
  <el-container class="layout-class">
    <el-header class="layout-header">
      <my-header />
    </el-header>
    <el-main class="layout-main-wrap">
      <div class="layout-main">
        <my-main />
      </div>
    </el-main>
  </el-container>
</template>

<script>
  import MyHeader from './components/header'
  import MyMain from './components/main'
  export default {
    name: 'BasicLayout',
    components: { MyHeader, MyMain }
  }
</script>
<style lang="scss" scoped>

</style>
