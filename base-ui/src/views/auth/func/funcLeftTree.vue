<template>
    <a-card class="tree-card">
        <a-input-search size="small" style="margin-bottom: 8px" placeholder="查询" @change="onChange" />
      <a-spin size="small" :spinning="treeLoading">
        <a-tree
                show-line
                :selectedKeys="selectedKeys"
                :expanded-keys="expandedKeys"
                :auto-expand-parent="autoExpandParent"
                :tree-data="treeData"
                @expand="onExpand"
                @select="select"
        >
            <template #title="{ title }">
                <span v-if="title.indexOf(searchValue) > -1">
                  {{ title.substr(0, title.indexOf(searchValue)) }}
                  <span style="color: #f50">{{ searchValue }}</span>
                  {{ title.substr(title.indexOf(searchValue) + searchValue.length) }}
                </span>
                <span v-else>{{ title }}</span>
            </template>
        </a-tree>
      </a-spin>
    </a-card>
</template>

<script>
    import {authFuncTree} from "@/api/auth";
    const getParentKey = (key, tree) => {
        let parentKey;
        for (let i = 0; i < tree.length; i++) {
            const node = tree[i];
            if (node.children) {
                if (node.children.some(item => item.key === key)) {
                    parentKey = node.key;
                } else if (getParentKey(key, node.children)) {
                    parentKey = getParentKey(key, node.children);
                }
            }
        }
        return parentKey;
    };
    const dataList = [];
    const generateList = data => {
        for (let i = 0; i < data.length; i++) {
            const node = data[i];
            const key = node.key;
            dataList.push({ key, title: node.title, funcId: node.value });
            if (node.children) {
                generateList(node.children);
            }
        }
    };
    export default {
        name: "funcLeftTree.vue",
        data(){
            return {
                dataList: [],
                expandedKeys: [],
                searchValue: '',
                treeLoading: false,
                autoExpandParent: true,
                treeData: [],
                selectedKeys:[]
            }
        },
        methods:{
            reloadTree(){
               this.queryFuncTree();
            },
            resetSelect(){
               this.selectedKeys = [];
            },
            select(e){
                this.selectedKeys = e;
                const funcKey = e[0];
                let item = null;
                for (let i = 0; i < dataList.length; i++) {
                    if(dataList[i].key === funcKey){
                        item = dataList[i];
                        break;
                    }
                }
                this.$emit("select-node",item);
            },
            onExpand(expandedKeys) {
                this.expandedKeys = expandedKeys;
                this.autoExpandParent = false;
            },
            async queryFuncTree(){
              this.treeLoading = true;
              try {
                const res = await authFuncTree();
                const tree  = res.result;
                this.treeData = tree;
                this.expandedKeys  = ["root"];
                return tree;
              }finally {
                this.treeLoading = false;
              }
            },
            onChange(e) {
                const value = e.target.value;
                const expandedKeys = dataList.map(item => {
                        if (item.title.indexOf(value) > -1) {
                            return getParentKey(item.key, this.treeData);
                        }
                        return null;
                    })
                    .filter((item, i, self) => item && self.indexOf(item) === i);
                Object.assign(this, {
                    expandedKeys,
                    searchValue: value,
                    autoExpandParent: true,
                });
            },
        },
        created() {
            this.queryFuncTree().then(tree=>{
                generateList(tree)
            })

        }
    }
</script>

<style scoped>
    .tree-card{
        height: 85vh;
        overflow: auto;
    }
    .tree{
        margin-top: 5px;
    }
</style>