<template>
  <div ref="main-container" class="main-container">
    <router-view :key="$route.fullPath"></router-view>
  </div>
</template>

<script>
  export default {
  name: 'Main',
}
</script>
<style scoped>
.main-container {
  height: 100%;
}
</style>
