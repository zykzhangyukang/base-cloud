<template>
    <a-modal v-model:visible="visible"
             title="绑定资源"
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
                label-in-value
                style="width: 100%"
                placeholder="查找资源"
                :filter-option="false"
                :not-found-content="searchLoading ? undefined : null"
                :options="searchList"
                @search="handleSearchResc"
        >
            <template v-if="searchLoading" #notFoundContent>
                <a-spin size="small" />
            </template>
        </a-select>

        <!-- 表格显示部分 -->
        <a-table   v-if="this.dataSource && this.dataSource.length > 0" size="small"  :dataSource="dataSource"
                 rowKey='rescId'
                 :pagination='false'
                 :columns="columns" >
            <template #rescDomain="{ text }">
                {{ rescDomainName[text] }}
            </template>
            <template #methodType="{ text }">
                {{ methodTypeGName[text] }}
            </template>
            <template #action="{ record }">
                                <span>
                                     <a-popconfirm
                                             title="您确定要删除该绑定关系吗?"
                                             ok-text="确定"
                                             cancel-text="取消"
                                             @confirm="handleDelete(record.rescId)">
                                         <a-button size="small"  class="btn-text-small"  type="link">删除</a-button>
                                      </a-popconfirm>
                                </span>
            </template>
        </a-table>
        <span v-else>
              <a-empty description="暂无绑定的资源" />
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
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: '100px',
                        slots: { customRender: 'action' },
                    },
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
            handleSearchResc(val){
                authRescSearchByKeyword(val).then(res=>{
                    this.searchList = res.result;
                })
            },
            handleOk() {

            },
            handleDelete(rescId){
                this.dataSource.splice(this.dataSource.findIndex(v => v.rescId === rescId), 1);
                this.$message.success("删除绑定成功！")
            },
            handleClose() {
                this.visible = false
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