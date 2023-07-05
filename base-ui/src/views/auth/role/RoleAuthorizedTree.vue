<template>
    <a-tree
        :tree-data="treeData"
        checkable
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
}
</script>

<style scoped>

</style>