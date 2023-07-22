<template>
    <a-modal v-model:visible="visible"
             :title="title"
             :width="800"
             :footer="null"
             @cancel="handleClose"
    >
            <pre class="code">
              <code v-highlightjs class="xml" :key="Math.ceil(Math.random()*100000)">{{code}}</code>
            </pre>
    </a-modal>
</template>

<script>
    import {syncPlanDetail} from "@/api/sync";
    import {highlightjs} from '@/utils/highlight';
    import 'highlight.js/styles/monokai-sublime.css';

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
            }
        },
        methods:{
            handleClose() {
                this.visible = false
            },
            open(uuid) {
                syncPlanDetail(uuid).then(res=>{
                    this.title = res.result.description;
                    this.code = res.result.planContent;
                    this.codeKey = res.result.planCode;
                    this.visible = true;
                })
            }
        },
    }
</script>

<style scoped>
.code{
    font-family: "Comic Sans MS",serif;
    font-size: 11px;
}
</style>