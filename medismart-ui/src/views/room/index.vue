<script>
import Content from "@/views/room/content.vue";
import Sider from "@/views/room/sider.vue";

import {chat} from "@/api/chat";

function buildMessage(type, content) {
  return {
    data: {
      type: type,
      content: content.trim(),
    },
    type: type,
  }
}

export default {
  components: {
    Sider,
    Content,
  },
  data() {
    return {
      // user devise
      isMobile: false,
      // drawer
      mainWidth: '0',
      isSiderOpen: true,
      // conversation data
      conversationId: -1,
      // input box
      inputText: '',
      sendMessage: {},
      loadingContent: true,
    }
  },
  computed: {
    inputDisabled() {
      return this.conversationId === -1 || this.loadingContent;
    }
  },
  watch: {
    loadingContent() {
      this.contentToBottom();
      document.getElementsByClassName('input-text-area')[0].focus();
    }
  },
  methods: {
    contentToBottom() {
      this.$nextTick(() => {
        let contentBoxDOM = document.getElementsByClassName('main-content-box')[0];
        contentBoxDOM.scrollTop = contentBoxDOM.scrollHeight;
      });
    },
    handleSider(isSiderOpen) {
      if (isSiderOpen) {
        let drawerDOM = document.getElementsByClassName('ant-drawer-content-wrapper')[0];
        let drawerWidth = drawerDOM.getBoundingClientRect().width + 18;
        this.mainWidth = drawerWidth + 'px';
      } else {
        this.mainWidth = '0';
      }
    },
    handleSelect(item) {
      // 移动端直接关闭
      if (this.isMobile) {
        this.isSiderOpen = false;
      }
      // 1. 检查是否重复点击
      if (this.conversationId === item.conversationId) {
        return;
      }
      // 2. 切换会话
      this.conversationId = item.conversationId;
      this.loadingContent = true;
    },
    handleEnter(event) {
      if(this.loadingContent){
        this.$message.warning('请等待AI回复');
        return;
      }
      if(this.inputDisabled && this.conversationId === -1){
        this.$message.warning('请先选择一个会话');
        return;
      }else if(this.inputDisabled){
        this.$message.warning('请等待AI回复');
        return;
      }
      if (event.ctrlKey && event.keyCode === 13) {
        event.preventDefault()
        this.inputText += "\n";
        this.$nextTick(() => {
          event.target.scrollTop = event.target.scrollHeight;
          this.contentToBottom();
        });
      } else if (event.keyCode === 13) {
        event.preventDefault()
        this.handleSend();
      }
    },
    handleSend() {
      if (this.inputText === '') {
        return;
      }
      this.loadingContent = true;
      this.sendMessage = buildMessage('user', this.inputText);
      this.contentToBottom();
      this.clearMessage();
      chat(this.conversationId, this.inputText).then(res => {
        this.sendMessage = buildMessage('ai', res.data.output);
        this.clearMessage();
        this.loadingContent = false;
        this.contentToBottom();
      });
    },
    clearMessage() {
      setTimeout(() => {
        this.sendMessage = {};
        this.inputText = '';
      }, 500);
    },
    handleDownload() {

    },
    checkWindowWidth() {
      this.isMobile = window.innerWidth < 650; // 你可以根据需要调整这个值
      if (this.isMobile) {
        this.isSiderOpen = false;
      }
    },
  },
  mounted() {
    this.checkWindowWidth();
    window.addEventListener('resize', this.checkWindowWidth);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.checkWindowWidth);
  },
}
</script>

<template>
  <div class="room-box">
    <Sider class="room-box-sider"
           :isMobile="isMobile"
           :isOpen="isSiderOpen"
           @status="handleSider"
           @select="handleSelect"
    />
    <div class="room-box-main"
         :style="{paddingLeft: isMobile? '0' : mainWidth}"
    >
      <Content style="height: 70vh; margin-bottom: 1vh"
               class="main-content-box"
               :key="conversationId"
               :conversationId="conversationId"
               :append="sendMessage"
               :waiting="inputDisabled && conversationId !== -1"
               @loading="(isLoading)=>{loadingContent = isLoading}"
      ></Content>
      <div :class="'input-box ' + (inputDisabled? 'disable-box': '')"
           style="height: 8vh"
      >
        <transition name="el-fade-in-linear">
          <el-button icon="el-icon-s-fold"
                     class="el-button-list"
                     key="el-button-fold"
                     @click="isSiderOpen = !isSiderOpen"
                     v-show="isMobile"
          >
          </el-button>
        </transition>
        <el-input
            class="input-text-area"
            type="textarea"
            :autosize="{ minRows: 1, maxRows: 2}"
            placeholder="输入医疗问题"
            v-model="inputText"
            resize="none"
            @keydown.enter.native="handleEnter"
        >
        </el-input>
        <div class="input-button-list">
          <el-tooltip content="下载聊天记录"
                      placement="top"
                      effect="light"
                      key="el-tooltip-download-history"
          >
            <el-button icon="el-icon-download"
                       key="el-button-download-history"
                       @click="$emit('download', true)"
                       :disabled="inputDisabled"
            ></el-button>
          </el-tooltip>
          <el-tooltip placement="top"
                      effect="light"
                      content="发送"
                      key="el-tooltip-send-msg"
          >
            <el-button type="success"
                       icon="el-icon-s-promotion"
                       key="el-button-send-msg"
                       class="send-button"
                       @click="handleSend"
                       :disabled="inputDisabled"
            ></el-button>
          </el-tooltip>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.room-box {
  border-radius: 5px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  position: relative;
  box-sizing: border-box;
  background: #FFF;
  width: 100vw;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.room-box-main {
  right: 0;
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  flex-grow: 1;
  transition: padding-left 0.3s ease;
}

.room-box-sider {
  position: absolute;
  z-index: 1;
  height: 100%;
  flex-grow: 1;
  box-sizing: border-box;
  bottom: 0;
}

.disable-box {
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #fff;
  opacity: 0.5;
}

.disable-box:hover {
  cursor: not-allowed;
}

.el-button-list {
  margin-left: 6px;
  transition: padding-left 0.3s ease;
}

.input-box {
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  background-color: var(--blur-bg);
  height: 73%;
  width: 100%;
  border-radius: var(--border-radius-1);
  border: 1px solid var(--blur-border);

  display: flex;
  align-items: center;
}

.input-text-area {
  padding: 0 10px;
  background-color: transparent !important;
}

.input-text-area:focus {
  border-color: transparent !important; // 设置激活状态下的边框颜色为透明
}

.input-text-area::-webkit-scrollbar-track {
  background-color: #f1f1f1 !important;
}

.send-button {
  background-color: #29eac4;
}

.send-button:hover {
  background-color: #73e6ce !important;
}

.input-button-list {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-right: 10px;
}
</style>
