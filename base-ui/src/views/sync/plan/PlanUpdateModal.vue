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
        <a-form :model="form" :label-col="labelCol"  ref="formRef" :rules="formRules"  :wrapper-col="wrapperCol">
            <a-form-item label="" name="planContent">
                <a-textarea v-model:value="form.planContent"  :rows="15" autocomplete="off" />
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script>
    import {syncPlanDetail, syncPlanUpdate} from "@/api/sync";

    export default {
        name: "PlanUpdateModal.vue",
        data() {
            return {
                deptList: [],
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
                        syncPlanUpdate(this.form).then(res => {
                            this.$message.success("更新计划成功！");
                            this.handleClose();
                            this.$emit('success')
                        }).finally(e=>{
                            this.confirmLoading =  false;
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
                syncPlanDetail(uuid).then(res=>{
                    this.form.uuid = uuid;
                    this.form.planContent = res.result.planContent;
                    this.visible = true;
                });
            }
        }
    }
</script>

<style scoped>

</style>