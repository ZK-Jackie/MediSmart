<template>
  <div class="sider">
    <div class="aside-control-button"
         :style="{left: buttonLeft}"
         @click="drawVisible = !drawVisible"
         v-show="!isMobile"
    >
      <i :class="drawVisible? 'el-icon-caret-left' : 'el-icon-caret-right'"></i>
    </div>
    <a-drawer class="sider-drawer"
              ref="drawer"
              :visible="drawVisible"
              :placement="'left'"
              :width="300"
              :closable="false"
              :mask="isMobile"
              :mask-closable="isMobile"
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
          <!-- TODO 抽屉底部内容 -->
        </div>
      </div>
    </a-drawer>
  </div>
</template>

<script>
import HistoryCard from "@/views/room/components/historyCard.vue";
import {getConversationList, deleteConversation, updateConversationTitle} from "@/api/chat";

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
  props:{
    isMobile: {
      type: Boolean,
      default: false
    },
    isOpen: {
      type: Boolean,
      default: true
    }
  },
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
      // 防止重复操作的状态
      operatingConversations: new Set(), // 正在操作的会话ID集合
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
    },
    isMobile: {
      handler: function () {
        this.drawVisible = !this.isMobile;
      },
      immediate: true
    },
    isOpen:{
      handler: function () {
        this.drawVisible = this.isOpen;
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
    async fetchConversationList() {
      try {
        const response = await getConversationList();
        if (response.code === 200) {
          this.conversationInfo = response.data.map(item => ({
            conversationTitle: item.conversationTitle,
            conversationId: item.conversationId,
            createTime: new Date(item.createTime).toLocaleDateString(),
          }));
        }
      } catch (error) {
        console.error('获取会话列表失败:', error);
        this.$message.error('获取会话列表失败');
      }
    },
    handleNew() {
      // 创建一个临时的新会话对象，不带conversationId
      const newConversation = {
        conversationTitle: '新对话',
        conversationId: null, // 新会话没有ID
        createTime: new Date().toLocaleDateString(),
        isNew: true, // 标记为新会话
      };
      // this.conversationInfo.push(newConversation);
      this.selectedConversation = newConversation;
      this.$emit('select', newConversation);
    },
    handleClick(item) {
      this.selectedConversation = item;
      this.$emit('select', item);
    },
    async handleTitleChange(item, newTitle) {
      // 防止重复操作
      if (this.operatingConversations.has(item.conversationId)) {
        this.$message.warning('操作进行中，请稍候');
        return;
      }
      
      // 1. 校验item title是否改变
      if(item.conversationTitle === newTitle) {
        return;
      }
      
      // 2. 校验新标题
      if (newTitle === '') {
        newTitle = '新对话';
      }
      
      // 添加到操作中集合
      this.operatingConversations.add(item.conversationId);
      
      try {
        // 3. 调用后端API更新标题
        await updateConversationTitle(item.conversationId, newTitle);
        
        // 4. 更新本地数据
        item.conversationTitle = newTitle;
        const index = convBinarySearch(this.conversationInfo, item.conversationId);
        if (index !== -1) {
          this.conversationInfo.splice(index, 1, item);
        }
        
        this.$message.success('修改成功');
      } catch (error) {
        console.error('修改标题失败:', error);
        this.$message.error('修改标题失败');
      } finally {
        // 从操作中集合移除
        this.operatingConversations.delete(item.conversationId);
      }
    },
    async handleDelete(item) {
      // 防止重复操作
      if (this.operatingConversations.has(item.conversationId)) {
        this.$message.warning('操作进行中，请稍候');
        return;
      }
      
      // 添加到操作中集合
      this.operatingConversations.add(item.conversationId);
      
      try {
        // 1. 调用后端API删除会话
        await deleteConversation(item.conversationId);
        
        // 2. 从列表中移除
        const index = convBinarySearch(this.conversationInfo, item.conversationId);
        if (index !== -1) {
          this.conversationInfo.splice(index, 1);
          this.$emit('delete', index);
        }
        
        this.$message.success('删除成功');
      } catch (error) {
        console.error('删除会话失败:', error);
        this.$message.error('删除会话失败');
      } finally {
        // 从操作中集合移除
        this.operatingConversations.delete(item.conversationId);
      }
    },
    updateConversationInfo(conversationId, conversationTitle) {
      // 更新或添加会话到列表
      const existingIndex = this.conversationInfo.findIndex(
        item => item.conversationId === conversationId
      );
      
      if (existingIndex !== -1) {
        // 更新现有会话
        this.conversationInfo[existingIndex].conversationTitle = conversationTitle || '新对话';
      } else {
        // 添加新会话
        this.conversationInfo.push({
          conversationTitle: conversationTitle || '新对话',
          conversationId: conversationId,
          createTime: new Date().toLocaleDateString(),
        });
      }
      
      // 更新选中的会话
      this.selectedConversation = this.conversationInfo.find(
        item => item.conversationId === conversationId
      );
    },
  },
  mounted() {
    // 从后端加载会话列表
    this.fetchConversationList();
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