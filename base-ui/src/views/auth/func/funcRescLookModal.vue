<template>
    <a-modal v-model:visible="visible"
             title="查看资源"
             :width="700"
             :footer="null"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="确定"
             ref="form"
    >

        <!-- 表格显示部分 -->
        <a-table  v-if="dataSource && dataSource.length > 0" size="small" class="mb15"  :dataSource="dataSource"
                   rowKey='rescId'
                   :pagination='false'
                   :columns="columns" >
            <template #rescDomain="{ text }">
                {{ rescDomainName[text] }}
            </template>
            <template #methodType="{ text }">
                {{ methodTypeGName[text] }}
            </template>
        </a-table>
        <span v-else>
             <a-empty description="暂无绑定的资源" />
               <a-divider />
        </span>
    </a-modal>
</template>
<script>

    import {authFuncSelectById, authRescSearchByKeyword} from "@/api/auth";
    import constant, {authDomain} from "@/utils/constant";

    export default {
        name: "funcBindRescModal.vue",
        data() {
            return {
                confirmLoading: false,
                visible: false,
                searchLoading: true,
                searchValues: [],
                searchList: [],
                dataSource: [],
                columns: [
                    {
                        title: '资源名称',
                        dataIndex: 'rescName',
                        key: 'rescName',
                    },
                    {
                        title: '资源URL',
                        dataIndex: 'rescUrl',
                        key: 'rescUrl',
                    },
                    {
                        title: '所属系统',
                        dataIndex: 'rescDomain',
                        key: 'rescDomain',
                        slots: { customRender: 'rescDomain' },
                    },
                    {
                        title: '请求方式',
                        dataIndex: 'methodType',
                        key: 'methodType',
                        slots: { customRender: 'methodType' },
                    }
                ],
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
            handleClose() {
                this.visible = false
                this.confirmLoading = false;
                this.form = this.$options.data().form;
            },
            open(funcId) {
                authFuncSelectById(funcId).then(res=>{
                    this.dataSource = res.result.rescVOList;
                    this.visible = true;
                })
            }
        }
    }
</script>

<style scoped>

</style>