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
                class="mb15"
                v-model:value="searchValues"
                mode="multiple"
                :autoClearSearchValue="true"
                label-in-value
                style="width: 100%"
                placeholder="搜索绑定资源"
                :filter-option="false"
                :not-found-content="searchLoading ? undefined : null"
                :options="searchList"
                @change="handleChange"
                @search="handleSearchResc"
        >
            <template v-if="searchLoading" #notFoundContent>
                <a-spin size="small" />
            </template>
        </a-select>

    </a-modal>
</template>
<script>

    import {authFuncSelectById, authRescSearchByKeyword} from "@/api/auth";
    import { debounce } from 'lodash-es';
    export default {
        name: "funcBindRescModal.vue",
        data() {
            return {
                confirmLoading: false,
                visible: false,
                searchLoading: true,
                searchValues: [],
                searchList: [],
            }
        },
        methods: {
            handleChange(val){
                console.log(this.searchValues)
            },
            handleSearchResc(val){
                authRescSearchByKeyword(val).then(res=>{
                    const arr = [];
                    if(res.result && res.result.length > 0){
                        res.result.forEach(item=>{
                            arr.push({
                                key: item.rescId,
                                label: item.rescUrl + "【"+item.rescName+"】"
                            })
                        })
                        this.searchList = arr
                    }
                })
            },
            handleOk() {

            },
            handleClose() {
                this.visible = false
                this.form = this.$options.data().form;
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
                    this.searchList = arr;
                    this.searchValues = arr;
                    this.visible = true;
                })
            }
        }
    }
</script>

<style scoped>

</style>