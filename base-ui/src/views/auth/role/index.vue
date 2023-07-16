<template>
    <a-layout class='role-container'>
        <a-card>
            <div :style="{'textAlign':'right'}">
                <a-button type="danger" @click="handleAdd" v-permission="'auth:role:add'">添加</a-button>
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
                <template #action="{ record }">
                    <div class="action-btns">
                        <a  class="btn-text-mini" href="javascript:;" @click="handleUpdate(record.roleId)" v-permission="'auth:role:update'"><EditOutlined/>编辑 </a>
                       <a-popconfirm
                               title="您确定要删除该角色吗?"
                               ok-text="确定"
                               cancel-text="取消"
                               @confirm="handleDelete(record.roleId)">
                               <a class="btn-text-mini" href="javascript:;" v-permission="'auth:role:delete'" ><DeleteOutlined/>删除</a>
                        </a-popconfirm>
                        <a  class="btn-text-mini" href="javascript:;" @click="this.$router.push(`/auth/role/authorized?roleId=${record.roleId}`)" v-permission="'auth:role:funcUpdate'"><LockOutlined/>授权</a>
                    </div>
                </template>
            </HTable>
            <HPage
                    :current="searchParams.currentPage"
                    :page-size="searchParams.pageSize"
                    :total="total"
                    @change="pageCurrentChange"
                    @showSizeChange="pageSizeChange"/>

            <role-save-modal ref="roleSaveModal" @success="queryData"></role-save-modal>
            <role-update-modal ref="roleUpdateModal" @success="queryData"></role-update-modal>
        </a-card>
    </a-layout>
</template>

<script>
    import {authRoleDelete, authRolePage} from "@/api/auth";
    import roleSaveModal from "@/views/auth/role/RoleSaveModal";
    import roleUpdateModal from "@/views/auth/role/RoleUpdateModal";
    import HTable from "@/components/table/HTable";
    import HPage from "@/components/pagination/HPage";

    export default {
        name: "role.vue",
        components: {
            roleUpdateModal,
            roleSaveModal,
            HTable,
            HPage
        },
        data() {
            return {
                toolbarFixed: true,
                searchParams: {
                    currentPage: 1,
                    pageSize: 20,
                    roleName: ''
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
            handleUpdate(id){
                this.$refs['roleUpdateModal'].open(id);
            },
            handleDelete(id){
                authRoleDelete(id).then(res=>{
                    this.$message.success("删除角色成功！");
                    this.queryData();
                })
            },
            handleAdd(){
                this.$refs['roleSaveModal'].open();
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
                    const res = await authRolePage(this.searchParams)
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