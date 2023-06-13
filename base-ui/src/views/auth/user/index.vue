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
                <a-form-item label="所属部门">
                    <a-select v-model:value="searchParams.userStatus" :style="{width:'180px'}" placeholder="用户状态">
                        <a-select-option v-for="item in userStatusG" :value="item.code" :key="item.code">{{userStatusGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="pageSearchChange">搜索</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="pageSearchReset">重置</a-button>
                </a-form-item>
            </a-form>
            <a-table
                    size='small'
                    rowKey='userId'
                    bordered
                    :pagination='false'
                    :loading='tableLoading'
                    :columns='tableColumns'
                    :data-source='tableData'
                    :scroll="{ y: 580 }"
            >

                <template #userStatus="{ text }">
                    <a-tag  :color="text===1 ? 'green' : 'red'">
                        {{ userStatusGName[text] }}
                    </a-tag>
                </template>

                <template #action="{ record }">
                    <span>
                        <a-button size="small" type="link" @click="handleUpdate(record.userId)">编辑</a-button>
                         <a-popconfirm
                                 title="您确定要删除该用户吗?"
                                 ok-text="确定"
                                 cancel-text="取消"
                                 @confirm="handleDelete(record.userId)">
                             <a-button size="small" type="link">删除</a-button>
                          </a-popconfirm>
                        <a-dropdown :trigger="['click']">
                            <a class="ant-dropdown-link" @click.prevent >
                              设置
                              <DownOutlined />
                            </a>
                            <template #overlay>
                              <a-menu>
                                <a-menu-item>
                                  <a href="javascript:;" @click="handleEnable(record.userId)">启用账号</a>
                                </a-menu-item>
                                <a-menu-item>
                                  <a href="javascript:;" @click="handleDisable(record.userId)">禁用账号</a>
                                </a-menu-item>
                                     <a-menu-item>
                                  <a href="javascript:;" @click="handleUpdatePwd(record.userId)">修改密码</a>
                                </a-menu-item>
                              </a-menu>
                            </template>
                          </a-dropdown>
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
            <user-update-modal ref="userUpdateModal" @success="queryData"></user-update-modal>
            <update-pwd ref="updatePwd"></update-pwd>
        </a-card>
    </a-layout>
</template>

<script>
    import {authUserDelete, authUserDisable, authUserEnable, authUserPage} from "@/api/auth"
    import userSaveModal from '@/views/auth/user/userSaveModal'
    import userUpdateModal from "@/views/auth/user/userUpdateModal";
    import updatePwd from "@/views/auth/user/updatePwd";
    import {authDomain, formatConst, getConst} from "@/utils/constant";
    import { DownOutlined,ExclamationCircleOutlined } from '@ant-design/icons-vue';
    import { Modal } from 'ant-design-vue';
    import { createVNode, defineComponent } from 'vue';
    export default {
        name: "User.vue",
        components: {
            userSaveModal,
            DownOutlined,
            userUpdateModal,
            updatePwd
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
                        width: '250px',
                        slots: { customRender: 'action' },
                    },
                ],
            }
        },
        computed:{
            userStatusG(){
                return getConst("user_status_group", authDomain)
            },
            userStatusGName(){
                return formatConst(this.userStatusG);
            }
        },
        methods:{
            handleEnable(userId){
                const _this = this;
                Modal.confirm({
                    title:  '是否启用用户?',
                    cancelText: "取消",
                    okText: "确定",
                    icon: () => createVNode(ExclamationCircleOutlined),
                    onOk() {
                        authUserEnable(userId).then(res=>{
                            _this.$message.success("启用用户成功！");
                            _this.queryData();
                        })
                    },
                    onCancel() {},
                })
            },
            handleUpdatePwd(userId){
                this.$refs['updatePwd'].open(userId);
            },
            handleDisable(userId){
                const _this = this;
                Modal.confirm({
                        title:  '是否锁定用户?',
                        cancelText: "取消",
                        okText: "确定",
                        icon: () => createVNode(ExclamationCircleOutlined),
                        onOk() {
                            authUserDisable(userId).then(res=>{
                                _this.$message.success("锁定用户成功！");
                                _this.queryData();
                            })
                        },
                        onCancel() {},
            })
            },
            handleUpdate(id){
                this.$refs['userUpdateModal'].open(id);
            },
            handleDelete(id){
                authUserDelete(id).then(e=>{
                    this.$message.success("删除用户成功！");
                    this.queryData();
                })
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