import router from './router'
import store from './store'

import { Message, Notification } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

import {getToken} from '@/utils/auth'
import {isRelogin} from "@/utils/request";

NProgress.configure({showSpinner: false})

const whiteList = ["/", "/auth","/test"]

router.beforeEach((to, from, next) => {
  NProgress.start();
  if (getToken()) {
    // 有token
    if (to.path === '/auth' && from.path !== '/user') {
      next({ path: '/user' });
      NProgress.done();
      return;
    } else if (whiteList.indexOf(to.path) !== -1) {
      next()
      return;
    }else{
      if (store.getters.roles.length === 0) {
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(() => {
          isRelogin.show = false
          next();
        }).catch(err => {
          store.dispatch('LogOut').then(() => {
            Message.error(err)
            next({ path: '/' });
          })
        })
      } else {
        next();
      }
      NProgress.done();
      return;
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next();
      return;
    } else {
      next(`/auth?redirect=${encodeURIComponent(to.fullPath)}`) // 否则全部重定向到登录页
      NProgress.done();
      return;
    }
  }
});

router.afterEach(() => {
  NProgress.done()
})