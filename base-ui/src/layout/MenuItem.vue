<template>
  <a-menu-item @click="toPage(item)">
    <component v-if='item.funcIcon' :is="item.funcIcon" ></component><span>{{item.funcName}}</span>
  </a-menu-item>
</template>
<script>
import * as Icon from '@ant-design/icons-vue';

export default {
  name: 'MenuItem',
  components: {
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
  methods:{
    toPage(item){
      if(item && item.path){
        this.$router.push(item.path);
      }else {
        this.$router.push('/404');
      }
    }
  },
  created() {
  }
}
</script>