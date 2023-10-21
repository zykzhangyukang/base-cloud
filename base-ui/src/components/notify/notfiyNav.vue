<template>
    <span class="msg_icon_wrapper">
        <a-popover placement="bottomLeft" trigger="hover">
        <a-badge :count="noReadCount">
           <BellOutlined class="msg_icon"/>
        </a-badge>
      <template #title>
          <h4>消息通知</h4>
        </template>
        <template #content>
            <div class="msg_wrapper">
                  <a-row v-for="item in messageList" :key="item" style="border-bottom: 1px solid #eeeeee">
                    <a-col :span="20">
                               <span class="msg_item" >
                                    {{ item}}
                               </span>
                    </a-col>
                    <a-col :span="4">
                        <div class="op_btn_group">
                            <span class="mark_read"><CheckOutlined />标记</span>
                          </div>
                    </a-col>
                  </a-row>
              <a-empty v-if="messageList.length === 0" description="暂无消息"/>
                <span v-else style="text-align: center;display: inline-block;width: 100%">
                       <a class="btn-text-normal">查看更多</a>
                </span>
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

    .msg_wrapper {
        border-radius: 5px;
        width: 250px;
    }

    .msg_item {
        padding: 5px;
        font-size: 12px;
        display: inline-block;
        color: #303030;
        background: #ffffff;
        cursor: pointer;
        width: 100%;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        transition: all 300ms;
    }

    .msg_item:hover {
        color: #5cadff;
    }
    .op_btn_group{
        margin-top: 4px;
    }
    .op_btn_group > .mark_read{
        font-size: 11px;
        cursor: pointer;
        color: #ff9900;
    }
</style>