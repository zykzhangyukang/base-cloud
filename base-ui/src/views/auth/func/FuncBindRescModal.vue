<template>
    <a-modal v-model:visible="visible"
             title="绑定资源"
             :maskClosable="false"
             @ok="handleOk"
             :width="700"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="提交"
             ref="form"
    >

        <!-- 搜索部分 -->
        <a-select
                allowClear
                class="mb15"
                v-model:value="form.rescVOList"
                mode="multiple"
                :autoClearSearchValue="true"
                label-in-value
                style="width: 100%"
                placeholder="搜索绑定资源"
                :filter-option="false"
                :not-found-content="searchLoading ? undefined : '暂无数据'"
                :options="options"
                @search="handleSearchResc"
        >
            <template v-if="searchLoading" #notFoundContent>
                <a-spin size="small" />
            </template>
        </a-select>

    </a-modal>
</template>
<script>

import {authFuncSelectById, authFuncRescBindUpdate, authRescSearchByKeyword} from "@/api/auth";
let timer = null;
export default {
        name: "funcBindRescModal.vue",
        data() {
            return {
                confirmLoading: false,
                visible: false,
                searchLoading: false,
                form: {
                    funcId: null,
                    rescVOList: [],
                },
                options: [],
            }
        },
        methods: {
            handleSearchResc(val){
                 let _this = this;
                 if(timer!=null){
                   clearTimeout(timer)
                 }
                 timer = setTimeout(()=>{
                   _this.queryRescByKeywords(val);
                 },500);
            },
            queryRescByKeywords(val){
                this.searchLoading = true;
                authRescSearchByKeyword(val).then(res=>{
                    const arr = [];
                    if(res.result && res.result.length > 0){
                        res.result.forEach(item=>{
                            arr.push({
                                key: item.rescId,
                                label: item.rescUrl + "【"+item.rescName+"】"
                            })
                        })
                    }
                    this.options = arr
                }).finally(e=>{
                    this.searchLoading = false;
                })
            },
            handleOk() {
                this.confirmLoading = true;
              authFuncRescBindUpdate(this.form).then(res => {
                    this.$message.success("绑定资源成功！");
                    this.handleClose();
                    this.$emit('success')
                }).finally(e=>{
                    this.confirmLoading = false;
                })
            },
            handleClose() {
                this.visible = false
                this.confirmLoading = false;
                this.form = this.$options.data().form;
                this.searchList = [];
                this.searchValues = [];
            },
            open(funcId) {
                authFuncSelectById(funcId).then(res=>{
                    let list  = res.result.rescVOList;
                    let arr = [];
                    if(list && list.length >0){
                        list.forEach(item=>{
                            arr.push({
                                key: item.rescId,
                                label: item.rescUrl + "【"+item.rescName+"】"
                            })
                        })
                    }
                    this.options = arr;
                    this.form.rescVOList = arr;
                    this.visible = true;
                    this.form.funcId  = funcId;
                })
            }
        }
    }
</script>

<style scoped>

</style>