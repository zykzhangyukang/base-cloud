<template>
    <a-layout class='index animated fadeIn'>
        <a-card>
            <div :style="{'textAlign':'right'}">
                <a-button type="danger" >添加</a-button>
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
                    <a-input  :style="{width:'180px'}" placeholder="测试输入框"></a-input>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" htmlType='submit'>搜索</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="default" >重置</a-button>
                </a-form-item>
            </a-form>
        </a-card>
    </a-layout>
</template>

<script>
    export default {
        name: "User.vue"
    }
</script>

<style scoped>

</style>