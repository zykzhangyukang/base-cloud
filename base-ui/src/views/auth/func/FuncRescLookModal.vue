<template>
    <a-modal v-model:visible="visible"
             title="资源&用户"
             :width="700"
             :footer="null"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="确定"
             ref="form"
    >

        <!-- 表格显示部分 -->
        <a-table size="small"   v-if="rescVOList && rescVOList.length > 0"  class="mb15"  :dataSource="rescVOList"
                   rowKey='rescId'
                   :pagination='false'
                   :columns="columns" >
            <template #rescDomain="{ text }">
                {{ rescDomainName[text] }}
            </template>
            <template #methodType="{ text }">
                {{ methodTypeGName[text] }}
            </template>
        </a-table>
        <span v-else>
             <a-empty description="暂无绑定的资源" />
               <a-divider />
        </span>

        <!-- 用户列表 -->
        <span v-if="userVOList && userVOList.length > 0">
             <span v-for="item in userVOList" :key="item.userId" class="ml15">
               <a-tooltip>
                <template #title>{{'账号:'+item.username}}</template>
                 <a-avatar shape="square"  style="background-color: rgb(253, 227, 207);color: rgb(245, 106, 0);cursor: pointer">
                    {{item.realName}}
              </a-avatar>
              </a-tooltip>
            </span>
        </span>
    </a-modal>
</template>
<script>

    import {authFuncSelectById } from "@/api/auth";
    import constant, {authDomain} from "@/utils/constant";

    export default {
        name: "funcBindRescModal.vue",
        data() {
            return {
                confirmLoading: false,
                visible: false,
                searchLoading: true,
                searchValues: [],
                searchList: [],
                rescVOList: [],
                userVOList: [],
                columns: [
                    {
                        title: '资源名称',
                        dataIndex: 'rescName',
                        key: 'rescName',
                    },
                    {
                        title: '资源URL',
                        dataIndex: 'rescUrl',
                        key: 'rescUrl',
                    },
                    {
                        title: '所属系统',
                        dataIndex: 'rescDomain',
                        key: 'rescDomain',
                        slots: { customRender: 'rescDomain' },
                    },
                    {
                        title: '请求方式',
                        dataIndex: 'methodType',
                        key: 'methodType',
                        slots: { customRender: 'methodType' },
                    }
                ],
            }
        },
        computed: {
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
        methods: {
            handleClose() {
                this.visible = false
                this.confirmLoading = false;
                this.form = this.$options.data().form;
            },
            open(funcId) {
                authFuncSelectById(funcId).then(res=>{
                    this.rescVOList = res.result.rescVOList;
                    this.userVOList = res.result.userVOList;
                    this.visible = true;
                })
            }
        }
    }
</script>

<style scoped>

</style>