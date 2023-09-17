<template>
    <a-modal v-model:visible="visible"
             title="新增用户"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="提交"
    >
        <a-spin :spinning="spinning" >
        <a-form :model="form" :label-col="labelCol" :wrapper-col="wrapperCol"  ref="formRef" :rules="formRules">
            <a-form-item label="登录账号" name="username">
                <a-input v-model:value="form.username" autocomplete="off" />
            </a-form-item>
            <a-form-item label="登录密码"  name="password">
                <a-input-password v-model:value="form.password" :visibility-toggle="true" autocomplete="off" />
            </a-form-item>
            <a-form-item label="真实姓名" name="realName">
                <a-input v-model:value="form.realName" autocomplete="off" />
            </a-form-item>
            <a-form-item label="所属部门"  name="deptCode">
                <a-select v-model:value="form.deptCode" placeholder="请选择部门" :style="{width:'200px'}" autocomplete="off">
                    <a-select-option v-for="item in deptList" :value="item.deptCode" :key="item.deptId">{{item.deptName}}</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="账号状态"  name="userStatus">
                <a-radio-group v-model:value="form.userStatus">
                    <a-radio v-for="item in userStatusG" :value="item.code" :key="item.code">{{userStatusGName[item.code]}}</a-radio>
                </a-radio-group>
            </a-form-item>
        </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
    import {authDeptList, authUserSave} from "@/api/auth"
    import {authDomain, formatConst, getConst} from "@/utils/constant";
    export default {
        name: "userSaveModal.vue",
        data() {
            return {
                spinning: false,
                formRules: {
                    username: [
                        { required: true, message: '请填写用户名', trigger: 'blur' },
                    ],
                    password: [
                        { required: true, message: '请填写密码', trigger: 'blur' },
                    ],
                    realName: [
                        { required: true, message: '请填写真实姓名', trigger: 'blur' },
                    ],
                    deptCode: [
                        { required: true, message: '请选择所属部门', trigger: 'change' },
                    ]
                },
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
                this.$refs.formRef
                    .validate()
                    .then(() => {
                        this.spinning = true;
                        this.confirmLoading = true;
                        authUserSave(this.form).then(res => {
                            this.$message.success("新增用户成功");
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
                authDeptList().then(res=>{
                    this.deptList=res.result;
                })
            }
        }
    }
</script>

<style scoped>

</style>