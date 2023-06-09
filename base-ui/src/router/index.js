import Vue from 'vue'
import VueRouter from 'vue-router'
import { constantRouter } from './constantRouter'

Vue.use(VueRouter)

const createRouter = () => new VueRouter({
  // 不适用 history 模式
  routes: constantRouter,
})
const router = createRouter()

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}


export default router
