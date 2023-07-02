<template>
    <a-layout class='resc-container'>
        <a-card>
            <div :style="{'textAlign':'right'}">
                <a-button type="danger" @click="handleAdd">添加</a-button>
            </div>
            <a-form
                    ref='form'
                    :style="{'marginBottom':'10px'}"
                    layout='inline'
            >
                <a-form-item label="所属系统">
                    <a-select v-model:value="searchParams.rescDomain" :style="{width:'180px'}" placeholder="所属系统">
                        <a-select-option v-for="item in rescDomainG" :value="item.code" :key="item.code">{{rescDomainName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="资源名称" >
                    <a-input v-model:value="searchParams.rescName" :style="{width:'200px'}" placeholder="资源名称输入框"></a-input>
                </a-form-item>
                <a-form-item label="资源URL" >
                    <a-input v-model:value="searchParams.rescUrl" :style="{width:'280px'}" placeholder="资源URL输入框"></a-input>
                </a-form-item>
                <a-form-item label="方法类型" >
                    <a-select v-model:value="searchParams.methodType" :style="{width:'180px'}" placeholder="请求方法类型">
                        <a-select-option v-for="item in methodTypeG" :value="item.code" :key="item.code">{{methodTypeGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="pageSearchChange">搜索</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="pageSearchReset">重置</a-button>
                </a-form-item>
            </a-form>
            <HTable
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
                    <span>
                         <a-divider type="vertical"/>
                         <a href="#" class="btn-text-small" @click="handleUpdate(record.rescId)"><EditOutlined/>编辑</a>
                       <a-divider type="vertical"/>
                         <a-popconfirm
                                 title="您确定要删除该资源吗?"
                                 ok-text="确定"
                                 cancel-text="取消"
                                 @confirm="handleDelete(record.rescId)">
                             <a href="#" class="btn-text-small"><DeleteOutlined/>删除</a>
                          </a-popconfirm>
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

            <!-- 新增资源 -->
            <resc-save-modal ref="rescSaveModal" @success="queryData"></resc-save-modal>
            <!-- 更新资源 -->
            <resc-update-modal ref="rescUpdateModal" @success="queryData"></resc-update-modal>
        </a-card>
    </a-layout>
</template>

<script>

    import {authRescDelete, authRescPage} from "@/api/auth";
    import constant, {authDomain} from "@/utils/constant";
    import rescSaveModal from "@/views/auth/resc/RescSaveModal";
    import rescUpdateModal from "@/views/auth/resc/RescUpdateModal";
    import {DeleteOutlined, EditOutlined} from '@ant-design/icons-vue';
    import HPage from "@/components/pagination/HPage";
    import HTable from "@/components/table/HTable";

    export default {
        name: "Resc..vue",
        components: {
            rescSaveModal,
            rescUpdateModal,
            DeleteOutlined,
            EditOutlined,
            HPage,
            HTable
        },
        data() {
            return {
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
                        width: '150px',
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
            handleUpdate(id){
                this.$refs['rescUpdateModal'].open(id);
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

</style>