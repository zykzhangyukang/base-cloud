<template>
  <div style="margin-top: 10px;margin-left: 10px;border: 1px solid #eee;border-radius: 5px;cursor: pointer">
    <a-tree
        :selectable="false"
        :tree-data="treeDataList"
        :checkable="true"
        @check="checkTreeNode"
        @expand="expand"
        :expandedKeys="expandedKeysList"
        :checkedKeys="checkedKeysList"
        :replaceFields="{children:'children', title:'funcName', key:'funcId' }"
    >
    </a-tree>
  </div>
</template>

<script>
export default {
  name: "RoleAuthorizedTree.vue",
  props: {
    treeData: {
      type: Array
    },
    expandedKeys: {
      type: Array
    },
    checkedKeys: {
      type: Array
    },
    bizCheckedKeys: {
      type: Array
    }
  },
  data() {
    return {
      treeDataList: [],
      expandedKeysList: [],
      checkedKeysList: [],
      bizCheckedKeysList: []
    }
  },
  methods:{
    checkTreeNode(checkedKeys, info){
      this.checkedKeysList = checkedKeys;
      this.bizCheckedKeysList = checkedKeys.concat(info.halfCheckedKeys);
    },
    expand(expandedKeys){
      this.expandedKeysList = expandedKeys;
    },
    setExpandedKeys(expandedKeys){
      this.expandedKeysList = expandedKeys;
    }
  },
  created() {
    this.treeDataList = this.treeData;
    this.expandedKeysList = this.expandedKeys;
    this.checkedKeysList = this.checkedKeys;
    this.bizCheckedKeysList = this.bizCheckedKeys;
  }
}
</script>

<style scoped>

</style>