<template>
    <el-container>
        <el-aside class="left-aside">
            <my-menu></my-menu>
        </el-aside>
        <el-container>
            <el-header class="top-header">
                <my-header></my-header>
                <tabs-view />
            </el-header>
            <el-main>
                <my-main></my-main>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
    import MyHeader from '../layouts/header'
    import TabsView from '../layouts/tagview'
    import MyMain from '../layouts/main'
    import MyMenu from '../layouts/menu'
    export default {
        name: 'BasicLayout',
        components: { MyHeader, TabsView, MyMain , MyMenu}
    }
</script>

<style scoped>
    .left-aside{
        width: 200px!important;
        overflow-x: hidden;
    }
    .top-header{
        padding: 0 !important;
    }
</style>