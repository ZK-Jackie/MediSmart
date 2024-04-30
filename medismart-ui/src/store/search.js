import { fuzzySearch, list, map } from '@/api/graph';
import {getToken, setToken, removeToken} from '@/utils/auth'

const search = {
  state: {
    fuzzy: {},
    list: {},
    map: {},
    graph: {}
  },
  mutations: {
    SET_FUZZY: (state, fuzzy) => {
      state.fuzzy = fuzzy
    },
    SET_LIST: (state, list) => {
      state.list = list
    },
    SET_MAP: (state, map) => {
      state.map = map
    }
  },

  actions: {
    // 模糊查询
    fuzzySearch({commit}, searchInfo) {
      const keyword = searchInfo.keyword.trim();
      const pageNow = searchInfo.pageNow;
      const pageSize = searchInfo.pageSize;
      const pageTotal = searchInfo.pageTotal;
      return new Promise((resolve, reject) => {
        fuzzySearch(keyword, pageNow, pageSize, pageTotal).then(res => {
          commit('SET_FUZZY', res.data)
          resolve(res.data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 精准查找
    listSearch({commit}, searchInfo) {
      const keyword = searchInfo.keyword.trim();
      const pageNow = searchInfo.pageNow;
      const pageSize = searchInfo.pageSize;
      const pageTotal = searchInfo.pageTotal;
      return new Promise((resolve, reject) => {
        list(keyword, pageNow, pageSize, pageTotal).then(res => {
          const key = Object.keys(res.data)[0];
          const value = Object.values(res.data)[0][0];
          commit('SET_LIST', value)
          resolve(value)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 精准查找
    map({commit}, centerNodeId) {
      return new Promise((resolve, reject) => {
        map(centerNodeId).then(res => {
          commit('SET_MAP', res.data)
          resolve(res.data)
        }).catch(error => {
          reject(error)
        })
      })
    },
  }
}

export default search
