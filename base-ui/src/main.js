import { createApp } from 'vue'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.less';
import './style/base.less'
import './style/App.less'
import App from './App.vue'
import router from './routers'
import permission from "@/utils/permission"
import store from './store'
import * as antIcons from '@ant-design/icons-vue'


const  app = createApp(App);

Object.keys(antIcons).forEach(key => {
    // @ts-ignore
    app.component(key, antIcons[key])
})
// 添加到全局
app.config.globalProperties.$antIcons = antIcons
app
    .use(Antd)
    .use(store)
    .use(router)
    .use(permission)
    .mount('#root')
 