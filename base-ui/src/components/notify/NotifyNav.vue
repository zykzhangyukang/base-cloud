<template>
    <span class="msg_icon_wrapper">
           <a-badge :count="noReadCount">
           <BellOutlined @click="showMsgCenter" class="msg_icon"/>
        </a-badge>
        <NotifyCenter ref="notifyCenterRef"></NotifyCenter>
    </span>
</template>

<script>
    import MyWebSock from "@/utils/socket";
    import store from '@/store/index'
    import {authUserPullNotify} from "@/api/auth";
    import NotifyCenter from "@/components/notify/NotifyCenter";
    export default {
        name: "NotifyNav.vue",
        components:{
         NotifyCenter
        },
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
        methods:{
            getMessages(){
                authUserPullNotify().then(res=>{
                    if(res.result){
                        res.result.forEach(e=>{
                            store.addUserMsg(e)
                        })
                    }
                })
            },
            showMsgCenter(){
                this.$refs['notifyCenterRef'].open();
            },
        },
        created() {
            this.stompClient = new MyWebSock();
            this.getMessages();
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

</style>