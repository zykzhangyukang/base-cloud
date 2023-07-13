<template>
    <a-layout class='user-container'>
        <a-card>
            <div :style="{'textAlign':'right'}">
                <a-button type="danger" @click="handleAdd" v-permission="'auth:user:add'">添加</a-button>
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
                <a-form-item label="用户状态">
                    <a-select v-model:value="searchParams.userStatus" :style="{width:'180px'}" placeholder="用户状态">
                        <a-select-option v-for="item in userStatusG" :value="item.code" :key="item.code">{{userStatusGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="pageSearchChange" v-permission="'auth:user:page'">搜索</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="pageSearchReset">重置</a-button>
                </a-form-item>
            </a-form>
            <HTable
                    rowKey='userId'
                    bordered
                    :pagination='false'
                    :loading='tableLoading'
                    :columns='tableColumns'
                    :data-source='tableData'
            >
                <template #userStatus="{ text }">
                    <a-tag :color="text===1 ? 'green' : 'red'">
                        {{ userStatusGName[text] }}
                    </a-tag>
                </template>
                <template #action="{ record }">
                    <span>
                      <a-divider type="vertical"/>
                       <a href="#" class="btn-text-small" v-permission="'auth:user:update'" @click="handleUpdate(record.userId)" ><EditOutlined/>编辑</a>
                       <a-divider type="vertical"/>
                         <a-popconfirm
                                 title="您确定要删除该用户吗?"
                                 ok-text="确定"
                                 cancel-text="取消"
                                 @confirm="handleDelete(record.userId)">
                               <a href="#" class="btn-text-small"  v-permission="'auth:user:delete'"><DeleteOutlined/>删除</a>
                          </a-popconfirm>
                        <a-divider type="vertical"/>
                        <a-dropdown :trigger="['click']">
                            <a class="ant-dropdown-link btn-text-small" @click.prevent>
                              <SettingOutlined/>设置
                            </a>
                            <template #overlay>
                              <a-menu>
                                <a-menu-item>
                                  <a href="javascript:;" @click="handleEnable(record.userId)" v-permission="'auth:user:enable'">启用账号</a>
                                </a-menu-item>
                                <a-menu-item>
                                  <a href="javascript:;" @click="handleDisable(record.userId)" v-permission="'auth:user:disable'">禁用账号</a>
                                </a-menu-item>
                                     <a-menu-item>
                                  <a href="javascript:;" @click="handleUpdatePwd(record.userId)" v-permission="'auth:user:updatePwd'">修改密码</a>
                                </a-menu-item>
                                <a-menu-item>
                                  <a href="javascript:;" @click="handleAssignRole(record.userId)" v-permission="'auth:user:assignRole'">分配角色</a>
                                </a-menu-item>
                              </a-menu>
                            </template>
                          </a-dropdown>
                    </span>
                </template>
            </HTable>
            <HPage
                    :current="searchParams.currentPage"
                    :page-size="searchParams.pageSize"
                    :total="total"
                    @change="pageCurrentChange"
                    @showSizeChange="pageSizeChange">
            </HPage>
            <!-- 新增用户 -->
            <user-save-modal  ref="userSaveModal" @success="queryData"></user-save-modal>
            <!-- 更新用户 -->
            <user-update-modal ref="userUpdateModal" @success="queryData"></user-update-modal>
            <!-- 更新密码 -->
            <update-pwd ref="updatePwd"></update-pwd>
            <!-- 用户分配角色 -->
            <user-assign-role ref="userAssignRole"></user-assign-role>
        </a-card>
    </a-layout>
</template>

<script>
import {authUserDelete, authUserDisable, authUserEnable, authUserPage} from "@/api/auth"
import userSaveModal from '@/views/auth/user/UserSaveModal'
import userUpdateModal from "@/views/auth/user/UserUpdateModal";
import userAssignRole from "@/views/auth/user/UserAssignRole";
import updatePwd from "@/views/auth/user/UpdatePwd";
import HPage from "@/components/pagination/HPage";
import HTable from "@/components/table/HTable";
import {authDomain, formatConst, getConst} from "@/utils/constant";
import {DeleteOutlined, EditOutlined, ExclamationCircleOutlined, SettingOutlined} from '@ant-design/icons-vue';
import {Modal} from 'ant-design-vue';
import {createVNode} from 'vue';

export default {
        name: "User.vue",
        components: {
            userSaveModal,
            userUpdateModal,
            updatePwd,
            EditOutlined,
            DeleteOutlined,
            SettingOutlined,
            userAssignRole,
            HPage,
            HTable
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
                        width: '200px',
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
            handleAssignRole(userId){
                this.$refs['userAssignRole'].open(userId);
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