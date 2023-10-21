<template>
    <span class="msg_icon_wrapper">
        <a-popover placement="bottomLeft" trigger="hover">
        <a-badge :count="noReadCount">
           <BellOutlined class="msg_icon"/>
        </a-badge>
      <template #title>
          <h5>消息通知</h5>
        </template>
        <template #content>
            <div class="msg_wrapper">
                <a style="display: inline-block" class="btn-text-normal" v-for="item in messageList" :key="item">
                 {{"消息内容："+ item}}
                </a>
              <a-empty v-if="messageList.length === 0" description="暂无消息" />
            </div>
        </template>
        </a-popover>
    </span>
</template>

<script>
    import MyWebSock from "@/utils/socket";
    import store from '@/store/index'
    export default {
        name: "notify.vue",
        data() {
            return {
                stompClient: null,
            }
        },
        computed: {
            messageList() {
                return store.state.message.messageList
            },
            noReadCount() {
                return store.state.message.noRead
            }
        },
        created() {
            this.stompClient = new MyWebSock()
        },
        beforeUnmount() {
            this.stompClient.disconnect()
        },
    }
</script>

<style scoped>
    .msg_icon {
        cursor: pointer;
        display: inline-block;
        font-size: 18px;
    }
    .msg_icon_wrapper {
        margin-right: 15px;
    }
    .msg_wrapper{
        width: 200px;
    }
</style>