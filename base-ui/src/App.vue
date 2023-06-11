<template>
  <a-config-provider >
    <router-view/>
  </a-config-provider>
</template>
<script>
import {setRouter} from '@/utils/app';
export default {
  name: 'App',
  data() {
    return {
    }
  },
  mounted() {
    setRouter(this.$router,this.$route);
  },
  computed: {
  }
}
</script>