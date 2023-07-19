<template>
    <a-modal v-model:visible="visible"
             title="添加计划"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             :width="700"
             cancelText="取消"
             okText="提交"
    >
        <a-form :model="form" :label-col="labelCol" :wrapper-col="wrapperCol"  ref="formRef" :rules="formRules">
            <a-form-item label="" name="planContent">
                <a-textarea v-model:value="form.planContent"  :rows="10" autocomplete="off" />
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>
    import {syncPlanSave} from "@/api/sync";
    export default {
        name: "planSaveModal.vue",
        data() {
            return {
                formRules: {
                    planContent: [
                        {required: true, message: '请填写同步计划', trigger: 'blur'},
                    ],
                },
                confirmLoading: false,
                visible: false,
                labelCol: { span: 4 },
                wrapperCol: { span: 24 },
                form:{
                    planContent: '',
                },
            }
        },
        methods:{
            handleOk() {
                this.$refs.formRef
                    .validate()
                    .then(() => {
                        this.confirmLoading = true;
                        syncPlanSave(this.form).then(res => {
                            this.$message.success("新增计划成功");
                            this.handleClose();
                            this.$emit('success')
                        }).finally(e=>{
                            this.confirmLoading = false;
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
            open(){
                this.visible = true;
            }
        }
    }
</script>

<style scoped>

</style>