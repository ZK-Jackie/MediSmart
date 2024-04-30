<script>
import Message from "@/views/room/components/message.vue";
import { getChatHistory } from "@/api/chat";

export default {
  props:{
    conversationId: {
      type: Number,
      default: -1
    },
    append:{
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  components: { Message },
  data() {
    return {
      // 展示的信息
      messages: [],
      // 是否显示使用说明
      showGuide: true,
      // 消息进入时是否要有动画
      animation: false,
      // 是否显示加载遮罩
      loadingContent: true
    }
  },
  watch:{
    append:{
      handler: function (){
        if(typeof this.append?.data?.content !== 'undefined'){
          this.messages.push(this.append);
          console.log(this.messages)
          this.animation = true;
        }
      },
      deep: true
    }
  },
  mounted(){
    getChatHistory(this.conversationId).then(res => {
      this.messages = res.data;
      this.messages.reverse();
      this.loadingContent = false;
      this.$emit('loading', false)
    })
  }
}
</script>

<template>
  <div class="content-box box" v-loading="loadingContent">
<!--    <div v-if="conversationId === -1">-->
<!--      项目LOGO-->
<!--    </div>-->
<!--    <div v-else-if="messages.length === 0 && showGuide">-->
<!--      使用说明 {{conversationId}}-->
<!--    </div>-->
    <Message v-for="(item, index) of messages"
             :key="'' + conversationId + index"
             :text="item.data.content"
             :inversion="item.type === 'ai'"
             :animation="animation"
    />
  </div>
</template>

<style scoped lang="scss">
.content-box {
  background-image: url('@/assets/images/background.jpg');
  background-attachment: fixed;
  background-repeat: no-repeat;
  background-size: cover;
  background-color: rgba(255, 255, 255, 0.2);

  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow-wrap: break-word;
  overflow-y: inherit;
  overflow-x: hidden;
  padding-bottom: 50px;
}

.box {
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  background-color: var(--blur-bg);
  height: 73%;
  width: 100%;
  border-radius: var(--border-radius-1);
  border: 1px solid var(--blur-border);
}
</style>