<template>
    <a-modal v-model:visible="visible"
             title="新增资源"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="提交"
             ref="form"
    >
        <a-form :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item label="所属系统">
                <a-select v-model:value="form.rescDomain"  placeholder="所属系统">
                    <a-select-option v-for="item in rescDomainG" :value="item.code" :key="item.code">{{rescDomainName[item.code]}}</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="资源名称">
                <a-input v-model:value="form.rescName" />
            </a-form-item>
            <a-form-item label="资源URL" >
                <a-input v-model:value="form.rescUrl" />
            </a-form-item>
            <a-form-item label="方法类型" >
                <a-select v-model:value="form.methodType" placeholder="请求方法类型">
                    <a-select-option v-for="item in methodTypeG" :value="item.code" :key="item.code">{{methodTypeGName[item.code]}}</a-select-option>
                </a-select>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script>
    import {authRescSave} from "@/api/auth";
    import constant, {authDomain} from "@/utils/constant";

    export default {
        name: "rescSaveModel.vue",
        data() {
            return {
                confirmLoading:  false,
                visible: false,
                labelCol: { span: 4 },
                wrapperCol: { span: 18 },
                form:{
                    rescName: '',
                    rescUrl: '',
                    methodType: '',
                    domain: '',
                },
            }
        },
        computed:{
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
        methods:{
            handleOk() {
                this.confirmLoading = true;
                authRescSave(this.form).then(res => {
                    this.$message.success("新增资源成功！");
                    this.handleClose();
                    this.$emit('success')
                }).finally(e=>{
                    this.confirmLoading =false;
                })
            },
            handleClose(){
                this.visible = false
                this.confirmLoading = false;
                this.form = this.$options.data().form;
            },
            open(){
                this.visible = true;
            }
        }
    }
</script>

<style scoped>

</style>