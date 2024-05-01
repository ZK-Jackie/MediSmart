<script>
import {toEchartsData} from "@/utils/graph";
import {fuzzySearch} from "@/api/graph";
import searchBox from "@/views/graph/searchBox.vue";
import graphChart from "@/views/graph/graph-chart.vue";

export default {
  components: {
    searchBox, graphChart
  },
  watch: {
    drawVisible: {
      handler: function () {
        this.$nextTick(() => {
          const drawerElement = document.getElementsByClassName("el-drawer__body")[0];
          this.drawWidth = drawerElement.getBoundingClientRect().width.toString();
        });
      },
      immediate: true
    }
  },
  computed: {
    buttonLeft() {
      return this.drawVisible ? this.drawWidth+"px" : '0';  // 你需要根据实际情况调整这个值
    },
  },
  data() {
    return {
      drawVisible: false,
      drawWidth: '',
      nodeInfo: {},
      chartData: {},
      treeData: {},
      treeProps: {
        label: 'name',
        children: 'zones',
        isLeaf: 'leaf'
      },
      isLoadingChart: false
    }
  },
  mounted() {
    //TODO 添加一个组件  搜索框周围放事例查询关键词  让用户点击试查
  },
  methods: {
    search() {
      fuzzySearch("").then(res => {
        console.log(res);
        // TODO 模糊查询，搜索框边输入内容边查询，用户点击后拿id去查详细信息并展示
      });
    },
    handleSearch() {
      this.drawVisible = true;
      this.isLoadingChart = true;
      this.nodeInfo = this.$store.getters.list;
      this.$store.dispatch('map', this.nodeInfo.id).then(() => {
        console.log(this.nodeInfo.id + " map success");
        this.chartData = toEchartsData(this.$store.getters.list, this.$store.getters.map);
        this.isLoadingChart = false;
      });
    },
    loadTree(node, resolve) {
      // TODO 展示节点的property
      // node是用户当前展开点击的结点，有一个level属性代表树层
      // 1. level从0开始，最外层就是0
      if (node.level === 0) {
        const nowCenterNode = this.$store.getters.list;
        return resolve([{id: nowCenterNode.id, label: nowCenterNode.name, children: []}])
      } else if (node.level === 1) {
        if (this.$store.getters.list.category === '疾病') {
          let retArr = [];
          for (let key in this.$store.getters.list) {

          }
          return resolve([]);
        }
      }
      resolve([]);
    }
  }
}
</script>

<template>
  <div class="graph">
    <searchBox class="search-box" @alert="handleSearch"></searchBox>
    <div class="graph-viewer">
<!--      <div class="aside-control-button"-->
<!--           @click="drawVisible = !drawVisible"-->
<!--           :style="{left: buttonLeft}"-->
<!--      >-->
<!--        <i :class="drawVisible? 'el-icon-caret-left' : 'el-icon-caret-right'"></i>-->
<!--      </div>-->
      <el-drawer
          ref="drawer"
          :visible.sync="drawVisible"
          title="详细信息"
          direction="ltr"
          style="position: absolute"
          :modal="false"
          :lock-scroll="false"
          :show-close="false"
      >
        <el-tree :props="treeProps" :load="loadTree" lazy>
        </el-tree>
      </el-drawer>
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

.aside-control-button{
  position: absolute;
  width: 25px;
  height: 80px;
  background-color: rgba(232, 232, 232, 0.53);
  left: 0;
  top:45%;
  /*perspective：对元素进行透视操作*/
  /*rotateX：以y轴（横轴）进行旋转（前后仰俯）
  画个梯形出来*/
  transform:perspective(2em) rotateY(20deg);
  transition: left 0.3s;
  text-align: center;
  line-height: 80px;
  color: #909399;
  z-index: 2100;  //higher than el-drawer
}

.aside-control-button:hover{
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
</style>