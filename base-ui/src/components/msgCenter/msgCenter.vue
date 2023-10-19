<template>
    <span class="msg_icon"><BellOutlined /></span>
</template>

<script>
    import MyWebSock from "@/utils/socket";

    export default {
        name: "msgCenter.vue",
        data() {
            return {
                stompClient: null,
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
.msg_icon{
    cursor: pointer;
}
</style>