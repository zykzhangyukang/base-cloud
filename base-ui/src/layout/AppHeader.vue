<template>
    <a-layout-header class='header'>
        <div class="left">
            <MenuUnfoldOutlined v-if='menuToggle' @click="menuClick"/>
            <MenuFoldOutlined v-else @click="menuClick"/>
            <div :style='{marginLeft:"10px"}'>
                <header-breadcrumb :arr='breadCrumb'></header-breadcrumb>
            </div>
        </div>
        <div class="right">
            <div class="mr15" @click='screenFullChange'>
                <ShrinkOutlined v-if='isScreenFull'/>
                <ArrowsAltOutlined v-else/>
            </div>
            <div>
                <a-dropdown>
                    <template v-slot:overlay>
                        <a-menu :style="{ width: '15rem' }">
                            <a-menu-item-group title='用户设置'>
                                <a-menu-divider/>
                                <a-menu-item>
                                    <EditOutlined/>
                                    个人设置
                                </a-menu-item>
                                <a-menu-item>
                                    <SettingOutlined/>
                                    系统设置
                                </a-menu-item>
                                <a-menu-item @click="handleRefreshLogin">
                                    <ReloadOutlined/>
                                    刷新登录
                                </a-menu-item>
                            </a-menu-item-group>
                            <a-menu-divider/>
                            <a-menu-item>
                                <div @click='loginOut'>
                                    <LogoutOutlined/>
                                    退出登录
                                </div>
                            </a-menu-item>
                        </a-menu>
                    </template>
                    <div class='ant-dropdown-link'>
                        <a-avatar :src='avatar' alt='avatar' :style="{ cursor: 'pointer' }">
                            <template v-slot:icon>
                                <UserOutlined/>
                            </template>
                        </a-avatar>
                    </div>
                </a-dropdown>
            </div>
        </div>
    </a-layout-header>
</template>
<script>
    import {
        MenuUnfoldOutlined,
        MenuFoldOutlined,
        ShrinkOutlined,
        ArrowsAltOutlined,
        EditOutlined,
        SettingOutlined,
        LogoutOutlined,
        ExclamationCircleOutlined,
        UserOutlined
    } from '@ant-design/icons-vue';
    import HeaderBreadcrumb from './HeaderBreadcrumb';
    import screenfull from 'screenfull';
    import { defineComponent, ref, createVNode } from 'vue';
    import { Modal } from 'ant-design-vue';
    import {authUserRefreshLogin} from "@/api/auth";
    import store from "@/store";

    export default {
        name: 'AppHeader',
        components: {
            MenuUnfoldOutlined,
            MenuFoldOutlined,
            ShrinkOutlined,
            ArrowsAltOutlined,
            HeaderBreadcrumb,
            EditOutlined,
            SettingOutlined,
            LogoutOutlined,
            UserOutlined
        },
        props: {
            menuClick: {
                required: true,
                type: Function
            },
            loginOut: {
                required: true,
                type: Function
            },
            menuToggle: {
                required: true,
                type: Boolean
            },
            avatar: String,
            breadCrumb: Array,
        },
        data() {
            return {
                isScreenFull: false,
            }
        },
        computed:{
            user() {
                return store.state.user;
            }
        },
        methods: {
            handleRefreshLogin() {
                let _this = this;
                Modal.confirm({
                    title: '刷新登录',
                    icon: createVNode(ExclamationCircleOutlined),
                    content: '您确定是否刷新登录，页面将重新加载！',
                    okText: '确认',
                    cancelText: '取消',
                    onOk() {
                        let msg = _this.$message.loading("正在刷新登录...", 0.8);
                       authUserRefreshLogin().then(res=>{
                           msg.then(e=>{
                               localStorage.setItem('token', res.result);
                               store.setUserToken(res.result);
                               window.location.reload();
                           })
                       })
                    },
                });
            },
            screenFullChange() {
                if (!screenfull.isEnabled) {
                    this.$message.warning('你的浏览器不支持全屏');
                    return false
                }
                this.screenfull = !this.isScreenFull;
                screenfull.toggle()
            },
        }
    }
</script>