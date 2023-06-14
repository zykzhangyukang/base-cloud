<template>
    <a-modal v-model:visible="visible"
             title="更新功能"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="提交"
             ref="form"
    >
        <a-form :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item label="功能名称">
                <a-input v-model:value="form.funcName"/>
            </a-form-item>
            <a-form-item label="功能Key">
                <a-input v-model:value="form.funcKey" />
            </a-form-item>
            <a-form-item label="功能类型">
                <a-select v-model:value="form.funcType"   >
                    <a-select-option v-for="item in funcTypeG"  :value="item.code" :key="item.code">{{funcTypeGName[item.code]}}</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="是否隐藏" v-if="form.funcType === 'dir'">
                <a-select v-model:value="form.funcDirStatus" >
                    <a-select-option v-for="item in funcDirStatusG"  :value="item.code" :key="item.code">{{funcDirStatusGName[item.code]}}</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="功能图标" v-if="form.funcType === 'dir'">
                <a-input v-model:value="form.funcIcon" />
            </a-form-item>
            <a-form-item label="功能排序">
                <a-input-number  v-model:value="form.funcSort" :style="{width:'180px'}" :min="0" :max="100" />
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>

    import {authFuncSelectById, authFuncUpdate} from "@/api/auth";
    import constant, {authDomain} from "@/utils/constant";

    export default {
        name: "funcUpdateModal.vue",
        data() {
            return {
                confirmLoading: false,
                visible: false,
                labelCol: {span: 4},
                wrapperCol: {span: 18},
                form: {
                },
            }
        },
        computed:{
            funcTypeG(){
                return constant.getConst("func_type_group",authDomain)
            },
            funcTypeGName(){
                return constant.formatConst(this.funcTypeG)
            },
            funcDirStatusG(){
                return constant.getConst("func_dir_status_group",authDomain)
            },
            funcDirStatusGName(){
                return constant.formatConst(this.funcDirStatusG)
            },
        },
        methods: {
            handleOk() {
                this.confirmLoading = true;
                authFuncUpdate(this.form).then(res => {
                    this.$message.success("更新功能成功！");
                    this.handleClose();
                    this.$emit('success')
                }).finally(e => {
                    this.confirmLoading = false;
                })
            },
            handleClose() {
                this.visible = false
                this.form = this.$options.data().form;
            },
            open(funcId) {
                authFuncSelectById(funcId).then(res => {
                    this.form = res.result;
                    this.visible = true;
                });
            }
        }
    }
</script>

<style scoped>

</style>