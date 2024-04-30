import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('../views/home/index.vue')
  },
  {
    path: '/chat',
    name: 'chat',
    component: () => import('../views/room/index.vue')
  },
  {
    path: '/graph',
    name: 'graph',
    component: () => import('../views/graph/index.vue')
  },
  {
    path: '/auth',
    name: 'auth',
    component: () => import('../views/user/auth.vue')
  },
  {
    path: '/user',
    name: 'user',
    component: () => import('../views/user/profile')
  },
]

// 防止连续点击多次路由报错
let routerPush = VueRouter.prototype.push;
let routerReplace = VueRouter.prototype.replace;
// push
VueRouter.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
// replace
VueRouter.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}

const router = new VueRouter({
  routes,
  mode: 'history'
})

export default router
