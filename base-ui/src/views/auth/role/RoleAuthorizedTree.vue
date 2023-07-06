<template>
  <div style="margin-top: 10px;margin-left: 10px;border: 1px solid #eee;border-radius: 5px;cursor: pointer">
    <a-tree
        :selectable="false"
        :tree-data="treeData"
        @check="checkTreeNode"
        :checkable="true"
        :expandedKeys="expandedKeys"
        :selectedKeys="selectedKeys"
        :checkedKeys="checkedKeys"
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
    selectedKeys: {
      type: Array
    },
    checkedKeys: {
      type: Array
    }
  },
  data() {
    return {
      businessSelectedRowKeys: [],
    }
  },
  methods:{
    checkTreeNode(checkedKeys, info){
      this.$emit('success',checkedKeys.concat(info.halfCheckedKeys),this.treeData)
    }
  }
}
</script>

<style scoped>

</style>