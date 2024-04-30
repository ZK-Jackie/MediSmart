<template>
  <div class="sider">
    <div class="aside-control-button"
         @click="drawVisible = !drawVisible"
         :style="{left: buttonLeft}"
    >
      <i :class="drawVisible? 'el-icon-caret-left' : 'el-icon-caret-right'"></i>
    </div>
    <a-drawer class="sider-drawer"
              ref="drawer"
              :visible="drawVisible"
              :placement="'left'"
              :width="300"
              :closable="false"
              :mask="false"
              :get-container="false"
              :drawerStyle="{ borderRadius: '10px'}"
              :wrap-style="{ position: 'absolute' }"
    >
      <div v-show="drawVisible" class="sider-drawer-content">
        <el-row>
          <el-col :span="24">
            <a-button type="dashed"
                      icon="plus"
                      class="drawer-new-button"
                      @click="handleNew"
                      block>
              新建对话
            </a-button>
          </el-col>
        </el-row>
        <el-row class="drawer-history-list">
          <history-card class="drawer-history-item"
                        v-for="(item, index) in reversedConversationInfo"
                        :key="item.conversationId"
                        :info="item"
                        :is-selected="item === selectedConversation"
                        @changeTitle="handleTitleChange"
                        @click="handleClick(item)"
                        @delete="handleDelete(item)"
          ></history-card>
        </el-row>
        <div class="drawer-bottom-button">

        </div>
      </div>
    </a-drawer>
  </div>
</template>

<script>
import {ls} from "@/utils/localstorage/localstorage.ts";
import HistoryCard from "@/views/room/components/historyCard.vue";
import {getChatHistory} from "@/api/chat";

function convBinarySearch(arr, target) {
  let left = 0;
  let right = arr.length - 1;
  while (left <= right) {
    const mid = Math.floor((left + right) / 2);
    if (arr[mid].conversationId === target) {
      return mid;
    }
    if (arr[mid].conversationId < target) {
      left = mid + 1;
    } else {
      right = mid - 1;
    }
  }
  return -1;
}

export default {
  name: "Sider",
  components: {HistoryCard},
  data() {
    return {
      // drawer status
      drawWidth: '0',
      drawVisible: true,
      // drawer conversation content
      conversationInfo: [
        // {
        //   conversationTitle: 'Test1',
        //   conversationId: 1,
        //   createTime: '2024-04-30',
        // },
      ],
      // drawer selected item
      selectedConversation: {
        // conversationTitle: 'Test1',
        // conversationId: 1,
        // createTime: '2024-04-30',
      },
    }
  },
  watch: {
    drawVisible: {
      handler: function () {
        this.$nextTick(() => {
          const drawerElement = document.getElementsByClassName("ant-drawer-wrapper-body")[0];
          this.drawWidth = drawerElement.getBoundingClientRect().width.toString();
          this.$emit('status', this.drawVisible);
        });
      },
      immediate: true
    }
  },
  computed: {
    buttonLeft() {
      return this.drawVisible ? this.drawWidth + "px" : '0';  // 你需要根据实际情况调整这个值
    },
    reversedConversationInfo() {
      // 使用 slice 创建原数组的副本，然后对副本进行反转
      return this.conversationInfo.slice().reverse();
    },
  },
  methods: {
    handleNew() {
      const convLength = this.conversationInfo.length;
      this.conversationInfo.push({
        conversationTitle: '新对话',
        conversationId: convLength + 1,
        createTime: new Date().toLocaleDateString(),
      });
      this.selectedConversation = this.conversationInfo[convLength];
      this.$emit('select', this.conversationInfo[convLength]);
      ls.set('conversationInfo', this.conversationInfo);
    },
    handleClick(item) {
      this.selectedConversation = item;
      this.$emit('select', item);
    },
    handleTitleChange(item, newTitle) {
      // 1. 校验item title是否改变
      if(item.conversationTitle === newTitle) {
        return;
      } else {
        if (newTitle === '') {
          item.conversationTitle = '新对话';
        } else {
          item.conversationTitle = newTitle;
        }
        this.$message.success('修改成功');
        ls.set('conversationInfo', this.conversationInfo);
      }
      // 2. 更新对话列表
      const index = convBinarySearch(this.conversationInfo, item.conversationId);
      if (index !== -1) {
        this.conversationInfo.splice(index, 1, item);
      }
    },
    handleDelete(item) {
      const index = convBinarySearch(this.conversationInfo, item.conversationId);
      if (index !== -1) {
        this.conversationInfo.splice(index, 1);
        this.$emit('delete', index);
        this.$message.success('删除成功');
        ls.set('conversationInfo', this.conversationInfo);
      }
    },
  },
  mounted() {
    const conversationInfo = ls.get('conversationInfo');
    if (conversationInfo) {
      // deep copy 获取历史对话列表
      this.conversationInfo = JSON.parse(JSON.stringify(conversationInfo));
    }
    getChatHistory('你好').then(res => {
      console.log(res);
    });
  },
}
</script>

<style lang="scss" scoped>
.aside-control-button {
  position: absolute;
  width: 20px;
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

.drawer-new-button {
  color: #8eb88e;
  border-color: #8eb88e;
}

.drawer-new-button:hover {
  color: #6e9166;
  border-color: #6e9166;
}

.ant-btn-dashed:focus {
  color: #8eb88e !important;
  border-color: #8eb88e !important;
}

.drawer-history-list {
  margin: 3vh 0;
  overflow: auto;
}

.drawer-history-item {
  margin-bottom: 3px;
}

</style>