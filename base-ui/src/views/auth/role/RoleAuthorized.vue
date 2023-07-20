<template>
  <a-layout class='role-authorized-container'>
    <a-spin :spinning="initLoading" size="large">
      <a-card>
        <a-affix :offset-top="30">
          <div :style="{'textAlign':'right'}">
            <a-button :style="{'marginRight': '10px'}" @click="handleCloseAll">收缩全部</a-button>
            <a-button :style="{'marginRight': '10px'}" @click="handleExpandAll">展开全部</a-button>
            <a-button type="dashed"  :style="{'marginRight': '10px'}" @click="this.$router.push('/auth/role')">返回列表</a-button>
            <a-button type="primary" @click="handleAuthorized" :loading="loading">更新授权</a-button>
          </div>
        </a-affix>
        <template v-for="(item, i) in allTreeList">
          <a-row :gutter="24" :key="i" v-if='i%6 === 0'>
            <a-col :span="4">
              <role-authorized-tree
                  :biz-checked-keys="this.allCheckedMap[item.funcId]"
                  :checked-keys="this.halfCheckedMap[item.funcId]"
                  :ref="'roleAuthorizedTreeRef_'+item.funcId"
                  :tree-data="[item]"
              >
              </role-authorized-tree>
            </a-col>
            <a-col :span="4" v-if="i+1 < allTreeList.length">
              <role-authorized-tree
                  :biz-checked-keys="this.allCheckedMap[allTreeList[i+1].funcId]"
                  :checked-keys="this.halfCheckedMap[allTreeList[i+1].funcId]"
                  :ref="'roleAuthorizedTreeRef_'+allTreeList[i+1].funcId"
                  :tree-data="[allTreeList[i+1]]"
              >
              </role-authorized-tree>
            </a-col>
            <a-col :span="4" v-if="i+2 < allTreeList.length">
              <role-authorized-tree
                  :biz-checked-keys="this.allCheckedMap[allTreeList[i+2].funcId]"
                  :checked-keys="this.halfCheckedMap[allTreeList[i+2].funcId]"
                  :ref="'roleAuthorizedTreeRef_'+allTreeList[i+2].funcId"
                  :tree-data="[allTreeList[i+2]]"
              >
              </role-authorized-tree>
            </a-col>
            <a-col :span="4" v-if="i+3 < allTreeList.length">
              <role-authorized-tree
                  :biz-checked-keys="this.allCheckedMap[allTreeList[i+3].funcId]"
                  :checked-keys="this.halfCheckedMap[allTreeList[i+3].funcId]"
                  :ref="'roleAuthorizedTreeRef_'+allTreeList[i+3].funcId"
                  :tree-data="[allTreeList[i+3]]"
              >
              </role-authorized-tree>
            </a-col>
            <a-col :span="4" v-if="i+4 < allTreeList.length">
              <role-authorized-tree
                  :biz-checked-keys="this.allCheckedMap[allTreeList[i+4].funcId]"
                  :checked-keys="this.halfCheckedMap[allTreeList[i+4].funcId]"
                  :ref="'roleAuthorizedTreeRef_'+allTreeList[i+4].funcId"
                  :tree-data="[allTreeList[i+4]]"
              >
              </role-authorized-tree>
            </a-col>
            <a-col :span="4" v-if="i+5 < allTreeList.length">
              <role-authorized-tree
                  :biz-checked-keys="this.allCheckedMap[allTreeList[i+5].funcId]"
                  :checked-keys="this.halfCheckedMap[allTreeList[i+5].funcId]"
                  :ref="'roleAuthorizedTreeRef_'+allTreeList[i+5].funcId"
                  :tree-data="[allTreeList[i+5]]"
              >
              </role-authorized-tree>
            </a-col>
          </a-row>
        </template>
      </a-card>
    </a-spin>

    <a-modal  cancelText="取消" okText="确定" v-model:visible="tipShow"    :confirm-loading="confirmLoading"  :title="null" @ok="handleAuthorizedUpdate" @cancel="this.checkInfo  = {insertList: [],delList: []}">
      <div v-if="checkInfo.insertList && checkInfo.insertList.length > 0" style="text-align: center">
        <a-divider>新增功能</a-divider>
             <a-tag class="mb5" v-for="item in checkInfo.insertList" :key="item.funcId" color="green">{{ item.funcName }}</a-tag>
        </div>
      <div v-if="checkInfo.delList && checkInfo.delList.length > 0" style="text-align: center">
        <a-divider>删除功能</a-divider>
            <a-tag class="mb5" v-for="item in checkInfo.delList" :key="item.funcId" color="red">{{ item.funcName }}</a-tag>
        </div>
    </a-modal>

  </a-layout>
