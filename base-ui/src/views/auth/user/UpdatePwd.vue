<template>
    <a-modal v-model:visible="visible"
             :confirm-loading="confirmLoading"
             title="修改密码"
             @ok="handleOk"
             @cancel="handleClose"
             cancelText="取消"
             okText="提交"
    >
        <a-form :model="form" ref="form"  :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item label="新密码" name="password" :rules="passwordRules">
                <a-input-password  v-model:value="form.password"  autocomplete="off"  />
            </a-form-item>
            <a-form-item label="确认密码" name="confirmPassword" :rules="confirmPasswordRules">
                <a-input-password v-model:value="form.confirmPassword" autocomplete="off"  />
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script>
    import {authUserSave, authUserUpdatePwd} from "@/api/auth";

    export default {
        name: "userUpdatePwd.vue",
        data() {
            return {
                deptList: [],
                confirmLoading: false,
                visible: false,
                labelCol: { span: 4 },
                wrapperCol: { span: 18 },
                form:{
                    password: '',
                    confirmPassword: '',
                    userId: null,
                },
                passwordRules: [
                    { required: true, message: '请输入新密码', trigger: 'blur' },
                ],
                confirmPasswordRules: [
                    { required: true, message: '请再次输入密码', trigger: 'blur' },
                    { validator: this.validateConfirmPassword, trigger: 'blur' },
                ],
            }
        },
        methods:{
            validateConfirmPassword(rule, value, callback) {
                if (value && value !== this.form.password) {
                    return Promise.reject(new Error('两次输入的密码不一致'));
                } else {
                    return Promise.resolve();
                }
            },
            open(userId){
                this.visible = true;
                this.form.userId = userId;
            },
            handleOk() {
                this.$refs.form
                    .validate()
                    .then(() => {
                        this.confirmLoading  = true;
                        authUserUpdatePwd({userId: this.form.userId , password: this.form.password}).then(res=>{
                            this.$message.success("修改密码成功");
                            this.handleClose();
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
                this.$refs.form.resetFields();
            },
        }
    }
</script>

<style scoped>

</style>