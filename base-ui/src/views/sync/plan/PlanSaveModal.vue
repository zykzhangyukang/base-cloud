<template>
    <a-modal v-model:visible="visible"
             title="新增计划"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             :width="750"
             cancelText="取消"
             okText="提交"
    >
        <a-spin :spinning="spinning" >
        <a-form :model="form" :label-col="labelCol" :wrapper-col="wrapperCol"  ref="formRef" :rules="formRules">
            <a-form-item label="" name="planContent">
                <a-textarea v-model:value="form.planContent"  :rows="15" autocomplete="off" />
            </a-form-item>
        </a-form>
        </a-spin>
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
                spinning: false,
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
                        this.spinning = true;
                        this.confirmLoading = true;
                        syncPlanSave(this.form).then(res => {
                            this.$message.success("新增计划成功");
                            this.handleClose();
                            this.$emit('success')
                        }).finally(e=>{
                            this.confirmLoading = false;
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
            open(){
                this.visible = true;
            }
        }
    }
</script>

<style scoped>

</style>