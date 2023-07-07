<template>
  <a-layout class='role-authorized-container'>
    <a-spin :spinning="initLoading" size="large">
      <a-card>
        <a-affix :offset-top="30">
          <div :style="{'textAlign':'right'}">
            <a-button :style="{'marginRight': '10px'}" @click="this.$router.push('/auth/role')">收缩全部</a-button>
            <a-button :style="{'marginRight': '10px'}" @click="this.$router.push('/auth/role')">展开全部</a-button>
            <a-button :style="{'marginRight': '10px'}" @click="this.$router.push('/auth/role')">返回列表</a-button>
            <a-button type="primary" @click="handleAuthorized">更新授权</a-button>
          </div>
        </a-affix>
        <template v-for="(item, i) in allTreeList">
          <a-row :gutter="24" :key="i" v-if='i%6 === 0'>
            <a-col :span="4">
              <role-authorized-tree
                  :expanded-keys="[item.funcId]"
                  :biz-checked-keys="this.allCheckedMap[item.funcId]"
                  :checked-keys="this.halfCheckedMap[item.funcId]"
                  :ref="'roleAuthorizedTreeRef_'+item.funcId"
                  :tree-data="[item]"
              >
              </role-authorized-tree>
            </a-col>
            <a-col :span="4" v-if="i+1 < allTreeList.length">
              <role-authorized-tree
                  :expanded-keys="[allTreeList[i+1].funcId]"
                  :biz-checked-keys="this.allCheckedMap[allTreeList[i+1].funcId]"
                  :checked-keys="this.halfCheckedMap[allTreeList[i+1].funcId]"
                  :ref="'roleAuthorizedTreeRef_'+allTreeList[i+1].funcId"
                  :tree-data="[allTreeList[i+1]]"
              >
              </role-authorized-tree>
            </a-col>
            <a-col :span="4" v-if="i+2 < allTreeList.length">
              <role-authorized-tree
                  :expanded-keys="[allTreeList[i+2].funcId]"
                  :biz-checked-keys="this.allCheckedMap[allTreeList[i+2].funcId]"
                  :checked-keys="this.halfCheckedMap[allTreeList[i+2].funcId]"
                  :ref="'roleAuthorizedTreeRef_'+allTreeList[i+2].funcId"
                  :tree-data="[allTreeList[i+2]]"
              >
              </role-authorized-tree>
            </a-col>
            <a-col :span="4" v-if="i+3 < allTreeList.length">
              <role-authorized-tree
                  :expanded-keys="[allTreeList[i+3].funcId]"
                  :biz-checked-keys="this.allCheckedMap[allTreeList[i+3].funcId]"
                  :checked-keys="this.halfCheckedMap[allTreeList[i+3].funcId]"
                  :ref="'roleAuthorizedTreeRef_'+allTreeList[i+3].funcId"
                  :tree-data="[allTreeList[i+3]]"
              >
              </role-authorized-tree>
            </a-col>
            <a-col :span="4" v-if="i+4 < allTreeList.length">
              <role-authorized-tree
                  :expanded-keys="[allTreeList[i+4].funcId]"
                  :biz-checked-keys="this.allCheckedMap[allTreeList[i+4].funcId]"
                  :checked-keys="this.halfCheckedMap[allTreeList[i+4].funcId]"
                  :ref="'roleAuthorizedTreeRef_'+allTreeList[i+4].funcId"
                  :tree-data="[allTreeList[i+4]]"
              >
              </role-authorized-tree>
            </a-col>
            <a-col :span="4" v-if="i+5 < allTreeList.length">
              <role-authorized-tree
                  :expanded-keys="[allTreeList[i+5].funcId]"
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
      initLoading: false,
      allTreeList: [],
      funcIdList: [],
      halfCheckedMap: {},
      allCheckedMap: {},
    }
  },
  methods: {
    handleAuthorized() {
      let keys = [];
      this.initLoading = true;
      this.allTreeList.forEach(e => {
        let reference = this.$refs['roleAuthorizedTreeRef_' + e.funcId];
        if (reference && reference.bizCheckedKeysList) {
          reference.bizCheckedKeysList.forEach(k => {
            keys.push(k);
          })
        }
      })
      const param = {roleId: this.$route.query.roleId, funcIdList: keys}
      authRoleAuthorizedUpdate(param).then(e => {
        this.$message.success("角色授权成功！");
        this.$router.push('/auth/role');
      }).finally(() => {
        this.initLoading = false;
      })
    },
    initRoleAuthorizedData(roleId) {
      this.initLoading = true;
      authRoleAuthorizedInit(roleId).then(res => {
        this.allTreeList = res.result.allTreeList;
        this.funcIdList = res.result.funcIdList;
        this.halfCheckedMap = res.result.halfCheckedMap;
        this.allCheckedMap = res.result.allCheckedMap;
      }).catch(e => {
        console.log(e)
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