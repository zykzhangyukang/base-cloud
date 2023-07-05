<template>
    <a-card class="tree-card">
        <a-input-search size="small" v-model:value="searchValue" style="margin-bottom: 8px"  placeholder="查询" @change="onChange" />
      <a-spin size="small" :spinning="treeLoading">
        <a-tree
                :show-icon="true"
                :selectedKeys="selectedKeys"
                :expanded-keys="expandedKeys"
                :auto-expand-parent="autoExpandParent"
                :tree-data="treeData"
                @expand="onExpand"
                @select="select"
        >
            <!-- 节点切换图标 -->
            <template #switcherIcon>
                <down-outlined />
            </template>
            <!-- 目录关闭目标 -->
            <template #dir>
<!--                <img :src="require('@/assets/images/folder.svg')" class="tree-icon">-->
              <FolderOutlined />
            </template>
            <!-- 目录打开图标 -->
            <template #openDir>
<!--                <img :src="require('@/assets/images/folder-open.svg')" class="tree-icon" >-->
              <FolderOpenOutlined />
            </template>
            <!-- 功能图标 -->
            <template #func>
                <img :src="require('@/assets/images/func.svg')" class="tree-icon">
            </template>
            <template #title="{ title }">
                <span v-if="title.indexOf(searchValue) > -1">
                  {{ title.substr(0, title.indexOf(searchValue)) }}
                  <b style="color: #ed4014">{{ searchValue }}</b>
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
            resetSelect(){
               this.selectedKeys = [];
               this.searchValue = '';
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
            onExpand(expandedKeys, event) {
                this.expandedKeys = expandedKeys;
                this.autoExpandParent = false;
                if(event.node.dataRef.slots.icon !=='func'){
                    if (event.expanded) {
                        event.node.dataRef.slots.icon = 'openDir'
                    } else {
                        event.node.dataRef.slots.icon = 'dir'
                    }
                }
            },
            setTitleSlots(tree){
                if(tree){
                   tree.forEach(item=>{
                       item.slots = {title: 'title' , icon:  item.funcType};
                       if(item.children && item.children.length > 0){
                           this.setTitleSlots(item.children);
                       }
                   })
                }
            },
            async queryFuncTree() {
                this.treeLoading = true;
                try {
                    const res = await authFuncTree();
                    const tree = res.result;
                    this.setTitleSlots(tree)
                    this.treeData = tree;
                    return tree;
                } finally {
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
        height: 86vh;
        overflow: auto;
    }
    .tree{
        margin-top: 5px;
    }
    .tree-icon{
        width: 15px;
        height: 15px;
        user-select: none;
        -webkit-user-drag: none;
    }
</style>