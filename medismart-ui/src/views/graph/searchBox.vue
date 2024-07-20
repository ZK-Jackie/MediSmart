<template>
  <div>
    <el-autocomplete
        v-model="searchInfo.keyword"
        :fetch-suggestions="fuzzyQuery"
        placeholder="输入你想知道的内容"
        @select="handleSelect"
    >
      <template slot-scope="{ item }">
        <div class="search-result-title">{{ item.name }}</div>
        <span class="search-result-subtitle">{{ item.category }}</span>
      </template>
    </el-autocomplete>
    <graph-add-dialog v-if="isAddDialogVisible" v-model="isAddDialogVisible"></graph-add-dialog>
  </div>
</template>

<script>
import {getKeys} from "@/utils/array";
import {UUID} from "@/utils/string";
import {edit} from "@/api/graph";
import graphAddDialog from "@/views/graph/graph-add-dialog.vue";

export default {
  components:{
    graphAddDialog
  },
  data() {
    return {
      isAddDialogVisible: false,
      searchInfo: {
        keyword: '',
        pageNow: 1,
        pageSize: 5,
        pageTotal: 0,
      },
      queryTimer: null,
    }
  },
  methods: {
    fuzzyQuery(queryString, callback) {
      if(!queryString) {
        callback([]);
        return;
      }
      clearTimeout(this.queryTimer);
      this.queryTimer = setTimeout(() => {
        this.$store.dispatch('fuzzySearch', this.searchInfo).then(res => {
          // 1.从res数组中获取所有的keys
          let keys = getKeys(res);
          // 2.构造一个返回值数组
          let ret = [];
          keys.forEach(key => {
            res[key].forEach(item => {
              ret.push(item);
            });
          });
          if(ret.length === 0) {
            ret.push({
              id: -1,
              name: '未找到相关内容',
              category: '点击添加'
            });
          }else{
            ret.push({
              id: -1,
              name: '没有找到想要的内容？',
              category: '点击添加'
            });
          }
          callback(ret);
        });
      }, 3000 * Math.random());
    },
    handleSelect(item) {
      if(item.id === -1) {
        this.isAddDialogVisible = true;
        return;
      }
      this.$store.commit('SET_LIST', item);
      this.$emit('alert', true);
    }
  }
}
</script>


<style scoped>
.search-result-title {
  font-size: 16px;
  color: #303133;
}
.search-result-subtitle {
  font-size: 12px;
  color: #606266;
}
</style>