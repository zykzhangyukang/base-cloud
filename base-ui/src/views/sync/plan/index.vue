<template>
    <a-layout class='plan-container'>
        <a-card>
            <div :style="{'textAlign':'right'}">
                <a-button type="primary"  v-permission="'sync:plan:add'" @click="handleAdd"><PlusOutlined />新增计划</a-button>
            </div>
            <a-form
                    :style="{'marginBottom':'10px'}"
                    layout='inline'
            >
                <a-form-item label="计划编号" name='planCode'>
                    <a-input v-model:value="searchParams.planCode" :style="{width:'180px'}" placeholder="计划编号输入框"  autocomplete="off" ></a-input>
                </a-form-item>
                <a-form-item label="状态">
                    <a-select v-model:value="searchParams.status" :style="{width:'180px'}" placeholder="状态">
                        <a-select-option v-for="item in planStatusG" :value="item.code" :key="item.code">{{planStatusGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="源系统">
                    <a-select v-model:value="searchParams.srcProject" :style="{width:'180px'}" placeholder="源系统">
                        <a-select-option v-for="item in srcProjectG" :value="item.code" :key="item.code">{{srcProjectGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="目标系统">
                    <a-select v-model:value="searchParams.destProject" :style="{width:'180px'}" placeholder="目标系统">
                        <a-select-option v-for="item in destProjectG" :value="item.code" :key="item.code">{{destProjectGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="pageSearchChange" v-permission="'sync:plan:page'"><template #icon><SearchOutlined /></template>搜索</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="pageSearchReset">重置</a-button>
                </a-form-item>
            </a-form>

            <HTable
                    :pagination='false'
                    :loading='tableLoading'
                    bordered
                    rowKey='uuid'
                    :columns='tableColumns'
                    :data-source='tableData'
            >
                <template #planCode="{ record }">
                   <a class="btn-text-mini" href="javascript:void(0);" @click="handlePlanLook(record.uuid)"> {{ record.planCode }}</a>
                </template>
                <template #srcProject="{ text }">
                    {{ srcProjectGName[text] }}
                </template>
                <template #destProject="{ text }">
                    {{ destProjectGName[text] }}
                </template>
                <template #action="{ record }">
                    <div class="action-btns">
                        <!-- 常用按钮 -->
                        <a class="btn-text-mini" href="javascript:;" @click="handleUpdate(record.uuid)" v-permission="'sync:plan:update'"><FormOutlined />编辑</a>
                        <a-popconfirm
                                title="您确定要删除该计划吗?"
                                ok-text="确定"
                                cancel-text="取消"
                                @confirm="handleDelete(record.uuid)"
                        >
                            <a class="btn-text-mini" href="javascript:;" v-permission="'sync:plan:delete'"><DeleteOutlined/>删除</a>
                        </a-popconfirm>
                        <a-switch size="small"  :checked="record.status === 'normal'" @click="handleUpdateStatus(record.uuid,record.status)" v-permission="'sync:plan:updateStatus'" />
                    </div>
                </template>
            </HTable>
            <HPage
                    :current="searchParams.currentPage"
                    :page-size="searchParams.pageSize"
                    :total="total"
                    @change="pageCurrentChange"
                    @showSizeChange="pageSizeChange"/>
        </a-card>
        <!-- 新增同步计划 -->
        <plan-save-modal ref="planSaveModal" @success="queryData"></plan-save-modal>
        <!-- 编辑同步计划 -->
        <plan-update-modal ref="planUpdateModal" @success="queryData"></plan-update-modal>
        <!-- 查看同步计划 -->
        <plan-look-modal ref="planLookModal"></plan-look-modal>
    </a-layout>
</template>

<script>
    import HTable from "@/components/table/HTable";
    import HPage from "@/components/pagination/HPage";
    import PlanLookModal from "@/views/sync/plan/PlanLookModal";
    import PlanSaveModal from "@/views/sync/plan/PlanSaveModal";
    import PlanUpdateModal from "@/views/sync/plan/PlanUpdateModal";
    import {syncPlanDelete, syncPlanPage, syncPlanUpdateStatus} from "@/api/sync";
    import constant, {syncDomain} from "@/utils/constant";

    export default {
        name: "plan.vue",
        components: {
            HTable,
            HPage,
            PlanLookModal,
            PlanSaveModal,
            PlanUpdateModal
        },
        data() {
            return {
                toolbarFixed: true,
                searchParams: {
                    currentPage: 1,
                    pageSize: 20,
                    planCode: '',
                    status: '',
                    srcProject: '',
                    destProject: ''
                },
                total: 0,
                tableData: [],
                tableLoading: true,
                tableColumns: [
                    {
                        title: 'uuid',
                        dataIndex: 'uuid',
                        key: 'uuid',
                        ellipsis: true,
                    },
                    {
                        title: '计划描述',
                        dataIndex: 'description',
                        key: 'description',
                    },
                    {
                        title: '计划编号',
                        dataIndex: 'planCode',
                        key: 'planCode',
                        width: '180px',
                        ellipsis: true,
                        slots: { customRender: 'planCode' },
                    },
                    {
                        title: '源数据库',
                        dataIndex: 'srcDb',
                        key: 'srcDb',
                    },
                    {
                        title: '目标数据库',
                        dataIndex: 'destDb',
                        key: 'destDb',
                    },
                    {
                        title: '源系统',
                        dataIndex: 'srcProject',
                        key: 'srcProject',
                        slots: { customRender: 'srcProject' },
                    },
                    {
                        title: '目标系统',
                        dataIndex: 'destProject',
                        key: 'destProject',
                        slots: { customRender: 'destProject' },
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
            srcProjectG(){
                return constant.getConst("src_project",syncDomain)
            },
            srcProjectGName(){
                return constant.formatConst(this.srcProjectG)
            },
            destProjectG(){
                return constant.getConst("dest_project",syncDomain)
            },
            destProjectGName(){
                return constant.formatConst(this.destProjectG)
            },
            planStatusG(){
                return constant.getConst("plan_status",syncDomain)
            },
            planStatusGName(){
                return constant.formatConst(this.planStatusG)
            },
        },
        methods:{
            handleDelete(id){
                syncPlanDelete(id).then(e=>{
                    this.$message.success("删除计划成功！");
                    this.queryData();
                })
            },
            handleUpdate(uid){
                this.$refs['planUpdateModal'].open(uid);
            },
            handleUpdateStatus(uuid, status){
                let s = status === 'normal' ? 'forbid' : 'normal';
                let msg = status === 'normal' ? '禁用' : '启用';
                syncPlanUpdateStatus({uuid, status: s }).then(e=>{
                   this.tableData.forEach(e=>{
                       if(e.uuid === uuid){
                           e.status = s;
                       }
                   })
                    this.$message.success(`计划${msg}状态成功`);
                })
            },
            handleAdd(){
                this.$refs['planSaveModal'].open();
            },
            handlePlanLook(uuid){
                this.$refs['planLookModal'].open(uuid);
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