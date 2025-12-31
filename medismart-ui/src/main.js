import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// ElementUI
import {
  Autocomplete,
  Row,
  Col,
  Tabs,
  TabPane,
  Menu,
  MenuItem,
  Card,
  Carousel,
  CarouselItem,
  Form,
  FormItem,
  Upload,
  Step,
  Steps,
  Input,
  Radio,
  RadioGroup,
  Select,
  Option,
  Button,
  Tooltip,
  Message,
  MessageBox,
  Notification,
  Dialog,
  Loading,
} from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// Echarts
// Ant Design Vue
import {Button as AntButton, Card as AntCard, Drawer, Icon, Popconfirm} from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';

import './assets/style.scss' // global css
import './assets/icons' // 动态 icon 组件
import './permission'   //路由守卫

import plugins from "@/plugins";  //插件

Vue.config.productionTip = false

// ElementUI
Vue.use(Autocomplete);
Vue.use(Row);
Vue.use(Col);
Vue.use(Tabs);
Vue.use(TabPane);
Vue.use(Menu);
Vue.use(MenuItem);
Vue.use(Card);
Vue.use(Carousel);
Vue.use(CarouselItem);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Upload);
Vue.use(Step);
Vue.use(Steps);
Vue.use(Input);
Vue.use(Radio);
Vue.use(RadioGroup);
Vue.use(Select);
Vue.use(Option);
Vue.use(Button);
Vue.use(Tooltip);
Vue.use(Dialog);
Vue.use(Loading.directive);

Vue.prototype.$notify = Notification;
Vue.prototype.$message = Message;
// Vue.prototype.$loading = Loading.service;
// Vue.prototype.$msgbox = MessageBox;
// Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
// Vue.prototype.$prompt = MessageBox.prompt;

// Ant Design Vue
Vue.use(AntButton);
Vue.use(AntCard);
Vue.use(Drawer);
Vue.use(Icon);
Vue.use(Popconfirm);

// 懒加载
Vue.use(plugins);

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
}).$mount('#app')