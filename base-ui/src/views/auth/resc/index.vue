<template>
    <a-layout class='resc-container'>
        <a-card>
            <div :style="{'textAlign':'right'}">
                <a-button type="primary" @click="handleAdd" v-permission="'auth:resc:add'"><PlusOutlined />新增资源</a-button>
            </div>
            <a-form
                    :style="{'marginBottom':'10px'}"
                    layout='inline'
            >
                <a-form-item label="所属系统">
                    <a-select v-model:value="searchParams.rescDomain" :style="{width:'180px'}" placeholder="所属系统">
                        <a-select-option v-for="item in rescDomainG" :value="item.code" :key="item.code">{{rescDomainName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="资源URL" >
                    <a-input v-model:value="searchParams.rescUrl" :style="{width:'200px'}" placeholder="资源URL输入框"  autocomplete="off" ></a-input>
                </a-form-item>
                <a-form-item label="资源名称" >
                    <a-input v-model:value="searchParams.rescName" :style="{width:'200px'}" placeholder="资源名称输入框"  autocomplete="off" ></a-input>
                </a-form-item>
                <a-form-item label="方法类型" >
                    <a-select v-model:value="searchParams.methodType" :style="{width:'180px'}" placeholder="请求方法类型">
                        <a-select-option v-for="item in methodTypeG" :value="item.code" :key="item.code">{{methodTypeGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="pageSearchChange" v-permission="'auth:resc:page'"><template #icon><SearchOutlined /></template>搜索</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="pageSearchReset">重置</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="handleRescRefresh" v-permission="'auth:resc:refresh'" :loading="btnLoading">资源刷新</a-button>
                </a-form-item>
            </a-form>
            <HTable
                    bordered
                    rowKey='rescId'
                    :pagination='false'
                    :loading='tableLoading'
                    :columns='tableColumns'
                    :data-source='tableData'
            >
                <template #rescDomain="{ text }">
                    {{ rescDomainName[text] }}
                </template>
                <template #methodType="{ text }">
                    {{ methodTypeGName[text] }}
                </template>
                <template #action="{ record }">
                    <div class="action-btns">
                        <a  class="btn-text-mini" href="javascript:;" @click="handleUpdate(record.rescId)" v-permission="'auth:resc:update'"><EditOutlined/>编辑</a>
                        <a  class="btn-text-mini" href="javascript:;" @click="handleFuncSearch(record.rescUrl)" v-permission="'auth:resc:funcSearch'"><LinkOutlined />功能</a>
                         <a-popconfirm
                                 title="您确定要删除该资源吗?"
                                 ok-text="确定"
                                 cancel-text="取消"
                                 @confirm="handleDelete(record.rescId)">
                             <a class="btn-text-mini" href="javascript:;" v-permission="'auth:resc:delete'"><DeleteOutlined/>删除</a>
                          </a-popconfirm>
                    </div>
                </template>
            </HTable>
            <HPage
                    :current="searchParams.currentPage"
                    :page-size="searchParams.pageSize"
                    :total="total"
                    @current-change="pageCurrentChange"
                    @size-change="pageSizeChange">
            </HPage>
            <!-- 新增资源 -->
            <resc-save-modal ref="rescSaveModal" @success="queryData"></resc-save-modal>
            <!-- 更新资源 -->
            <resc-update-modal ref="rescUpdateModal" @success="queryData"></resc-update-modal>
        </a-card>
    </a-layout>
</template>

<script>

    import {authRescDelete, authRescPage, authRescRefresh} from "@/api/auth";
    import constant, {authDomain} from "@/utils/constant";
    import rescSaveModal from "@/views/auth/resc/RescSaveModal";
    import rescUpdateModal from "@/views/auth/resc/RescUpdateModal";
    import HPage from "@/components/pagination/HPage";
    import HTable from "@/components/table/HTable";

    export default {
        name: "Resc..vue",
        components: {
            rescSaveModal,
            rescUpdateModal,
            HPage,
            HTable
        },
        data() {
            return {
                btnLoading: false,
                toolbarFixed: true,
                searchParams: {
                    currentPage: 1,
                    pageSize: 20,
                    rescName: '',
                    rescUrl: '',
                    rescDomain: '',
                    methodType: ''
                },
                total: 0,
                tableData: [],
                tableLoading: true,
                tableColumns: [{
                    title: 'ID',
                    dataIndex: 'rescId',
                    key: 'rescId',
                },
                    {
                        title: '资源名称',
                        dataIndex: 'rescName',
                        key: 'rescName',
                    },
                    {
                        title: '资源URL',
                        dataIndex: 'rescUrl',
                        key: 'rescUrl',
                        ellipsis:  true,
                    },
                    {
                        title: '请求方法类型',
                        dataIndex: 'methodType',
                        key: 'methodType',
                        slots: { customRender: 'methodType' },
                    },
                    {
                        title: '所属系统',
                        dataIndex: 'rescDomain',
                        key: 'rescDomain',
                        slots: { customRender: 'rescDomain' },
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
        methods:{
            handleRescRefresh() {
                this.btnLoading = true;
                authRescRefresh().then(e=>{
                    this.$message.success("刷新系统资源成功！");
                }).finally(()=>{
                    this.btnLoading  = false;
                })
            },
            handleUpdate(id){
                this.$refs['rescUpdateModal'].open(id);
            },
            handleFuncSearch(rescUrl){
                this.$router.push({ name: "AuthFunc", params: { rescUrl: rescUrl } })
            },
            handleDelete(id){
                authRescDelete(id).then(e=>{
                    this.$message.success("删除资源成功！");
                    this.queryData();
                })
            },
            handleAdd(){
                this.$refs['rescSaveModal'].open();
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
                    const res = await authRescPage(this.searchParams)
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