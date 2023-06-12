<template>
    <a-layout class='user-container'>
        <a-card>
            <div :style="{'textAlign':'right'}">
                <a-button type="danger" @click="handleAdd">添加</a-button>
            </div>
            <a-form
                    ref='form'
                    :style="{'marginBottom':'10px'}"
                    layout='inline'
            >
                <a-form-item label="用户名" name='username'>
                    <a-input v-model:value="searchParams.username" :style="{width:'180px'}" placeholder="用户名输入框"></a-input>
                </a-form-item>
                <a-form-item label="真实姓名" name='realName'>
                    <a-input v-model:value="searchParams.realName" :style="{width:'180px'}" placeholder="真实姓名输入框"></a-input>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="pageSearchChange">搜索</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="pageSearchReset">重置</a-button>
                </a-form-item>
            </a-form>
            <a-table
                    size='middle'
                    rowKey='userId'
                    bordered
                    :pagination='false'
                    :loading='tableLoading'
                    :columns='tableColumns'
                    :data-source='tableData'
                    :scroll="{ y: 580 }"
            >

                <template #userStatus="{ text }">
                    <a-tag :color="text===1 ? 'green' : 'red'">
                        {{ text === 1? '启用': '禁用'}}
                    </a-tag>
                </template>

                <template #action="{ record }">
                    <span>
                        <a-button size="small" type="link" @click="handleEdit(record.userId)">编辑</a-button>
                        <a-button size="small" type="link" @click="handleDelete(record.userId)">删除</a-button>
                    </span>
                </template>
            </a-table>

            <a-pagination
                    a-pagination
                    :style="{marginTop:'10px',textAlign:'right'}"
                    show-size-changer
                    show-quick-jumper
                    :current="searchParams.currentPage"
                    :page-size="searchParams.pageSize"
                    :total="total"
                    @change="pageCurrentChange"
                    @showSizeChange="pageSizeChange">
            </a-pagination>

            <user-save-modal  ref="userSaveModal" @success="queryData"></user-save-modal>
        </a-card>
    </a-layout>
</template>

<script>
    import {authUserPage} from "@/api/auth"
    import userSaveModal from '@/views/auth/user/userSaveModal'
    export default {
        name: "User.vue",
        components: {
            userSaveModal
        },
        data() {
            return {
                toolbarFixed: true,
                searchParams: {
                    currentPage: 1,
                    pageSize: 20,
                    username: '',
                    userStatus: null,
                    realName: '',
                },
                total: 0,
                tableData: [],
                tableLoading: true,
                tableColumns: [{
                    title: 'ID',
                    dataIndex: 'userId',
                    key: 'userId',
                    },
                    {
                        title: '用户名',
                        dataIndex: 'username',
                        key: 'username',
                    },
                    {
                        title: '真实姓名',
                        dataIndex: 'realName',
                        key: 'realName',
                    },
                    {
                        title: '所属部门',
                        dataIndex: 'deptName',
                        key: 'deptName',
                    },
                    {
                        title: '用户状态',
                        dataIndex: 'userStatus',
                        key: 'userStatus',
                        slots: { customRender: 'userStatus' },
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
                        slots: { customRender: 'action' },
                    },
                ],
            }
        },
        computed:{

        },
        methods:{
            handleEdit(id){

            },
            handleDelete(id){

            },
            handleAdd(){
                this.$refs['userSaveModal'].open();
            },
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
                    const res = await authUserPage(this.searchParams)
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

</style>