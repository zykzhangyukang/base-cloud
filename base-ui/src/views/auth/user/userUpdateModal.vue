<template>
    <a-modal v-model:visible="visible"
             title="更新用户"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="提交"
             ref="form"
    >
        <a-form :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item label="用户名">
                <a-input v-model:value="form.username" :disabled="true" />
            </a-form-item>
            <a-form-item label="真实姓名">
                <a-input v-model:value="form.realName" />
            </a-form-item>
            <a-form-item label="所属部门">
                <a-select v-model:value="form.deptCode" placeholder="请选择部门" :style="{width:'200px'}">
                    <a-select-option v-for="item in deptList" :value="item.deptCode" :key="item.deptId">{{item.deptName}}</a-select-option>
                </a-select>
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
    import {authDeptList, authUserSelectById, authUserUpdate} from "@/api/auth"
    import {authDomain, formatConst, getConst} from "@/utils/constant";
    export default {
        name: "userUpdateModal.vue",
        data() {
            return {
                deptList: [],
                confirmLoading: false,
                visible: false,
                labelCol: { span: 4 },
                wrapperCol: { span: 18 },
                form:{
                    username: '',
                    password: '',
                    userStatus: 0,
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
            handleOk() {
                this.confirmLoading = true;
                authUserUpdate(this.form).then(res => {
                    this.$message.success("更新用户成功！");
                    this.handleClose();
                    this.$emit('success')
                }).finally(e=>{
                    this.confirmLoading =  false;
                })
            },
            handleClose(){
                this.visible = false
                this.form = this.$options.data().form;
            },
            open(userId){

                authUserSelectById(userId).then(res=>{
                    this.form = res.result;
                    return authDeptList();
                }).then(res=>{

                    console.log(res)
                    this.visible = true;
                    this.deptList=res.result;
                })
            }
        }
    }
</script>

<style scoped>

</style>