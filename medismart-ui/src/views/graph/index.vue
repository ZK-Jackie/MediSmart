<script>
import {isJsonEqual} from "@/utils/jsObj";
import {toEchartsData} from "@/utils/graph";
import {edit, fuzzySearch} from "@/api/graph";
import searchBox from "@/views/graph/searchBox.vue";
import graphChart from "@/views/graph/graph-chart.vue";
import HistoryCard from "@/views/room/components/historyCard.vue";

export default {
  components: {
    HistoryCard,
    searchBox, graphChart
  },
  watch: {
    drawerNodeInfo: {
      handler: function () {
        this.isCommitDisabled = isJsonEqual(this.centerNodeInfo, this.drawerNodeInfo);
      },
      deep: true
    }
  },
  computed: {},
  data() {
    return {
      // 抽屉禁用编辑列表
      disabledList: ["id", "category", "uid", "create_time", "update_time", "delete_time", "delete_flag"],
      // 禁用所有编辑功能（只读模式）
      readOnlyMode: true,
      // 抽屉整体参数
      drawerVisible: false,
      drawWidth: '',
      // 抽屉内容参数
      drawerNodeInfo: {},
      newTagInputVisible: {
        init: false
      },  // 根据属性值，会临时创建不同的Visible控制器
      newTagInput: '',
      isCommitDisabled: false,
      // 知识图谱参数
      centerNodeInfo: {},
      chartData: {},
      isLoadingChart: false,
    }
  },
  mounted() {
    //TODO 添加一个组件  搜索框周围放事例查询关键词  让用户点击试查
  },
  methods: {
    handleSearch() {
      // 1.打开抽屉，开始加载动画
      this.drawerVisible = true;
      this.isLoadingChart = true;
      // 2.根据搜索框的内容，获取中心节点信息
      this.centerNodeInfo = this.$store.getters.list;
      debugger;
      this.$store.dispatch('map', this.centerNodeInfo.id).then(() => {
        // console.log(this.centerNodeInfo.id + " map success");
        this.chartData = toEchartsData(this.centerNodeInfo, this.$store.getters.map);
        this.isLoadingChart = false;
        this.drawerNodeInfo = JSON.parse(JSON.stringify(this.centerNodeInfo));
        // 初始化标签输入框
        for (let key in this.drawerNodeInfo) {
          if(Array.isArray(this.drawerNodeInfo[key])) {
            this.newTagInputVisible[key] = false;
          }
        }
        // 抽屉显示
        this.drawerVisible = true;
      });
    },
    handleDrawerEdit() {
      edit(this.drawerNodeInfo).then(res => {
        this.$message({
          message: '修改成功',
          type: 'success'
        });
      });
    },
    handleTagClose(key, tag) {
      // 删除标签
      // this.drawerNodeInfo[key].splice(this.drawerNodeInfo[key].indexOf(tag), 1);
      this.$message({
        message: '权限不足，无法删除标签',
        type: 'warning'
      });
    },
    handleNewTagInputConfirm(key) {
      alert();
      if(this.newTagInput === "") {
        this.newTagInputVisible[key] = false;
        return;
      }
      const inputValue = this.newTagInput.trim();
      if (inputValue) {
        this.drawerNodeInfo[key].push(inputValue);
      }
      this.newTagInputVisible[key] = false;
      this.newTagInput = '';
    },
    handleNewTagClick(key) {
      // TODO 无法获取到ref，无法切换按钮状态，待修正
      // this.newTagInput = "";
      // this.newTagInputVisible[key] = true;
      // console.log(this.$refs);
      // console.log(this.newTagInputVisible[key]);
      // this.$nextTick(_ => {
      //   // console.log(this.$refs);
      //   // this.$refs[hideTarget][0].display = 'none';
      //   // this.$refs[refTarget][0].$refs.input.focus();
      // });
      this.$message({
        message: '权限不足，无法添加标签',
        type: 'warning'
      });
    }
  }
}
</script>

