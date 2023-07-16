<template>
    <a-layout class='role-container'>
        <a-card>
            <div :style="{'textAlign':'right'}">
                <a-button type="danger"  v-permission="'auth:role:add'">添加</a-button>
            </div>
            <a-form
                    :style="{'marginBottom':'10px'}"
                    layout='inline'
            >
                <a-form-item label="角色名称" name='realName'>
                    <a-input v-model:value="searchParams.roleName" :style="{width:'180px'}" placeholder="角色名称输入框"  autocomplete="off" ></a-input>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="pageSearchChange" v-permission="'auth:role:page'">搜索</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="pageSearchReset">重置</a-button>
                </a-form-item>
            </a-form>

            <HTable
                    :pagination='false'
                    :loading='tableLoading'
                    bordered
                    rowKey='roleId'
                    :columns='tableColumns'
                    :data-source='tableData'
            >
            </HTable>
            <HPage
                    :current="searchParams.currentPage"
                    :page-size="searchParams.pageSize"
                    :total="total"
                    @change="pageCurrentChange"
                    @showSizeChange="pageSizeChange"/>
        </a-card>
    </a-layout>
</template>

<script>
    import HTable from "@/components/table/HTable";
    import HPage from "@/components/pagination/HPage";
    import {syncPlanPage} from "@/api/sync";

    export default {
        name: "plan.vue",
        components: {
            HTable,
            HPage
        },
        data() {
            return {
                toolbarFixed: true,
                searchParams: {
                    currentPage: 1,
                    pageSize: 20,
                },
                total: 0,
                tableData: [],
                tableLoading: true,
                tableColumns: [{
                    title: 'ID',
                    dataIndex: 'roleId',
                    key: 'roleId',
                },
                    {
                        title: '角色名',
                        dataIndex: 'roleName',
                        key: 'roleName',
                    },
                    {
                        title: '角色描述',
                        dataIndex: 'roleDesc',
                        key: 'roleDesc',
                    },
                    {
                        title: '创建时间',
                        dataIndex: 'createTime',
                        key: 'createTime',
                    },
                    {
                        title: '更新时间',
                        dataIndex: 'updateTime',
                        key: 'updateTime',
                    },
                    {
                        title: '操作',
                        key: 'action',
                        align: 'center',
                        width: '200px',
                        slots: { customRender: 'action' },
                    },
                ],
            }
        },
        computed:{
        },
        methods:{
            pageSearchChange() {
                this.searchParams.currentPage = 1
                this.queryData()
            },
            pageSearchReset() {
                const page = {
                    currentPage: this.searchParams.currentPage,
                    pageSize: this.searchParams.pageSize
                }
                this.searchParams = {
                    ...this.$options.data().searchParams,
                    ...page
                }
            },
            pageCurrentChange(page, pageSize) {
                this.searchParams.currentPage = page;
                this.searchParams.pageSize = pageSize;
                this.queryData()
            },
            pageSizeChange(current, size){
                this.searchParams.pageSize = size
                this.queryData()
            },
            async queryData() {
                try {
                    this.tableLoading = true
                    const res = await syncPlanPage(this.searchParams)
                    const { totalRow, dataList } = res.result
                    this.total = totalRow
                    this.tableData = dataList
                } finally {
                    this.tableLoading = false
                }
            },
        },
        created() {
           this.pageSearchChange();
        }
    }
</script>

<style scoped>
    .action-btns {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        gap: 8px;
    }
</style>