<template>
    <a-modal v-model:visible="visible"
             :title="'消息中心'"
             :width="700"
             :closable="true"
             :footer="null"
             :maskClosable="false"
             @cancel="handleClose"
    >
        <a-radio-group v-model:value="tab"  button-style="solid">
            <a-radio-button value="st"><BellOutlined /> 系统通知</a-radio-button>
            <a-radio-button value="sx"><AliwangwangOutlined /> 我的私信</a-radio-button>
            <a-radio-button value="dj">@ 我的</a-radio-button>
            <a-radio-button value="hf"><TeamOutlined /> 回复我的</a-radio-button>
            <a-radio-button value="pl"><LikeOutlined /> 收到的赞</a-radio-button>
        </a-radio-group>
        <a-card style="margin-top: 10px">
            <a-empty  image="https://gw.alipayobjects.com/mdn/miniapp_social/afts/img/A*pevERLJC9v0AAAAAAAAAAABjAQAAAQ/original" :description="'暂无消息'"/>
        </a-card>
    </a-modal>
</template>

<script>
    export default {
        name: "NotifyCenter.vue",
        components: {
        },
        data(){
            return {
                visible: false,
                tab: 'st',
            }
        },
        methods:{
            handleClose() {
                this.visible = false;
                this.tab = 'st'
            },
            open(){
                this.visible = true;
            }
        },
    }
</script>

<style scoped>

</style>