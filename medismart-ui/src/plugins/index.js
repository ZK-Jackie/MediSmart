import cache from './cache'
import modal from './modal'
import lazyEcharts from './lazy-echarts'

export default {
  install(Vue) {
    // 缓存对象
    Vue.prototype.$cache = cache
    // 模态框对象
    Vue.prototype.$modal = modal
    // 懒加载的 echarts
    Vue.prototype.$echarts = lazyEcharts
  }
}
