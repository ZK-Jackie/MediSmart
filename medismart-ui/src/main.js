import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// Echarts
import * as echarts from 'echarts';
// Ant Design Vue
import {Button,Card,Drawer,Icon,Popconfirm} from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';

import './assets/style.scss' // global css

import './assets/icons' // 动态 icon 组件
import './permission'   //路由守卫
import plugins from "@/plugins";  //插件

Vue.config.productionTip = false
Vue.prototype.$echarts = echarts

Vue.use(ElementUI);
Vue.use(plugins);
Vue.use(Button);
Vue.use(Card);
Vue.use(Drawer);
Vue.use(Icon);
Vue.use(Popconfirm);

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
}).$mount('#app')
