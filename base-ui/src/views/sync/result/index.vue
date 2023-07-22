<template>
    <a-layout class='role-container'>
        <a-card>
            <a-form
                    :style="{'marginBottom':'10px'}"
                    layout='inline'
            >
                <a-form-item label="计划编号">
                    <a-input v-model:value="searchParams.planCode" :style="{width:'180px'}" placeholder="计划编号输入框"  autocomplete="off" ></a-input>
                </a-form-item>
                <a-form-item label="关键字">
                    <a-input v-model:value="searchParams.keywords" :style="{width:'260px'}" placeholder=" 消息内容，同步内容，计划编号"  autocomplete="off" ></a-input>
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
                <a-form-item label="状态">
                    <a-select v-model:value="searchParams.syncStatus" :style="{width:'180px'}" placeholder="同步状态">
                        <a-select-option v-for="item in resultStatusG" :value="item.code" :key="item.code">{{resultStatusGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="消息来源">
                    <a-select v-model:value="searchParams.msgSrc" :style="{width:'180px'}" placeholder="消息来源">
                        <a-select-option v-for="item in msgSrcG" :value="item.code" :key="item.code">{{msgSrcGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="重试次数">
                    <a-select v-model:value="searchParams.repeatCount" :style="{width:'180px'}" placeholder="重试次数">
                        <a-select-option v-for="item in repeatCountG" :value="item.code" :key="item.code">{{repeatCountGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="pageSearchChange" v-permission="'sync:plan:page'">搜索</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" @click="pageSearchReset">重置</a-button>
                </a-form-item>
            </a-form>

            <HTable
                    :pagination='false'
                    :loading='tableLoading'
                    rowKey='uuid'
                    bordered
                    :columns='tableColumns'
                    :data-source='tableData'
            >
                <template #msgContent="{ record }">
                    <a class="btn-text-mini" href="javascript:;" @click="this.$refs.MsgCntLookModal.open(record.msgContent)">  {{ record.msgContent }}</a>
                </template>
                <template #syncContent="{ record }">
                   <a class="btn-text-mini" href="javascript:;" @click="this.$refs.SyncCntLookModal.open(record.syncContent)"> {{ record.syncContent }}</a>
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
                               {{ resultStatusGName[record.status] }}
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
                    @change="pageCurrentChange"
                    @showSizeChange="pageSizeChange"/>
        </a-card>
        <sync-cnt-look-modal ref="SyncCntLookModal"></sync-cnt-look-modal>
        <msg-cnt-look-modal ref="MsgCntLookModal"></msg-cnt-look-modal>
    </a-layout>
</template>

<script>
    import HTable from "@/components/table/HTable";
    import HPage from "@/components/pagination/HPage";
    import {syncResultPage} from "@/api/sync";
    import constant, {syncDomain} from "@/utils/constant";
    import MsgCntLookModal from "@/views/sync/result/MsgCntLookModal";
    import SyncCntLookModal from "@/views/sync/result/SyncCntLookModal";

    export default {
        name: "plan.vue",
        components: {
            HTable,
            HPage,
            MsgCntLookModal,
            SyncCntLookModal
        },
        data() {
            return {
                tooltipStyle: {
                    color: 'white',
                    fontSize: '10px',
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
                    destProject: ''
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
                        dataIndex: 'syncContent',
                        key: 'syncContent',
                        ellipsis: true,
                        slots: { customRender: 'syncContent' },
                    },
                    {
                        title: '消息内容',
                        dataIndex: 'msgContent',
                        key: 'msgContent',
                        ellipsis: true,
                        slots: { customRender: 'msgContent' },
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
                    },
                    {
                        title: '同步时间',
                        dataIndex: 'syncTime',
                        key: 'syncTime',
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
        color: #19be6b;
        cursor: pointer;
        font-size: 13px;
    }
    .fail {
        color: #ed4014;
        cursor: pointer;
        font-size: 13px;
    }
</style>