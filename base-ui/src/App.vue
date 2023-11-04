<template>
  <a-config-provider :locale="Zh_CN" >
    <router-view/>
  </a-config-provider>
</template>
<script>
  import Zh_CN from 'ant-design-vue/lib/locale/zh_CN'
import {setRouter} from '@/utils/app';
export default {
  name: 'App',
  data() {
    return {
      Zh_CN
    }
  },
  mounted() {
    setRouter(this.$router,this.$route);
  },
  computed: {
  }
}
</script>