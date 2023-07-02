<template>
    <a-modal v-model:visible="visible"
             :title="null"
             :width="600"
             :footer="null"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="确定"
             ref="form"
    >
      <a-divider >资源列表</a-divider>
      <div>
        <span  v-if="rescVOList && rescVOList.length > 0">
          <div class="resc_item" v-for="item in rescVOList" :key="item.rescId">{{   '请求方式：'+methodTypeGName[item.methodType] + " ，资源URL：" + item.rescUrl  }} <span style="float: right">({{item.rescName}})</span></div>
        </span>
        <span v-else>
             <a-empty description="暂无绑定的资源"/>
               <a-divider/>
        </span>
      </div>
      <a-divider >用户列表</a-divider>
        <!-- 用户列表 -->
      <div>
          <div v-if="userVOList && userVOList.length > 0">
             <a-tag v-for="item in userVOList" :key="item.userId" class="ml15 user_item">
               <a-tooltip>
                <template #title>{{'账号:'+item.username}}</template>
                  {{item.realName + '-' + item.deptName}}
              </a-tooltip>
            </a-tag>
        </div>
          <span v-else>
               <a-empty description="暂无绑定的用户"/>
                 <a-divider/>
          </span>
      </div>
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
.resc_item{
  background-color: #f5f5f5;
  border-radius: 5px;
  font-size: 11px;
  color: #000000d9;
  margin-bottom: 5px;
  padding: 5px;
}
.user_item{
  display: inline-block;
  margin-left: 5px;
  margin-bottom: 5px;
  cursor: pointer;
}
</style>