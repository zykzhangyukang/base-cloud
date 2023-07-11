<template>
    <a-modal v-model:visible="visible"
             title="用户分配角色"
             @ok="handleOk"
             @cancel="handleClose"
             :confirm-loading="confirmLoading"
             cancelText="取消"
             okText="提交"
             ref="form"
    >
        <a-transfer
                :list-style="{
                  width: '210px',
                  height: '310px',
                }"
                :data-source="dataList"
                :render="item => item.title"
                :target-keys="targetKeys"
                @change="handleChange"
                show-search
        />
    </a-modal>
</template>

<script>
import {authUserRoleInit, authUserRoleUpdate} from "@/api/auth";
export default {
        name: "userAssignRole.vue",
        data() {
            return {
                form: {
                    userId: null,
                    roleIdList: [],
                },
                dataList:[],
                targetKeys: [],
                confirmLoading:  false,
                visible: false,
            }
        },
        computed:{
        },
        methods:{
            handleOk() {
                this.confirmLoading = true;
                this.form.roleIdList = this.targetKeys;
                authUserRoleUpdate(this.form).then(res => {
                    this.$message.success("分配角色成功！");
                    this.handleClose();
                }).finally(e=>{
                    this.confirmLoading =false;
                })
            },
            handleClose(){
                this.visible = false
                this.confirmLoading = false;
                this.form = this.$options.data().form;
            },
            handleChange(targetKeys, direction, moveKeys){
                this.targetKeys = targetKeys;
            },
            open(userId){
                this.visible = true;
                this.form.userId = userId;
                authUserRoleInit(userId).then(res=>{
                    const { roleList, assignedIdList } = res.result

                    const dataList =[];
                    const targetKeys = [];

                    if(roleList && roleList.length > 0){
                        roleList.forEach(item=>{
                            const data = {
                                key: item.roleId.toString(),
                                title: item.roleName,
                                description: item.roleDesc,
                            };
                            dataList.push(data);
                        })
                    }
                    this.dataList = dataList;

                    if(assignedIdList && assignedIdList.length >0){
                        assignedIdList.forEach(item=>{
                            targetKeys.push(item.toString());
                        })
                    }
                    this.targetKeys = targetKeys;
                })
            }
        }
    }
</script>

<style scoped>

</style>