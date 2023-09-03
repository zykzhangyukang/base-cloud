<template>
    <a-modal v-model:visible="visible"
             title="更新资源"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="提交"
    >
        <a-spin :spinning="spinning">
        <a-form :model="form" ref="formRef" :rules="formRules" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item label="所属系统" name="rescDomain">
                <a-select v-model:value="form.rescDomain"  placeholder="所属系统">
                    <a-select-option v-for="item in rescDomainG" :value="item.code" :key="item.code">{{rescDomainName[item.code]}}</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="资源名称" name="rescName">
                <a-input v-model:value="form.rescName" placeholder="资源名称" autocomplete="off" />
            </a-form-item>
            <a-form-item label="资源URL"  name="rescUrl">
                <a-input v-model:value="form.rescUrl" placeholder="资源URL" autocomplete="off" />
            </a-form-item>
            <a-form-item label="方法类型"  name="methodType">
                <a-select v-model:value="form.methodType" placeholder="请求方法类型">
                    <a-select-option v-for="item in methodTypeG" :value="item.code" :key="item.code">{{methodTypeGName[item.code]}}</a-select-option>
                </a-select>
            </a-form-item>
        </a-form>
        </a-spin>
    </a-modal>
</template>
<script>
    import {authRescSelectById, authRescUpdate} from "@/api/auth"
    import constant, {authDomain} from "@/utils/constant";

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
                    rescId: null,
                    rescUrl: '',
                    rescName: '',
                    methodType: '',
                    rescDomain: ''
                },
                formRules: {
                    rescDomain: [
                        { required: true, message: '请填写所属系统', trigger: 'blur' },
                    ],
                    rescName: [
                        { required: true, message: '请填写资源名称', trigger: 'blur' },
                    ],
                    rescUrl: [
                        { required: true, message: '请填写资源备注', trigger: 'blur' },
                    ],
                    methodType: [
                        { required: true, message: '请填写方法类型', trigger: 'blur' },
                    ],
                },
            }
        },
        computed: {
            rescDomainG(){
                return constant.getConst("project_domain",authDomain)
            },
            rescDomainName(){
                return constant.formatConst(this.rescDomainG)
            },
            methodTypeG(){
                return constant.getConst("method_type",authDomain)
            },
            methodTypeGName(){
                return constant.formatConst(this.methodTypeG)
            }
        },
        methods: {
            handleOk() {
                this.$refs.formRef
                    .validate()
                    .then(() => {
                        this.confirmLoading = true;
                        this.spinning = true;
                        authRescUpdate(this.form).then(res => {
                            this.$message.success("更新资源成功！");
                            this.handleClose();
                            this.$emit('success')
                        }).finally(e => {
                            this.confirmLoading = false;
                            this.spinning = false;
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
            open(rescId) {
                this.spinning = true;
                this.visible = true;
                this.form.rescId = rescId;
                authRescSelectById(rescId).then(res => {
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