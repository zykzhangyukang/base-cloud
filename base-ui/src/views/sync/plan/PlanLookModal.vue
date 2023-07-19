<template>
    <a-modal v-model:visible="visible"
             :title="null"
             :width="600"
             :footer="null"
             @cancel="handleClose"
    >
        <code-highlight :code="code" language="xml"></code-highlight>
    </a-modal>
</template>

<script>
    import {syncPlanDetail} from "@/api/sync";
    import CodeHighlight from "@/components/highlight/CodeHighlight";
    export default {
        name: "PlanLookModal.vue",
        components: {
           CodeHighlight
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