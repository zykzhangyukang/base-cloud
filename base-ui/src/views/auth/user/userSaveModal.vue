<template>
    <a-modal v-model:visible="visible"
             title="新增用户"
             @ok="handleOk"
             @cancel="handleClose"
             cancelText="取消"
             okText="提交"
             ref="form"
    >
        <a-form :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item label="用户名">
                <a-input v-model:value="form.username" />
            </a-form-item>
            <a-form-item label="登录密码">
                <a-input v-model:value="form.password" />
            </a-form-item>
            <a-form-item label="真实姓名">
                <a-input v-model:value="form.realName" />
            </a-form-item>
            <a-form-item label="部门编号">
                <a-input v-model:value="form.deptCode" />
            </a-form-item>
            <a-form-item label="状态">
                <a-radio-group v-model:value="form.userStatus">
                    <a-radio v-for="item in userStatusG" :value="item.code" :key="item.code">{{userStatusGName[item.code]}}</a-radio>
                </a-radio-group>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script>
    import {authUserSave} from "@/api/auth"
    import {authDomain, formatConst, getConst} from "@/utils/constant";
    export default {
        name: "userSaveModal.vue",
        data() {
            return {
                visible: false,
                labelCol: { span: 4 },
                wrapperCol: { span: 18 },
                form:{
                    username: '',
                    password: '',
                    userStatus: 1,
                    deptCode: ''
                },
            }
        },
        computed:{
            userStatusG(){
                return getConst("user_status_group", authDomain)
            },
            userStatusGName(){
                return formatConst(this.userStatusG);
            }
        },
        methods:{
            async handleOk(){
                const res = await authUserSave(this.form);
                this.$message.success("新增用户成功");
                this.handleClose();
                this.$emit('success')
            },
            handleClose(){
                this.visible = false
                this.form = this.$options.data().form
            },
            open(){
                this.visible = true;
            }
        }
    }
</script>

<style scoped>

</style>