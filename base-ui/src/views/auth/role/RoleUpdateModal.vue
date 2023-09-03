<template>
    <a-modal v-model:visible="visible"
             title="更新角色"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="提交"
    >
        <a-spin :spinning="spinning">
        <a-form :model="form" ref="formRef" :rules="formRules" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item label="角色名称" name="roleName">
                <a-input v-model:value="form.roleName"/>
            </a-form-item>
            <a-form-item label="角色描述" name="roleDesc">
                <a-textarea v-model:value="form.roleDesc"/>
            </a-form-item>
        </a-form>
        </a-spin>
    </a-modal>
</template>
<script>
    import {authRoleSelectById, authRoleUpdate} from "@/api/auth"

    export default {
        name: "roleUpdateModal.vue",
        data() {
            return {
                spinning: false,
                confirmLoading: false,
                visible: false,
                labelCol: {span: 4},
                wrapperCol: {span: 18},
                form: {
                    roleId: null,
                    roleName: '',
                    roleDesc: '',
                },
                formRules: {
                    roleName: [
                        { required: true, message: '请填写角色名称', trigger: 'blur' },
                    ],
                    roleDesc: [
                        { required: true, message: '请填写角色备注', trigger: 'blur' },
                    ],
                },
            }
        },
        computed: {},
        methods: {
            handleOk() {
                this.$refs.formRef
                    .validate()
                    .then(() => {
                        this.spinning = true;
                        this.confirmLoading = true;
                        authRoleUpdate(this.form).then(res => {
                            this.$message.success("更新角色成功！");
                            this.handleClose();
                            this.$emit('success')
                        }).finally(e => {
                            this.spinning = false;
                            this.confirmLoading = false;
                        })
                    })
                    .catch(() => {
                        this.$message.warn('表单验证失败！');
                    });
            },
            handleClose() {
                this.visible = false
                this.confirmLoading = false;
                this.form = this.$options.data().form;
                this.$refs.formRef.resetFields();
            },
            open(roleId) {
                this.visible = true;
                this.spinning = true;
                authRoleSelectById(roleId).then(res => {
                    this.form = res.result;
                }).finally(()=>{
                    this.spinning = false;
                });
            }
        }
    }
</script>

<style scoped>

</style>