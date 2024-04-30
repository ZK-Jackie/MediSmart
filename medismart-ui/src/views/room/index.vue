<script>
import Content from "@/views/room/content.vue";
import MsgInput from "@/views/room/msgInput.vue";
import Sider from "@/views/room/sider.vue";

import {chat} from "@/api/chat";

function buildMessage(type, content) {
  return {
    data: {
      type: type,
      content: content,
    },
    type: type,
  }
}

export default {
  components: {
    MsgInput,
    Sider,
    Content,
  },
  data() {
    return {
      // user devise
      isMobile: false,
      // drawer width
      mainWidth: '0',
      // conversation data
      conversationId: -1,
      sendMessage: {},
      // is ai generating message
      isLoading: false,
    }
  },
  methods: {
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
      this.conversationId = item.conversationId;
    },
    handleSend(message) {
      this.sendMessage = buildMessage('user', message);
      this.clearMessage();
      chat(this.conversationId, message).then(res => {
        this.sendMessage = buildMessage('ai', res.data.output);
        this.clearMessage();
      });
    },
    clearMessage() {
      setTimeout(() => {
        this.sendMessage = {};
      }, 500);
    },
    handleDownload() {

    },
  }
}


</script>

<template>
  <div class="room-box">
    <Sider class="room-box-sider"
           @status="handleSider"
           @select="handleSelect"
    />
    <div class="room-box-main"
         :style="{paddingLeft: mainWidth}"
    >
      <Content style="height: 70vh; margin-bottom: 1vh"
               :key="conversationId"
               :conversationId="conversationId"
               :append="sendMessage"
      />
      <msg-input style="height: 8vh"
                 @send="handleSend"
                 @download="handleDownload"
                 :disabled="conversationId === -1"
      />
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
</style>
