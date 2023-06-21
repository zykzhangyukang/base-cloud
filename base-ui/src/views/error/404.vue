<template>
  <a-card>
  <a-result
        status="404"
        title="404"
        subTitle="抱歉, 当前页面不存在！"
    >
      <template v-slot:extra>
        <a-button type="primary" @click="()=>{this.$router.push('/')}">返回首页</a-button>
      </template>
    </a-result>
  </a-card>
</template>
<script>
export default {
  name: '404',
}
</script>