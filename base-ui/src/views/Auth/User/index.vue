<template>
    <a-layout class='user-container'>
        <a-card>
            <div :style="{'textAlign':'right'}">
                <a-button type="danger">添加</a-button>
            </div>
            <a-form
                    ref='form'
                    :style="{'marginBottom':'10px'}"
                    layout='inline'
            >
                <a-form-item label="筛选项1" name='name1'>
                    <a-input :style="{width:'180px'}" placeholder="测试输入框"></a-input>
                </a-form-item>
                <a-form-item label="筛选项2" name='name2'>
                    <a-input :style="{width:'180px'}" placeholder="测试输入框"></a-input>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" htmlType='submit'>搜索</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default">重置</a-button>
                </a-form-item>
            </a-form>
            <HTable
                    :pagination='false'
                    :loading='listLoading'
                    bordered
                    rowKey='id'
                    :columns='columns'
                    :data-source='data'></HTable>
            <HPage
                    :current="1"
                    :page-size="10"
                    :total="100" >
            </HPage>
        </a-card>
    </a-layout>
</template>

<script>
    import HTable from '@/components/HTable'
    import HPage from "@/components/HPage";
    export default {
        name: "User.vue",
        components: {
            HTable,
            HPage
        },
        data() {
            return {
                listLoading: false,
                columns: [{
                    title: 'ID',
                    dataIndex: 'id',
                    key: 'id',
                    },
                    {
                        title: '昵称',
                        dataIndex: 'nickname',
                        key: 'id',
                    },
                    {
                        title: '姓名',
                        dataIndex: 'name',
                        key: 'name',
                    },
                    {
                        title: '年龄',
                        dataIndex: 'age',
                        key: 'age',
                    },
                    {
                        title: '地址',
                        dataIndex: 'address',
                        key: 'address',
                    }],
                data: [],
            }
        }
    }
</script>

<style scoped>

</style>