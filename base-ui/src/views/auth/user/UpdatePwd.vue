<template>
    <a-modal v-model:visible="visible"
             :confirm-loading="confirmLoading"
             title="设置密码"
             @ok="handleOk"
             @cancel="handleClose"
             cancelText="取消"
             okText="提交"
             ref="form"
    >
        <a-form :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item label="登录密码">
                <a-input v-model:value="form.password"  />
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script>
    import {authUserUpdatePwd} from "@/api/auth";

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
                    userId: null,
                },
            }
        },
        methods:{
            open(userId){
                this.visible = true;
                this.form.userId = userId;
            },
            handleOk() {
                this.confirmLoading  = true;
                authUserUpdatePwd(this.form).then(res=>{
                    this.$message.success("设置密码成功");
                    this.handleClose();
                }).finally(e=>{
                    this.confirmLoading = false;
                })
            },
            handleClose(){
                this.visible = false
                this.confirmLoading = false;
                this.form = this.$options.data().form;
            },
        }
    }
</script>

<style scoped>

</style>