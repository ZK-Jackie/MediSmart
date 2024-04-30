<script>
export default {
  name: "msgInput",
  props: {
    disabled: {
      type: Boolean,
      default: false
    },
    isMobile: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      inputText: '',
    }
  },
  methods:{
    handleSend(){
      if (this.inputText === ''){
        return;
      }
      this.$emit('send', this.inputText);
      this.inputText = '';
    },
  }
}
</script>

<template>
  <div :class="'input-box ' + (disabled? 'disable-box': '')">
    <el-button icon="el-icon-s-fold" key="el-button-fold">
    </el-button>
    <el-input
        class="input-text-area"
        type="textarea"
        :autosize="{ minRows: 1, maxRows: 2}"
        placeholder="输入医疗问题"
        v-model="inputText"
        resize="none"
        @keydown.enter.native="handleSend"
        :disabled="disabled"
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
                   :disabled="disabled"
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
                   :disabled="disabled"
        ></el-button>
      </el-tooltip>
    </div>
  </div>
</template>


<style lang="scss">
.disable-box{
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #fff;
  opacity: 0.5;
}
.disable-box:hover{
  cursor: not-allowed;
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

.input-text-area{
  padding: 0 10px;
  background-color: transparent !important;
}
.input-text-area:focus {
  border-color: transparent !important;  // 设置激活状态下的边框颜色为透明
}
.input-text-area::-webkit-scrollbar-track {
  background-color: #f1f1f1 !important;
}

.send-button{
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