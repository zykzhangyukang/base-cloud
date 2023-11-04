<template>
    <a-layout class='result-container'>
        <a-card>
            <a-form
                    :style="{'marginBottom':'10px'}"
                    layout='inline'
            >
                <a-form-item label="计划编号">
                    <a-input v-model:value="searchParams.planCode" :style="{width:'180px'}" placeholder="计划编号输入框"  autocomplete="off" allowClear></a-input>
                </a-form-item>
                <a-form-item label="源系统">
                    <a-select v-model:value="searchParams.srcProject" :style="{width:'180px'}" placeholder="源系统" allowClear>
                        <a-select-option v-for="item in srcProjectG" :value="item.code" :key="item.code">{{srcProjectGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="目标系统">
                    <a-select v-model:value="searchParams.destProject" :style="{width:'180px'}" placeholder="目标系统" allowClear>
                        <a-select-option v-for="item in destProjectG" :value="item.code" :key="item.code">{{destProjectGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="状态">
                    <a-select v-model:value="searchParams.syncStatus" :style="{width:'180px'}" placeholder="同步状态" allowClear>
                        <a-select-option v-for="item in resultStatusG" :value="item.code" :key="item.code">{{resultStatusGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="消息来源">
                    <a-select v-model:value="searchParams.msgSrc" :style="{width:'180px'}" placeholder="消息来源" allowClear>
                        <a-select-option v-for="item in msgSrcG" :value="item.code" :key="item.code">{{msgSrcGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="重试次数">
                    <a-select v-model:value="searchParams.repeatCount" :style="{width:'180px'}" placeholder="重试次数" allowClear>
                        <a-select-option v-for="item in repeatCountG" :value="item.code" :key="item.code">{{repeatCountGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="创建时间">
                    <a-range-picker
                            style="width: 200px"
                            v-model:value="timeList"
                            :ranges="ranges"
                            :allowClear="false"
                            valueFormat="YYYY-MM-DD HH:mm:ss"
                            format="YYYY-MM-DD HH:mm:ss"
                            showTime
                    />
                </a-form-item>
                <a-form-item label="关键字">
                    <a-input v-model:value="searchParams.keywords" :style="{width:'250px'}"  placeholder=" 消息内容，同步内容"  autocomplete="off" ></a-input>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="pageSearchChange" v-permission="'sync:result:page'"><template #icon><SearchOutlined /></template>搜索</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="pageSearchReset">重置</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="signSuccess" v-permission="'sync:result:signSuccess'"  :loading="loading2">标记成功</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="repeatSync" v-permission="'sync:result:repeatSync'"  :loading="loading3">重新同步</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="validResultData" v-permission="'sync:result:validResultData'" >校验结果</a-button>
                </a-form-item>
            </a-form>

            <HTable
                    :pagination='false'
                    :loading='tableLoading'
                    rowKey='uuid'
                    bordered
                    size="small"
                    :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: 'radio' }"
                    :columns='tableColumns'
                    :data-source='tableData'
                    @change="handleTableChange"
            >
                <template #hlsMsgContent="{ record }">
                    <span  id="msg_content" @click="this.$refs.MsgCntLookModal.open(record.msgContent)"  v-html="record.hlsMsgContent || record.msgContent"></span>
                </template>
                <template #hlsSyncContent="{ record }">
                   <span id="sync_content" @click="this.$refs.SyncCntLookModal.open(record.syncContent)" v-html="record.hlsSyncContent || record.syncContent"></span>
                </template>
                <template #srcProject="{ text }">
                    {{ srcProjectGName[text] }}
                </template>
                <template #destProject="{ text }">
                    {{ destProjectGName[text] }}
                </template>
                <template #msgSrc="{ text }">
                    {{ msgSrcGName[text] }}
                </template>
                <template #status="{ record }">
                    <span>
                        <!-- 成功 -->
                        <span class="success" v-if="record.status === 'success'">
                               {{ resultStatusGName[record.status] + ' ('+((new Date(record.syncTime).getTime())  - (new Date(record.msgCreateTime).getTime()) ) / 1000+')'}}
                        </span>
                        <!-- 失败 -->
                        <span class="fail" v-if="record.status === 'fail'">
                            <a-tooltip placement="topLeft" :title="record.errorMsg" :overlay-style="tooltipStyle">{{ resultStatusGName[record.status] }}</a-tooltip>
                        </span>
                    </span>
                </template>
            </HTable>
            <HPage
                    :current="searchParams.currentPage"
                    :page-size="searchParams.pageSize"
                    :total="total"
                    @current-change="pageCurrentChange"
                    @size-change="pageSizeChange"/>
        </a-card>
        <sync-cnt-look-modal ref="SyncCntLookModal"></sync-cnt-look-modal>
        <msg-cnt-look-modal ref="MsgCntLookModal"></msg-cnt-look-modal>
        <valid-data-look-modal ref="ValidDataLookModal"></valid-data-look-modal>
    </a-layout>
</template>

<script>
    import moment from 'moment';
    import HTable from "@/components/table/HTable";
    import HPage from "@/components/pagination/HPage";
    import {syncResultPage, syncResultRepeatSync, syncResultSignSuccess} from "@/api/sync";
    import constant, {syncDomain} from "@/utils/constant";
    import MsgCntLookModal from "@/views/sync/result/MsgCntLookModal";
    import SyncCntLookModal from "@/views/sync/result/SyncCntLookModal";
    import ValidDataLookModal from "@/views/sync/result/ValidDataLookModal";
    import {Modal} from "ant-design-vue";
    import {createVNode} from "vue";
    import {ExclamationCircleOutlined} from '@ant-design/icons-vue';

    export default {
        name: "plan.vue",
        components: {
            HTable,
            HPage,
            MsgCntLookModal,
            SyncCntLookModal,
            ValidDataLookModal,
        },
        data() {
            return {
                loading2: false,
                loading3: false,
                timeList: [moment().subtract(7, 'day').startOf("day").format("YYYY-MM-DD HH:mm:ss"), moment().endOf('day').format("YYYY-MM-DD HH:mm:ss")],
                ranges: {
                    "今天": [moment().startOf("day"), moment().endOf('day')],
                    "昨天": [moment().subtract(1, 'day').startOf("day"), moment().subtract(1, 'day').endOf('day')],
                    "近7天": [moment().subtract(7, 'day').startOf("day"), moment().endOf('day')],
                    "本月": [moment().startOf('month'), moment().endOf('month')]
                },
                selectedRowKeys: [],
                selectedRows: [],
                tooltipStyle: {
                    color: 'white',
                    fontSize: '12px',
                    width: '800px',
                },
                toolbarFixed: true,
                searchParams: {
                    currentPage: 1,
                    pageSize: 20,
                    planCode: '',
                    keywords: '',
                    syncStatus: '',
                    msgSrc: '',
                    repeatCount: null,
                    srcProject: '',
                    destProject: '',
                    sortField: '',
                    sortOrder: '',
                    startTime: null,
                    endTime: null,
                },
                total: 0,
                tableData: [],
                tableLoading: true,
                tableColumns: [
                    {
                        title: '计划编号',
                        dataIndex: 'planCode',
                        key: 'planCode',
                        width: '180px',
                        ellipsis: true,
                    },
                    {
                        title: '计划名称',
                        dataIndex: 'planName',
                        key: 'planName',
                        ellipsis: true,
                        width: 120,
                    },
                    {
                        title: '源系统',
                        dataIndex: 'srcProject',
                        key: 'srcProject',
                        ellipsis: true,
                        slots: { customRender: 'srcProject' },
                    },
                    {
                        title: '目标系统',
                        dataIndex: 'destProject',
                        key: 'destProject',
                        ellipsis: true,
                        slots: { customRender: 'destProject' },
                    },
                    {
                        title: '消息来源',
                        dataIndex: 'msgSrc',
                        key: 'msgSrc',
                        ellipsis: true,
                        width: 120,
                        slots: { customRender: 'msgSrc' },
                    },
                    {
                        title: '同步状态',
                        dataIndex: 'status',
                        key: 'status',
                        ellipsis: true,
                        width: 100,
                        align: 'center',
                        slots: { customRender: 'status' },
                    },
                    {
                        title: '同步内容',
                        dataIndex: 'hlsSyncContent',
                        key: 'hlsSyncContent',
                        width: '150px',
                        ellipsis: true,
                        slots: { customRender: 'hlsSyncContent' },
                    },
                    {
                        title: '消息内容',
                        dataIndex: 'hlsMsgContent',
                        key: 'hlsMsgContent',
                        width: '150px',
                        ellipsis: true,
                        slots: { customRender: 'hlsMsgContent' },
                    },
                    {
                        title: '重试次数',
                        dataIndex: 'repeatCount',
                        key: 'repeatCount',
                        ellipsis: true,
                        width: 100,
                        align: 'center',
                        slots: { customRender: 'repeatCount' },
                    },
                    {
                        title: '创建时间',
                        dataIndex: 'msgCreateTime',
                        key: 'msgCreateTime',
                        sorter: true,
                    },
                    {
                        title: '同步时间',
                        dataIndex: 'syncTime',
                        key: 'syncTime',
                        sorter: true,
                    },
                    {
                        title: '备注信息',
                        dataIndex: 'remark',
                        key: 'remark',
                        width: 80,
                        ellipsis: true,
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
            msgSrcG(){
                return constant.getConst("msg_src",syncDomain)
            },
            msgSrcGName(){
                return constant.formatConst(this.msgSrcG)
            },
            resultStatusG(){
                return constant.getConst("result_status",syncDomain)
            },
            resultStatusGName(){
                return constant.formatConst(this.resultStatusG)
            },
            repeatCountG(){
                return constant.getConst("repeat_times",syncDomain)
            },
            repeatCountGName(){
                return constant.formatConst(this.repeatCountG)
            },
        },
        methods:{
            handleTableChange(pagination, filters, sorter, { currentDataSource }){
                if(sorter.field && sorter.order){
                    this.searchParams.sortField = sorter.field;
                    this.searchParams.sortOrder = sorter.order;
                }else {
                    this.searchParams.sortField = '';
                    this.searchParams.sortOrder = '';
                }

                this.queryData();
            },
            onSelectChange(selectedRowKeys, selectedRows) {
                this.selectedRowKeys = selectedRowKeys;
                this.selectedRows = selectedRows;
            },
            signSuccess(){
                if(!this.selectedRows || this.selectedRows.length !==1){
                    return this.$message.warn("请选择一条记录进行操作！");
                }
                const p = this.selectedRows[0];
                if(p && p.status !== 'fail'){
                    return this.$message.warn("请选择同步失败的记录！");
                }
                let _this = this;
                Modal.confirm({
                    title: '标记成功',
                    icon: createVNode(ExclamationCircleOutlined),
                    content: '确定标记成功吗，请在开发人员的协助下操作！',
                    okText: '确认',
                    cancelText: '取消',
                    onOk() {
                        _this.loading2 = true;
                        syncResultSignSuccess(p.uuid).then(e=>{
                            _this.$message.success("操作成功,请刷新查看！");
                        }).finally(()=>{
                            _this.loading2 = false;
                        })
                    },
                });
            },
            validResultData(){
                if(!this.selectedRows || this.selectedRows.length !==1){
                    return this.$message.warn("请选择一条记录进行操作！");
                }
                const p = this.selectedRows[0];
                if(p){
                    this.$refs['ValidDataLookModal'].open(p);
                }
            },
            repeatSync(){
                if(!this.selectedRows || this.selectedRows.length !==1){
                    return this.$message.warn("请选择一条记录进行操作！");
                }
                const p = this.selectedRows[0];
                if(p && p.status !== 'fail'){
                    return this.$message.warn("请选择同步失败的记录！");
                }
                let _this = this;
                Modal.confirm({
                    title: '重新同步',
                    icon: createVNode(ExclamationCircleOutlined),
                    content: '确定重新同步吗，请在开发人员的协助下操作！',
                    okText: '确认',
                    cancelText: '取消',
                    onOk() {
                        _this.loading3 = true;
                        syncResultRepeatSync(p.uuid).then(e=>{
                            _this.$message.success("操作成功,请刷新查看！");
                            _this.selectedRowKeys = [];
                        }).finally(()=>{
                            _this.loading3 = false;
                        })
                    },
                });
            },
            async pageSearchChange() {
                this.searchParams.currentPage = 1
                await this.queryData()
            },
            pageSearchReset() {
                const page = {
                    currentPage: this.searchParams.currentPage,
                    pageSize: this.searchParams.pageSize
                }
                const sort = {
                    sortField: this.searchParams.sortField,
                    sortOrder: this.searchParams.sortOrder,
                }
                this.searchParams = {
                    ...this.$options.data().searchParams,
                    ...page,
                    ...sort
                }
                this.selectedRowKeys = [];
                this.selectedRows = [];
                this.timeList = [moment().subtract(7, 'day').startOf("day").format("YYYY-MM-DD HH:mm:ss"), moment().endOf('day').format("YYYY-MM-DD HH:mm:ss")];
            },
            pageCurrentChange(page, pageSize) {
                this.searchParams.currentPage = page;
                this.searchParams.pageSize = pageSize;
                this.selectedRows = [];
                this.selectedRowKeys = [];
                this.queryData()
            },
            pageSizeChange(current, size){
                this.searchParams.pageSize = size
                this.queryData()
            },
            async queryData() {
                try {
                    this.tableLoading = true
                    if(this.timeList && this.timeList.length === 2){
                        this.searchParams.startTime = this.timeList[0];
                        this.searchParams.endTime = this.timeList[1];
                    }
                    const res = await syncResultPage(this.searchParams)
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
    .success {
        color: #00b96b;
        cursor: pointer;
        font-size: 12px;
    }
    .fail {
        color: #ed4014;
        cursor: pointer;
        font-size: 12px;
    }
    #sync_content,#msg_content{
        font-size: 12px;
        cursor: pointer;
        color: #409eff;
    }
</style>