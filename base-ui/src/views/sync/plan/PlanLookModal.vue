<template>
    <a-modal v-model:visible="visible"
             :title="null"
             :width="700"
             :closable="false"
             :footer="null"
             @cancel="handleClose"
    >
        <a-spin :spinning="spinning" >
            <pre class="plan_code">
              <code v-highlightjs class="xml" :key="Math.ceil(Math.random()*100000)">{{code}}</code>
            </pre>
        </a-spin>
    </a-modal>
</template>

<script>
    import {syncPlanDetail} from "@/api/sync";
    import {highlightjs} from '@/utils/highlight';
    import 'highlight.js/styles/idea.css';

    export default {
        name: "PlanLookModal.vue",
        directives: {
            highlightjs,
        },
        components: {
        },
        data(){
            return {
                title: '',
                code: '',
                codeKey: '',
                visible: false,
                spinning: false,
            }
        },
        methods:{
            handleClose() {
                this.visible = false
            },
            open(uuid) {
                this. spinning = true;
                this.visible = true;
                syncPlanDetail(uuid).then(res=>{
                    this.title = res.result.description;
                    this.code = res.result.planContent;
                    this.codeKey = res.result.planCode;
                }).finally(()=>{
                    this.spinning = false;
                });
            }
        },
    }
</script>

<style scoped>
    .plan_code{
        font-size: 13px;
    }
</style>