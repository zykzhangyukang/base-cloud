<template>
    <a-table
            size='small'
            ref='table'
            :scroll='{x: width, y:this.windowHeight-350}'
            v-bind="$attrs"
    >
        <template #[item]="data" v-for="item in Object.keys($slots)" :key="item">
            <slot :name="item" v-bind="data || {}"></slot>
        </template>
    </a-table>
</template>
<script>
    export default {
        name: 'HTable',
        props: {
            page: {
                type: Boolean,
                default: true
            },
            width: {
                type: [String, Number],
            }
        },
        data () {
            return {
               windowHeight: document.documentElement.clientHeight,   //实时屏幕高度
            };
        },
        methods:{
        },
        mounted () {
          window.onresize = () => {
            return (() => {
              window.fullHeight = document.documentElement.clientHeight;
              this.windowHeight = window.fullHeight;
            })()
          };
        },
      watch: {
      },
    };
</script>