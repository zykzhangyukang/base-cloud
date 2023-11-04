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
                    <a-select v-model:value="searchParams.destProject" :style="{width:'180px'}" placeholder="目标系统" >
                        <a-select-option v-for="item in destProjectG" :value="item.code" :key="item.code">{{destProjectGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="回调状态">
                    <a-select v-model:value="searchParams.status" :style="{width:'180px'}" placeholder="回调状态" allowClear>
                        <a-select-option v-for="item in callbackStatusG" :value="item.code" :key="item.code">{{callbackStatusGName[item.code]}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="回调次数">
                    <a-select v-model:value="searchParams.repeatCount" :style="{width:'180px'}" placeholder="回调次数" allowClear>
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
                    rowKey='uuid'
                    :columns='tableColumns'
                    :data-source='tableData'
            >
                <template #srcProject="{ text }">
                    {{ srcProjectGName[text] }}
                </template>
                <template #destProject="{ text }">
                    {{ destProjectGName[text] }}
                </template>
                <template #status="{ text }">
                    {{ callbackStatusGName[text] }}
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
    import {syncCallbackPage} from "@/api/sync";
    import constant, {syncDomain} from "@/utils/constant";
    import moment from "moment";
    import MsgCntLookModal from "@/views/sync/result/MsgCntLookModal";

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
                    destProject: 'bizedu',
                    status: '',
                    planCode: '',
                    startTime: null,
                    endTime: null,
                },
                total: 0,
                tableData: [],
                tableLoading: true,
                tableColumns: [
                    {
                        title: '源系统',
                        dataIndex: 'srcProject',
                        key: 'srcProject',
                        slots: { customRender: 'srcProject' },
                        ellipsis: true,
                    },
                    {
                        title: '目标系统',
                        dataIndex: 'destProject',
                        key: 'destProject',
                        slots: { customRender: 'destProject' },
                        ellipsis: true,
                    },

                    {
                        title: '创建时间',
                        dataIndex: 'createTime',
                        key: 'createTime',
                        ellipsis: true,
                    },
                    {
                        title: '回调时间',
                        dataIndex: 'ackTime',
                        key: 'ackTime',
                        ellipsis: true,
                    },
                    {
                        title: '消息内容',
                        dataIndex: 'msgContent',
                        key: 'msgContent',
                        slots: { customRender: 'msgContent' },
                        ellipsis: true,
                    },
                    {
                        title: '回调状态',
                        dataIndex: 'status',
                        key: 'status',
                        align: 'center',
                        ellipsis: true,
                        slots: { customRender: 'status' },
                    },
                    {
                        title: '回调次数',
                        dataIndex: 'repeatCount',
                        key: 'repeatCount',
                        align: 'center',
                        ellipsis: true,
                    },
                    {
                        title: '备注',
                        dataIndex: 'remark',
                        key: 'remark',
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
            callbackStatusG(){
                return constant.getConst("callback_status",syncDomain)
            },
            callbackStatusGName(){
                return constant.formatConst(this.callbackStatusG)
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
                    const res = await syncCallbackPage(this.searchParams)
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