<template>
  <div class="graph">
    <searchBox class="search-box" @alert="handleSearch"></searchBox>
    <div class="graph-viewer">
      <a-drawer class="graph-viewer-drawer"
                title="节点详情"
                ref="drawer"
                v-if="drawerVisible"
                :visible="drawerVisible"
                :placement="'left'"
                :width="400"
                :closable="false"
                :mask="false"
                :get-container="false"
                :drawerStyle="{ borderRadius: '10px'}"
                :wrap-style="{ position: 'absolute' }"
      >
        <div class="viewer-drawer-header">

        </div>
        <div v-show="drawerVisible" class="viewer-drawer-content">
          <el-form label-position="top" label-width="80px" :model="drawerNodeInfo">
            <el-form-item v-for="(_, key) in drawerNodeInfo" :key="key" :label="key">
              <div v-if="Array.isArray(drawerNodeInfo[key])">
                <!-- 备份原有代码：可关闭的标签 -->
                <!--
                <el-tag class="drawer-tag"
                        v-for="tag in drawerNodeInfo[key]"
                        :key="key+'-'+tag"
                        closable
                        :disable-transitions="false"
                        @close="handleTagClose(key,tag)">
                  {{ tag }}
                </el-tag>
                -->
                <!-- 只读模式：不可关闭的标签 -->
                <el-tag class="drawer-tag"
                        v-for="tag in drawerNodeInfo[key]"
                        :key="key+'-'+tag"
                        :disable-transitions="false">
                  {{ tag }}
                </el-tag>
                <!-- BUG 输入框状态并不会切换 -->
                <!-- 备份原有代码：添加标签功能 -->
                <!--
                <div v-if="newTagInputVisible[key]">
                  <el-input
                      class="drawer-input-tag"
                      :ref="'input-'+key"
                      v-model="newTagInput"
                      size="small"
                      @keyup.enter.native="handleNewTagInputConfirm(key)"
                      @blur="handleNewTagInputConfirm(key)"
                  >
                  </el-input>
                </div>
                <div v-else>
                  <el-button class="button-new-tag"
                             size="small"
                             @click="handleNewTagClick(key)">+ 添加
                  </el-button>
                </div>
                -->
              </div>
              <el-input v-else
                        v-model="drawerNodeInfo[key]"
                        :disabled="true"
                        :type="drawerNodeInfo[key].toString().length > 20 && key !== 'uid' ? 'textarea': 'text'"
                        autosize>
              </el-input>
            </el-form-item>
          </el-form>
        </div>
        <!-- 备份原有代码：底部操作按钮 -->
        <!--
        <div class="viewer-drawer-footer">
          <el-button type="primary" icon="el-icon-edit" circle
                     @click="handleDrawerEdit"
                     :disabled="isCommitDisabled">
          </el-button>
          <el-button type="success" icon="el-icon-check" circle></el-button>
          <el-button type="info" icon="el-icon-message" circle></el-button>
          <el-button type="warning" icon="el-icon-star-off" circle></el-button>
          <el-button type="danger" icon="el-icon-delete" circle></el-button>
        </div>
        -->
      </a-drawer>
      <graph-chart :data="chartData" v-loading="isLoadingChart"></graph-chart>
    </div>
  </div>
</template>

<style lang="scss">
.graph {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
  grid-template-rows: auto 1fr;
}

.search-box {
  width: 27%;
  position: relative;
  left: 0;
  margin: 1%;
}

.aside-control-button {
  position: absolute;
  width: 25px;
  height: 80px;
  background-color: rgba(232, 232, 232, 0.53);
  left: 0;
  top: 45%;
  /*perspective：对元素进行透视操作*/
  /*rotateX：以y轴（横轴）进行旋转（前后仰俯）
  画个梯形出来*/
  transform: perspective(2em) rotateY(20deg);
  transition: left 0.3s;
  text-align: center;
  line-height: 80px;
  color: #909399;
  z-index: 2100; //higher than el-drawer
}

.aside-control-button:hover {
  background-color: #ecf5ff;
}

.graph-viewer {
  position: relative;
  width: 100%;
  height: 700px;
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.viewer-drawer-footer {
  background-color: white;
  background-size: 120% 120%;
  border-radius: 1px;
  position: sticky;
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
  bottom: 2%;
}

.drawer-input-tag{
  width: 90px;
  margin-left: 10px;
}
</style>