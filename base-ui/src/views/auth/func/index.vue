<template>
    <a-layout>
            <a-row :gutter="16">
                <a-col :span="3">
                    <div class="left">
                        <left-func-tree ref="leftFuncTree" @select-node="handleSelectNode"></left-func-tree>
                    </div>
                </a-col>
                <a-col :span="21">
                    <a-card style=" height: 85vh;">
                        <div :style="{'textAlign':'right'}">
                            <a-button type="danger" @click="handleAdd" :disabled="searchParams.parentId === null">
                                添加
                            </a-button>
                        </div>
                        <a-form
                                ref='form'
                                :style="{'marginBottom':'10px'}"
                                layout='inline'
                        >
                            <a-form-item label="功能名称">
                                <a-input v-model:value="searchParams.funcName" :style="{width:'200px'}" placeholder="功能名称输入框"></a-input>
                            </a-form-item>
                            <a-form-item label="功能Key">
                                <a-input v-model:value="searchParams.funcKey" :style="{width:'200px'}" placeholder="功能Key输入框"></a-input>
                            </a-form-item>
                            <a-form-item label="功能类型">
                                <a-select v-model:value="searchParams.funcType" :style="{width:'180px'}" placeholder="功能类型">
                                    <a-select-option v-for="item in funcTypeG" :value="item.code" :key="item.code">{{funcTypeGName[item.code]}}</a-select-option>
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
                                bordered
                                rowKey='funcId'
                                :pagination='false'
                                :loading='tableLoading'
                                :columns='tableColumns'
                                :data-source='tableData'
                                :scroll="{ y: 580 }"
                        >
                            <template #funcType="{ text }">
                                {{ funcTypeGName[text] }}
                            </template>
                            <template #action="{ record }">
                                <span>
                                    <a-button size="small" type="link" @click="handleUpdate(record.funcId)">编辑</a-button>
                                     <a-popconfirm
                                             title="您确定要删除该功能吗?"
                                             ok-text="确定"
                                             cancel-text="取消"
                                             @confirm="handleDelete(record.funcId)">
                                         <a-button size="small" type="link">删除</a-button>
                                      </a-popconfirm>
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

                        <!-- 新增功能 -->
                        <func-save-modal ref="funcSaveModal" @success="refreshData"></func-save-modal>
                        <!-- 更新功能 -->
                        <func-update-modal ref="funcUpdateModal" @success="refreshData"></func-update-modal>
                    </a-card>
                </a-col>
            </a-row>
    </a-layout>
</template>
<script>
    import {authFuncDelete, authFuncPage} from "@/api/auth";
    import constant, {authDomain} from "@/utils/constant";
    import leftFuncTree from "@/views/auth/func/leftFuncTree";
    import funcSaveModal from "@/views/auth/func/funcSaveModal";
    import funcUpdateModal from "@/views/auth/func/funcUpdateModal";

    export default {
        name: "Func.vue",
        components:{
            leftFuncTree,
            funcSaveModal,
            funcUpdateModal
        },
        data(){
            return {
                toolbarFixed: true,
                searchParams: {
                    currentPage: 1,
                    pageSize: 20,
                    funcName: '',
                    funcKey: '',
                    parentId: null,
                    funcType: ''
                },
                parentFunc: {},
                total: 0,
                tableData: [],
                tableLoading: true,
                tableColumns: [{
                    title: 'ID',
                    dataIndex: 'funcId',
                    key: 'funcId',
                },
                    {
                        title: '功能名称',
                        dataIndex: 'funcName',
                        key: 'funcName',
                    },
                    {
                        title: '功能Key',
                        dataIndex: 'funcKey',
                        key: 'funcKey',
                        ellipsis:  true,
                    },
                    {
                        title: '功能类型',
                        dataIndex: 'funcType',
                        key: 'funcType',
                        slots: { customRender: 'funcType' },
                    },
                    {
                        title: '菜单图标',
                        dataIndex: 'funcIcon',
                        key: 'funcIcon',
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
            funcTypeG(){
                return constant.getConst("func_type_group",authDomain)
            },
            funcTypeGName(){
                return constant.formatConst(this.funcTypeG)
            },
        },
        methods:{
            refreshData(){
               this.queryData();
                this.$refs['leftFuncTree'].reloadTree();
            },
            handleSelectNode(item){
               let parentId = null;
                if(item && item.funcId !==null){
                    this.parentFunc = item;
                    parentId = item.funcId;
                    this.searchParams.parentId = parentId;
                    this.queryData();
                }else {
                    this.searchParams.parentId = null;
                }
            },
            handleUpdate(id){
                this.$refs['funcUpdateModal'].open(id);
            },
            handleDelete(id){
                authFuncDelete(id).then(e=>{
                    this.$message.success("删除功能成功！");
                    this.refreshData();
                })
            },
            handleAdd(){
                if(this.parentFunc){
                    this.$refs['funcSaveModal'].open(this.parentFunc);
                }else {
                    this.$message.warn("请先点击左侧功能树再添加！");
                }
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
                this.$refs['leftFuncTree'].resetSelect();
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
                    const res = await authFuncPage(this.searchParams)
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