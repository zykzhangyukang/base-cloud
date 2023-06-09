<template>
    <div class="menu-container">
        <el-menu
                class="left-menu"
                default-active="2"
                :open="1"
                background-color="#191a23"
                text-color="#fff"
                active-text-color="#2d8cf0"
        >

            <el-submenu index="1">
                <template slot="title">
                    <i class="el-icon-folder-opened"></i>
                    <span slot="title">权限系统</span>
                </template>
                <el-menu-item v-for="item in menus2" :key="item.path" :index="item.path" >
                    <i :class="item.icon"></i>
                    <span slot="title">{{item.name}}</span>
                </el-menu-item>
            </el-submenu>

            <el-submenu index="2">
                <template slot="title">
                    <i class="el-icon-folder-opened"></i>
                    <span slot="title">同步系统</span>
                </template>
                <el-menu-item v-for="item in menus" :key="item.path" :index="item.path" >
                    <i :class="item.icon"></i>
                    <span slot="title">{{item.name}}</span>
                </el-menu-item>
            </el-submenu>

        </el-menu>
    </div>
</template>

<script>
    export default {
        name: "MyMenu.vue",
        data() {
            return {
                menus:[
                    {
                        name: '系统首页',
                        icon: 'el-icon-s-home',
                        path: '/sync/index',
                    },
                    {
                        name: '同步计划',
                        icon: 'el-icon-s-grid',
                        path: '/sync/plan',
                    },
                    {
                        name: '同步记录',
                        icon: 'el-icon-s-marketing',
                        path: '/sync/result',
                    },
                    {
                        name: '消息列表',
                        icon: 'el-icon-tickets',
                        path: '/sync/message',
                    },
                    {
                        name: '回调列表',
                        icon: 'el-icon-timer',
                        path: '/sync/callback',
                    }
                ],
                menus2:[
                    {
                        name: '用户管理',
                        icon: 'el-icon-s-home',
                        path: '/auth/user',
                    },
                    {
                        name: '角色管理',
                        icon: 'el-icon-s-grid',
                        path: '/auth/role',
                    },
                    {
                        name: '资源管理',
                        icon: 'el-icon-s-marketing',
                        path: '/auth/res',
                    },
                    {
                        name: '功能管理',
                        icon: 'el-icon-tickets',
                        path: '/auth/func',
                    }
                ]
            }
        },
        methods: {

        },
    }
</script>

<style>
    .el-menu {
        border-right-width: 0 !important;
    }
    .left-menu{
        height: 100vh;
    }
</style>
