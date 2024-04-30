import Vue from 'vue'
import Vuex from 'vuex'

import user from "@/store/user";
import search from "@/store/search";

import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    user,
    search
  },
  getters
})
