<template>
    <a-layout class='msg-container'>
        <a-card>
            <a-form
                    :style="{'marginBottom':'10px'}"
                    layout='inline'
            >
                <a-form-item label="源系统">
                    <a-select v-model:value="searchParams.srcProject" :style="{width:'200px'}" placeholder="源系统">
                        <a-select-option v-for="item in srcProjectG" :value="item.code" :key="item.code">{{srcProjectGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="目标系统">
                    <a-select v-model:value="searchParams.destProject" :style="{width:'180px'}" placeholder="目标系统" allowClear>
                        <a-select-option v-for="item in destProjectG" :value="item.code" :key="item.code">{{destProjectGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="消息id">
                    <a-input v-model:value="searchParams.msgId" :style="{width:'180px'}" placeholder="消息id"  autocomplete="off" ></a-input>
                </a-form-item>
                <a-form-item label="mq消息id">
                    <a-input v-model:value="searchParams.mid" :style="{width:'200px'}" placeholder="mq消息id"  autocomplete="off" ></a-input>
                </a-form-item>
                <a-form-item label="发送状态">
                    <a-select v-model:value="searchParams.sendStatus" :style="{width:'200px'}" placeholder="发送状态" allowClear>
                        <a-select-option v-for="item in sendStatusG" :value="item.code" :key="item.code">{{sendStatusGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="处理状态">
                    <a-select v-model:value="searchParams.dealStatus" :style="{width:'180px'}" placeholder="处理状态" allowClear>
                        <a-select-option v-for="item in dealStatusG" :value="item.code" :key="item.code">{{dealStatusGName[item.code]}}</a-select-option>
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
                <a-form-item label="消息内容">
                    <a-input v-model:value="searchParams.msgContent"   :style="{width:'250px'}"  placeholder="消息内容"  autocomplete="off" ></a-input>
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
                    rowKey='mqMessageId'
                    :columns='tableColumns'
                    :data-source='tableData'
            >
                <template #srcProject="{ text }">
                    {{ srcProjectGName[text] }}
                </template>
                <template #destProject="{ text }">
                    {{ destProjectGName[text] }}
                </template>
                <template #sendStatus="{ text }">
                    {{ sendStatusGName[text] }}
                </template>
                <template #dealStatus="{ text }">
                    {{ dealStatusGName[text] }}
                </template>
                <template #msgContent="{ record }">
                    <a id="msg_content" @click="this.$refs.MsgCntLookModal.open(record.msgContent)">  {{ record.msgContent }}</a>
                </template>
            </HTable>
            <HPage
                    :current="searchParams.currentPage"
                    :page-size="searchParams.pageSize"
                    :total="total"
                    @current-change="pageCurrentChange"
                    @size-change="pageSizeChange"/>
        </a-card>
        <msg-cnt-look-modal ref="MsgCntLookModal"></msg-cnt-look-modal>
    </a-layout>
</template>

<script>
    import HTable from "@/components/table/HTable";
    import HPage from "@/components/pagination/HPage";
    import {syncMsgPage} from "@/api/sync";
    import constant, {syncDomain} from "@/utils/constant";
    import MsgCntLookModal from "@/views/sync/result/MsgCntLookModal";
    import moment from "moment";

    export default {
        name: "plan.vue",
        components: {
            HTable,
            HPage,
            MsgCntLookModal
        },
        data() {
            return {
                toolbarFixed: true,
                timeList: [moment().subtract(7, 'day').startOf("day").format("YYYY-MM-DD HH:mm:ss"), moment().endOf('day').format("YYYY-MM-DD HH:mm:ss")],
                ranges: {
                    "今天": [moment().startOf("day"), moment().endOf('day')],
                    "昨天": [moment().subtract(1, 'day').startOf("day"), moment().subtract(1, 'day').endOf('day')],
                    "近7天": [moment().subtract(7, 'day').startOf("day"), moment().endOf('day')],
                    "本月": [moment().startOf('month'), moment().endOf('month')]
                },
                searchParams: {
                    currentPage: 1,
                    pageSize: 20,
                    srcProject: 'auth',
                    destProject: '',
                    dealStatus: '',
                    sendStatus: '',
                    msgContent: '',
                    msgId: '',
                    mid: '',
                    startTime: null,
                    endTime: null,
                },
                total: 0,
                tableData: [],
                tableLoading: true,
                tableColumns: [
                    {
                        title: '消息id',
                        dataIndex: 'uuid',
                        width: '250px',
                        ellipsis: true,
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
                        title: 'mq消息id',
                        dataIndex: 'mid',
                        key: 'mid',
                        width: '260px',
                        ellipsis: true,
                    },
                    {
                        title: '发送状态',
                        dataIndex: 'sendStatus',
                        key: 'sendStatus',
                        slots: { customRender: 'sendStatus' },
                    },
                    {
                        title: '处理状态',
                        dataIndex: 'dealStatus',
                        key: 'dealStatus',
                        slots: { customRender: 'dealStatus' },
                    },
                    {
                        title: '消息内容',
                        dataIndex: 'msgContent',
                        key: 'msgContent',
                        ellipsis: true,
                        slots: { customRender: 'msgContent' },
                    },
                    {
                        title: '处理次数',
                        dataIndex: 'dealCount',
                        key: 'dealCount',
                        align: 'center',
                        width: '100px',
                    },
                    {
                        title: '发送时间',
                        dataIndex: 'sendTime',
                        key: 'sendTime',
                    },
                    {
                        title: 'ACK时间',
                        dataIndex: 'ackTime',
                        key: 'ackTime',
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
            sendStatusG(){
                return constant.getConst("send_status",syncDomain)
            },
            sendStatusGName(){
                return constant.formatConst(this.sendStatusG)
            },
            dealStatusG(){
                return constant.getConst("deal_status",syncDomain)
            },
            dealStatusGName(){
                return constant.formatConst(this.dealStatusG)
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
                this.timeList = [moment().subtract(7, 'day').startOf("day").format("YYYY-MM-DD HH:mm:ss"), moment().endOf('day').format("YYYY-MM-DD HH:mm:ss")];
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
                    if(this.timeList && this.timeList.length === 2){
                        this.searchParams.startTime = this.timeList[0];
                        this.searchParams.endTime = this.timeList[1];
                    }
                    const res = await syncMsgPage(this.searchParams)
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
    #msg_content{
        font-family: Corbel,serif;
        font-size: 13px;
        cursor: pointer;
        color: #409eff;
    }
</style>