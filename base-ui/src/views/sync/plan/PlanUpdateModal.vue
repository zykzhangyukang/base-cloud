<template>
    <a-modal v-model:visible="visible"
             title="编辑计划"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             :width="750"
             cancelText="取消"
             okText="提交"
    >
        <a-spin :spinning="spinning" >
        <a-form :model="form" :label-col="labelCol"  ref="formRef" :rules="formRules"  :wrapper-col="wrapperCol">
            <a-form-item label="" name="planContent">
                <a-textarea v-model:value="form.planContent"  :rows="10" autocomplete="off" />
            </a-form-item>
        </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
    import {syncPlanDetail, syncPlanUpdate} from "@/api/sync";

    export default {
        name: "PlanUpdateModal.vue",
        data() {
            return {
                deptList: [],
                spinning: false,
                confirmLoading: false,
                visible: false,
                labelCol: { span: 4 },
                wrapperCol: { span: 24 },
                form:{
                    uuid: '',
                    planContent: '',
                },
                formRules: {
                    planContent: [
                        {required: true, message: '请填写同步计划', trigger: 'blur'},
                    ],
                },
            }
        },
        methods:{
            handleOk() {
                this.$refs.formRef
                    .validate()
                    .then(() => {
                        this.confirmLoading = true;
                        this.spinning = true;
                        syncPlanUpdate(this.form).then(res => {
                            this.$message.success("更新计划成功！");
                            this.handleClose();
                            this.$emit('success')
                        }).finally(e=>{
                            this.confirmLoading =  false;
                            this.spinning = false;
                        })
                    })
                    .catch(() => {
                        this.$message.warn('表单验证失败！');
                    });
            },
            handleClose(){
                this.visible = false
                this.confirmLoading = false;
                this.form = this.$options.data().form;
                this.$refs.formRef.resetFields();
            },
            open(uuid){
                this.visible = true;
                this.spinning = true;
                syncPlanDetail(uuid).then(res=>{
                    this.form.uuid = uuid;
                    this.form.planContent = res.result.planContent;
                }).finally(e=>{
                    this.spinning = false;
                });
            }
        }
    }
</script>

<style scoped>

</style>