</template>

<script>

import {authRoleAuthorizedCheck, authRoleAuthorizedInit, authRoleAuthorizedUpdate} from "@/api/auth";
import RoleAuthorizedTree from "@/views/auth/role/RoleAuthorizedTree";

export default {
  name: "roleAuthorized.vue.vue",
  components: {
    RoleAuthorizedTree
  },
  data() {
    return {
        loading: false,
        confirmLoading: false,
        checkInfo: {
        insertList: [],
        delList: []
      },
      tipShow: false,
      initLoading: false,
      allTreeList: [],
      funcIdList: [],
      halfCheckedMap: {},
      allCheckedMap: {},
      roleInfo: {},
    }
  },
  methods: {
    handleExpandAll(){
      this.allTreeList.forEach(e => {
        let reference = this.$refs['roleAuthorizedTreeRef_' + e.funcId];
        reference.setExpandedKeys(this.getAllNodeKeys(e))
      })
    },
    handleCloseAll(){
      this.allTreeList.forEach(e => {
        let reference = this.$refs['roleAuthorizedTreeRef_' + e.funcId];
        reference.setExpandedKeys([])
      })
    },
    getAllNodeKeys(nodes) {
      let keys = [];
      if(nodes && nodes.funcId){
        keys.push(nodes.funcId);
        if(nodes.children && nodes.children.length > 0){
          for (let node of nodes.children) {
            keys = keys.concat(this.getAllNodeKeys(node));
          }
        }
      }
      return keys;
    },
    getBizCheckedKeysList(){
      let keys = [];
      this.allTreeList.forEach(e => {
        let reference = this.$refs['roleAuthorizedTreeRef_' + e.funcId];
        if (reference && reference.bizCheckedKeysList) {
          reference.bizCheckedKeysList.forEach(k => {
            keys.push(k);
          })
        }
      })
      return keys;
    },
    handleAuthorized() {
      this.loading = true;
      const param = {roleId: this.$route.query.roleId, funcIdList: this.getBizCheckedKeysList()}
      // 预校验
      authRoleAuthorizedCheck(param).then(res => {
        this.checkInfo = res.result;
        if (this.checkInfo.insertList.length === 0 && this.checkInfo.delList.length === 0) {
          this.handleAuthorizedUpdate();
        } else {
          this.tipShow = true;
        }
      }).finally(()=>{
         this.loading = false;
      });
    },
    handleAuthorizedUpdate(){
      this.initLoading = true;
      this.confirmLoading = true;
      const param = {roleId: this.$route.query.roleId, funcIdList: this.getBizCheckedKeysList()}
      authRoleAuthorizedUpdate(param).then(e => {
        this.$message.success("角色授权成功！");
        this.$router.push('/auth/role');
      }).finally(() => {
        this.initLoading = false;
        this.confirmLoading = false;
      })
    },
    initRoleAuthorizedData(roleId) {
      this.initLoading = true;
      authRoleAuthorizedInit(roleId).then(res => {
        this.roleInfo  = res.result;
        this.allTreeList = res.result.allTreeList;
        this.funcIdList = res.result.funcIdList;
        this.halfCheckedMap = res.result.halfCheckedMap;
        this.allCheckedMap = res.result.allCheckedMap;
      }).catch(e => {
        this.$router.push('/auth/role')
      }).finally(() => {
        this.initLoading = false;
      })
    }
  },
  created() {
    this.initRoleAuthorizedData(this.$route.query.roleId);
  }
}
</script>

<style scoped>
</style>