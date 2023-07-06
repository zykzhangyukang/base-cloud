<template>
  <a-layout class='role-authorized-container'>
    <a-card>
      <a-affix :offset-top="30">
        <div :style="{'textAlign':'right'}">
          <a-button :style="{'marginRight': '10px'}" @click="this.$router.push('/auth/role')">收缩全部</a-button>
          <a-button :style="{'marginRight': '10px'}" @click="this.$router.push('/auth/role')">展开全部</a-button>
          <a-button :style="{'marginRight': '10px'}" @click="this.$router.push('/auth/role')">返回列表</a-button>
          <a-button type="primary" @click="handleAuthorized">更新授权</a-button>
        </div>
      </a-affix>
      <div style="background-color: #f8f8f9;margin-top: 10px;border: 1px solid #eee"
           v-for="item in allTreeList"
           :key="item.funcId">
        <role-authorized-tree
                              :ref="'roleAuthorizedTreeRef_'+item.funcId"
                              :tree-data="[item]"
                              @success="checkNode"
        >
        </role-authorized-tree>
      </div>
    </a-card>
  </a-layout>
</template>

<script>

import {authRoleAuthorizedInit} from "@/api/auth";
import RoleAuthorizedTree from "@/views/auth/role/RoleAuthorizedTree";

export default {
  name: "roleAuthorized.vue.vue",
  components: {
    RoleAuthorizedTree
  },
  data() {
    return {
      allTreeList: [],
      allFuncKeyList: [],
      checkedInfo: {},
    }
  },
  methods: {
    checkNode(checkedKeys,treeData){
      this.checkedInfo[treeData[0].title] = checkedKeys;
    },
    handleAuthorized(){
      let keys = [];
      Object.keys(this.checkedInfo).forEach(item=>{
        let arr = this.checkedInfo[item];
        if(arr && arr.length > 0){
          arr.forEach(e=>{
            keys.push(e);
          })
        }
      })

    },
    initRoleAuthorizedData(roleId) {
      authRoleAuthorizedInit(roleId).then(res => {
        this.allTreeList = res.result.allTreeList;
      }).catch(e => {
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