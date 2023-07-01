<template>
    <a-modal v-model:visible="visible"
             title="更新角色"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="提交"
             ref="form"
    >
        <a-form :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item label="角色名称">
                <a-input v-model:value="form.roleName"/>
            </a-form-item>
            <a-form-item label="角色描述">
                <a-textarea v-model:value="form.roleDesc"/>
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>
    import {authRoleSelectById, authRoleUpdate} from "@/api/auth"

    export default {
        name: "roleUpdateModal.vue",
        data() {
            return {
                confirmLoading: false,
                visible: false,
                labelCol: {span: 4},
                wrapperCol: {span: 18},
                form: {
                    roleId: null,
                    roleName: '',
                    roleDesc: '',
                },
            }
        },
        computed: {},
        methods: {
            handleOk() {
                this.confirmLoading = true;
                authRoleUpdate(this.form).then(res => {
                    this.$message.success("更新角色成功！");
                    this.handleClose();
                    this.$emit('success')
                }).finally(e => {
                    this.confirmLoading = false;
                })
            },
            handleClose() {
                this.visible = false
                this.confirmLoading = false;
                this.form = this.$options.data().form;
            },
            open(roleId) {
                authRoleSelectById(roleId).then(res => {
                    this.form = res.result;
                    this.visible = true;
                });
            }
        }
    }
</script>

<style scoped>

</style>