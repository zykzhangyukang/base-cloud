<template>
    <a-modal v-model:visible="visible"
             :title="null"
             :width="600"
             :footer="null"
             @cancel="handleClose"
    >
    </a-modal>
</template>

<script>
    import {syncPlanDetail} from "@/api/sync";
    export default {
        name: "PlanLookModal.vue",
        components: {
        },
        data(){
            return {
                code: '',
                visible: false,
            }
        },
        methods:{
            handleClose() {
                this.visible = false
            },
            open(uuid) {
                syncPlanDetail(uuid).then(res=>{
                    this.code = res.result.planContent;
                    this.visible = true;
                })
            }
        }
    }
</script>

<style scoped>

</style>