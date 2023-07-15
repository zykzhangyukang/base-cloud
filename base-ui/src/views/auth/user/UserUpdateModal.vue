<template>
    <a-modal v-model:visible="visible"
             title="编辑用户"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="提交"
    >
        <a-form :model="form" :label-col="labelCol"  ref="formRef" :rules="formRules"  :wrapper-col="wrapperCol">
            <a-form-item label="登录账号" name="username">
                <a-input v-model:value="form.username" :disabled="true" />
            </a-form-item>
            <a-form-item label="真实姓名" name="realName">
                <a-input v-model:value="form.realName" />
            </a-form-item>
            <a-form-item label="所属部门" name="deptCode">
                <a-select v-model:value="form.deptCode" placeholder="请选择部门" :style="{width:'200px'}">
                    <a-select-option v-for="item in deptList" :value="item.deptCode" :key="item.deptId">{{item.deptName}}</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="账号状态">
                <a-radio-group v-model:value="form.userStatus">
                    <a-radio v-for="item in userStatusG" :value="item.code" :key="item.code">{{userStatusGName[item.code]}}</a-radio>
                </a-radio-group>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script>
    import {authDeptList, authUserSave, authUserSelectById, authUserUpdate} from "@/api/auth"
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
                formRules: {
                    username: [
                        { required: true, message: '请填写用户名', trigger: 'blur' },
                    ],
                    realName: [
                        { required: true, message: '请填写真实姓名', trigger: 'blur' },
                    ],
                    deptCode: [
                        { required: true, message: '请选择所属部门', trigger: 'change' },
                    ]
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
                this.$refs.formRef
                    .validate()
                    .then(() => {
                        this.confirmLoading = true;
                        authUserUpdate(this.form).then(res => {
                            this.$message.success("更新用户成功！");
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
            },
            open(userId){
                authUserSelectById(userId).then(res=>{
                    this.form = res.result;
                    return authDeptList();
                }).then(res=>{
                    this.visible = true;
                    this.deptList=res.result;
                })
            }
        }
    }
</script>

<style scoped>

</style>