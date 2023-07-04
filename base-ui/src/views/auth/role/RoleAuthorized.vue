<template>
  <a-layout class='role-authorized-container'>
    <a-card>
      <div :style="{'textAlign':'right'}">
        <a-button :style="{'marginRight': '10px'}" @click="this.$router.push('/auth/role')">返回列表</a-button>
        <a-button type="primary">更新授权</a-button>
      </div>
      <!-- 权限树 -->
      <a-tree
          checkable
          :tree-data="treeData"
          v-model:expandedKeys="expandedKeys"
          v-model:selectedKeys="selectedKeys"
          v-model:checkedKeys="checkedKeys"
      >
        <template #title0010><span style="color: #1890ff">sss</span></template>
      </a-tree>
    </a-card>
  </a-layout>
</template>

<script>

import {authRoleAuthorizedUpdateInit} from "@/api/auth";

export default {
  name: "roleAuthorized.vue.vue",
  data() {
    return {
      treeData: [
      ],
      expandedKeys: [],
      selectedKeys: [],
      checkedKeys: [],
    }
  },
  methods:{
    initRoleAuthorizedData(roleId){
      authRoleAuthorizedUpdateInit(roleId).then(res=>{
        this.treeData = res.result.allTreeList;
        this.expandedKeys = ["Root"];
      }).catch(e=>{
        this.$router.push('/auth/role')
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