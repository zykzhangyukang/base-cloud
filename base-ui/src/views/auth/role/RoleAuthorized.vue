<template>
  <a-layout class='role-authorized-container'>
    <a-affix :offset-top="0">
      <a-card>
        <div :style="{'textAlign':'right'}">
          <a-button :style="{'marginRight': '10px'}" @click="this.$router.push('/auth/role')">返回列表</a-button>
          <a-button type="primary">更新授权</a-button>
        </div>
      </a-card>
    </a-affix>
    <a-card>
      <role-authorized-tree v-for="item in allTreeList"
                            :key="item.funcId"
                            :ref="'roleAuthorizedTreeRef_'+item.funcId"
                            :tree-data="[item]">
      </role-authorized-tree>
    </a-card>
  </a-layout>
</template>

<script>

import {authRoleAuthorizedUpdateInit} from "@/api/auth";
import RoleAuthorizedTree from "@/views/auth/role/RoleAuthorizedTree";

export default {
  name: "roleAuthorized.vue.vue",
  components: {
    RoleAuthorizedTree
  },
  data() {
    return {
      allTreeList: [],
    }
  },
  methods: {
    initRoleAuthorizedData(roleId) {
      authRoleAuthorizedUpdateInit(roleId).then(res => {
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