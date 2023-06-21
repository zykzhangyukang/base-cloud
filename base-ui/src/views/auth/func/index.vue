<template>
    <a-layout>
            <a-row :gutter="16">
                <a-col :span="3">
                    <div class="left">
                        <func-left-tree ref="funcLeftTree" @select-node="handleSelectNode"></func-left-tree>
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
                                <a-input v-model:value="searchParams.funcName" :style="{width:'150px'}" placeholder="功能名称输入框"></a-input>
                            </a-form-item>
                            <a-form-item label="功能Key">
                                <a-input v-model:value="searchParams.funcKey" :style="{width:'150px'}" placeholder="功能Key输入框"></a-input>
                            </a-form-item>
                            <a-form-item label="资源URL">
                                <a-input v-model:value="searchParams.rescUrl" :style="{width:'150px'}" placeholder="资源URL输入框"></a-input>
                            </a-form-item>
                            <a-form-item label="功能类型">
                                <a-select v-model:value="searchParams.funcType" :style="{width:'150px'}" placeholder="功能类型">
                                    <a-select-option v-for="item in funcTypeG" :value="item.code" :key="item.code">{{funcTypeGName[item.code]}}</a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-form-item label="是否隐藏">
                                <a-select v-model:value="searchParams.funcDirStatus" :style="{width:'150px'}" placeholder="是否隐藏">
                                    <a-select-option v-for="item in funcDirStatusG" :value="item.code" :key="item.code">{{funcDirStatusGName[item.code]}}</a-select-option>
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
                              <span>
                                  <FolderOpenOutlined  v-if="text === 'dir'"/>
                                  <ToolOutlined v-else />
                              </span>
                                {{ funcTypeGName[text] }}
                            </template>
                            <template #rescVOList="{ record }">
                                   <span>
                                         <a  v-if="record.rescVOList !==null && record.rescVOList.length >0" class="btn-text-small color303030" @click="handleLookResc(record.funcId)">
                                             {{record.rescVOList.map(item=>{return item.rescUrl}).join(",") }}
                                         </a>
                                        <a v-else class="btn-text-small color303030" @click="handleLookResc(record.funcId)">-</a>
                                    </span>
                            </template>
                            <template #action="{ record }">
                                <span>
                                    <a-divider type="vertical"/>
                                    <a href="#" class="btn-text-small" @click="handleUpdate(record.funcId)"><EditOutlined />编辑</a>
                                        <a-divider type="vertical"/>
                                          <a-popconfirm
                                                  title="您确定要删除该功能吗?"
                                                  ok-text="确定"
                                                  cancel-text="取消"
                                                  @confirm="handleDelete(record.funcId)">
                                        <a href="#" class="btn-text-small"><DeleteOutlined />删除</a>
                                      </a-popconfirm>
                                     <a-divider type="vertical"/>
                                    <a-dropdown :trigger="['click']">
                                        <a class="ant-dropdown-link btn-text-small" @click.prevent >
                                          <SettingOutlined/>设置
                                        </a>
                                        <template #overlay>
                                          <a-menu>
                                            <a-menu-item>
                                                <a href="#" class="btn-text-small" @click="handleBindResc(record.funcId)">修改资源</a>
                                            </a-menu-item>
                                            <a-menu-item>
                                               <a href="#" class="btn-text-small" @click="handleDeleteResourceBind(record.funcId)">清空资源</a>
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

                        <!-- 新增功能 -->
                        <func-save-modal ref="funcSaveModal" @success="queryData"></func-save-modal>
                        <!-- 更新功能 -->
                        <func-update-modal ref="funcUpdateModal" @success="queryData"></func-update-modal>
                        <!-- 绑定资源 -->
                        <func-bind-resc-modal ref="funcBindRescModal" @success="queryData"></func-bind-resc-modal>
                        <!-- 查看资源 -->
                        <func-resc-look-modal ref="funcRescLookModal"></func-resc-look-modal>
                    </a-card>
                </a-col>
            </a-row>
    </a-layout>
</template>
<script>
import {authFuncDelete, authFuncPage, deleteResourceBind} from "@/api/auth";
import {
  DeleteOutlined,
  EditOutlined,
  ExclamationCircleOutlined,
  FolderOpenOutlined,
  SettingOutlined,
  ToolOutlined
} from '@ant-design/icons-vue';
import constant, {authDomain} from "@/utils/constant";
import funcLeftTree from "@/views/auth/func/funcLeftTree";
import funcSaveModal from "@/views/auth/func/funcSaveModal";
import funcUpdateModal from "@/views/auth/func/funcUpdateModal";
import funcBindRescModal from "@/views/auth/func/funcBindRescModal";
import funcRescLookModal from "@/views/auth/func/funcRescLookModal";
import {Modal} from "ant-design-vue";
import {createVNode} from 'vue';

export default {
        name: "Func.vue",
        components:{
          funcLeftTree,
            funcSaveModal,
            funcUpdateModal,
            funcBindRescModal,
            funcRescLookModal,
            FolderOpenOutlined,
            ToolOutlined,
            EditOutlined,
            DeleteOutlined,
            SettingOutlined
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
                    funcType: '',
                    rescUrl: '',
                    funcDirStatus: '',
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
                        align: 'center',
                        ellipsis:  true,
                    },
                    {
                        title: '功能Key',
                        dataIndex: 'funcKey',
                        key: 'funcKey',
                        ellipsis:  true,
                        align: 'center',
                    },
                    {
                        title: '功能类型',
                        dataIndex: 'funcType',
                        key: 'funcType',
                        align: 'center',
                        slots: { customRender: 'funcType' },
                    },
                    {
                        title: '资源列表',
                        dataIndex: 'rescVOList',
                        key: 'rescVOList',
                        slots: { customRender: 'rescVOList' },
                        align: 'center',
                        ellipsis:  true,
                    },
                    {
                        title: '排序',
                        dataIndex: 'funcSort',
                        align: 'center',
                        key: 'funcSort',
                    },
                    {
                        title: '菜单图标',
                        dataIndex: 'funcIcon',
                        align: 'center',
                        key: 'funcIcon',
                        ellipsis:  true,
                    },
                    {
                        title: '更新时间',
                        dataIndex: 'updateTime',
                        align: 'center',
                        key: 'updateTime',
                      ellipsis:  true,
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: '200px',
                        align: 'center',
                        slots: { customRender: 'action' },
                        fixed: 'right',
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
            funcDirStatusG(){
                return constant.getConst("func_dir_status_group",authDomain)
            },
            funcDirStatusGName(){
                return constant.formatConst(this.funcDirStatusG)
            },
        },
        methods:{
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
            handleLookResc(id){
                this.$refs['funcRescLookModal'].open(id);
            },
            handleBindResc(id){
                this.$refs['funcBindRescModal'].open(id);
            },
          handleDeleteResourceBind(funcId){
            const _this = this;
            Modal.confirm({
              title:  '是否清空绑定的资源?',
              cancelText: "取消",
              okText: "确定",
              icon: () => createVNode(ExclamationCircleOutlined),
              onOk() {
                deleteResourceBind(funcId).then(res=>{
                  _this.$message.success("清空绑定资源成功！");
                  _this.queryData();
                })
              },
              onCancel() {},
            })
          },
            handleDelete(id){
                authFuncDelete(id).then(e=>{
                    this.$message.success("删除功能成功！");
                    this.queryData();
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
                this.$refs['funcLeftTree'].resetSelect();
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