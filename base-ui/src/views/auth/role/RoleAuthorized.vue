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
      <template v-for="(item, i) in allTreeList" >
        <a-row :gutter="24" :key="i" v-if='i%6 === 0'>
          <a-col :span="4">
            <role-authorized-tree
                :ref="'roleAuthorizedTreeRef_'+item.funcId"
                :tree-data="[item]"
                @success="checkNode"
            >
            </role-authorized-tree>
          </a-col>
          <a-col :span="4" v-if="i+1 < allTreeList.length">
            <role-authorized-tree
                :ref="'roleAuthorizedTreeRef_'+allTreeList[i+1].funcId"
                :tree-data="[allTreeList[i+1]]"
                @success="checkNode"
            >
            </role-authorized-tree>
          </a-col>
          <a-col :span="4" v-if="i+2 < allTreeList.length">
            <role-authorized-tree
                :ref="'roleAuthorizedTreeRef_'+allTreeList[i+2].funcId"
                :tree-data="[allTreeList[i+2]]"
                @success="checkNode"
            >
            </role-authorized-tree>
          </a-col>
          <a-col :span="4" v-if="i+3 < allTreeList.length">
            <role-authorized-tree
                :ref="'roleAuthorizedTreeRef_'+allTreeList[i+3].funcId"
                :tree-data="[allTreeList[i+3]]"
                @success="checkNode"
            >
            </role-authorized-tree>
          </a-col>
          <a-col :span="4" v-if="i+4 < allTreeList.length">
            <role-authorized-tree
                :ref="'roleAuthorizedTreeRef_'+allTreeList[i+4].funcId"
                :tree-data="[allTreeList[i+4]]"
                @success="checkNode"
            >
            </role-authorized-tree>
          </a-col>
          <a-col :span="4" v-if="i+5 < allTreeList.length">
            <role-authorized-tree
                :ref="'roleAuthorizedTreeRef_'+allTreeList[i+5].funcId"
                :tree-data="[allTreeList[i+5]]"
                @success="checkNode"
            >
            </role-authorized-tree>
          </a-col>
        </a-row>
      </template>
    </a-card>
  </a-layout>
</template>

<script>

import {authRoleAuthorizedInit, authRoleAuthorizedUpdate} from "@/api/auth";
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
      const param = {roleId: this.$route.query.roleId , funcKeys: keys}
      authRoleAuthorizedUpdate(param).then(e=>{
        this.$message.success("角色授权成功！");
        this.$router.push('/auth/role');
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