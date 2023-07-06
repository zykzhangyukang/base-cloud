<template>
  <a-tree
      :tree-data="treeData"
      @check="checkTreeNode"
      :checkable="true"
      :expandedKeys="expandedKeys"
      :selectedKeys="selectedKeys"
      :checkedKeys="checkedKeys"
  >
  </a-tree>
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
    return {}
  },
  methods:{
    checkTreeNode(checkedKeys){
      this.$emit('success',checkedKeys,this.treeData)
    }
  }
}
</script>

<style scoped>

</style>