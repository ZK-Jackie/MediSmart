<script>
import MarkdownIt from "markdown-it";

export default {
  props: {
    dateTime: {
      type: String,
      default: '',
    },
    text: {
      type: String,
      default: ''
    },
    inversion: {
      type: Boolean,
      default: false
    },
    error: {
      type: Boolean,
      default: false
    },
    animation: {
      type: Boolean,
      default: false
    },
    waiting: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      showCursor: false,
      currentText: ''
    }
  },
  watch: {
    text: {
      handler(newVal) {
        this.showCursor = false;
        this.currentText = newVal; // 直接更新为新值，而不是重置后再动画
        if (this.animation) {
          // 这里可以选择是否重新启动动画
          // this.typingEffect();
        }
      },
      deep: true // 添加深度监听
    }
  },
  methods: {
    typingEffect() {
      let index = 0;
      const typeNextCharacter = () => {
        if (index < this.text.length) {
          this.currentText += this.text[index];
          index++;
          const lag = 40 + Math.floor(Math.random() * 100);
          setTimeout(typeNextCharacter, lag);
        } else {
          this.showCursor = false;
        }
      };
      typeNextCharacter();
    },
    toMarkdown(text) {
      const md = new MarkdownIt();
      return md.render(text);
    }
  },
  mounted() {
    this.currentText = '';
    if (this.text !== '' && this.animation) {
      // 如果当前是正在聊天的状态，就会有文字，就需要动画效果
      // this.typingEffect();
    } else if (this.text !== '' && !this.animation) {
      // 如果当前是历史消息，就不需要动画效果
      // this.typingEffect();
      this.currentText = this.text;
    }
  },
  updated() {
    // this.showCursor = true;
  }
}
</script>

<template>
  <div :class="'my-message-box message-box' + (animation? ' slide-fade-in': '')"
       v-if="!inversion">
    <div class="message-content frame" v-html="toMarkdown(text)"></div>
    <div class="avatar">
      <img src="@/assets/images/user.png" alt="用户头像"/>
    </div>
  </div>
  <div :class="'other-message-box message-box' + (animation? ' slide-fade-in': '')"
       v-else>
    <div class="avatar">
      <img src="@/assets/images/bot.png" alt="AI头像"/>
    </div>
    <div class="message-content frame loading">
      <div class="content-markdown"
           v-html="toMarkdown(currentText)"
           :style="showCursor? 'margin-right: 1rem': ''"
      ></div>
      <div class="cursor" v-show="showCursor||waiting">
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
@keyframes blink {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}

@keyframes slide-fade-in {
  0% {
    transform: translateY(20px);
    opacity: 0;
  }
  100% {
    transform: translateY(0);
    opacity: 1;
  }
}

.message-box {
  box-sizing: border-box;
  width: 100%;
  overflow-wrap: break-word;
  position: relative;
  display: flex;
  gap: var(--section-gap);
  padding: var(--section-gap);
  padding-bottom: 0;
  color: #000;
}

.slide-fade-in {
  animation: slide-fade-in 0.5s ease-out;
}

.my-message-box {
  display: inline-flex !important;
  flex-wrap: nowrap;
  justify-content: flex-end;

  .avatar {
    max-width: 48px;
    max-height: 48px;
    flex-shrink: 0;
    width: 80px;
    position: relative;
    display: block;

    img {
      position: relative;
      width: 100%;
      height: 100%;
      object-fit: cover;
      border-radius: 8px;
      outline: 1px solid var(--blur-border);
    }
  }

  .message-content {
    position: relative;
    display: flex;
    flex-direction: column;
    gap: 18px;
    min-width: 2rem;
    color: #000;
  }

  .frame {
    width: auto;
    right: 0;
    text-align: left;
    position: relative;
    border-radius: 10px;
    border: 1px solid #000;
    padding: 10px;
    margin-left: 3px;
    max-width: 1000px;
    background-color: #fff;
  }
}

.other-message-box {
  .avatar {
    position: relative;
    max-width: 48px;
    max-height: 48px;
    flex-shrink: 0;
    width: 80px;

    img {
      position: relative;
      width: 100%;
      height: 100%;
      object-fit: cover;
      border-radius: 8px;
      outline: 1px solid var(--blur-border);
    }
  }

  .frame {
    position: relative;
    border-radius: 10px;
    border: 1px solid #000;
    padding: 10px;
    min-width: 2rem;
    /* position: absolute; */
    background-color: #9cc;
  }

  .loading {
    position: relative;
    display: flex;
    flex-direction: row-reverse;
    justify-content: flex-end;
  }

  .message-content {
    text-align: left;
    align-items: center;
    position: relative;
    display: flex;
    flex-direction: column;
    gap: 18px;
    min-width: 2rem;
    color: #000;
  }
}

.text-and-cursor {
  display: flex;
  align-items: center;
}

.cursor {
  color: transparent;
  margin: 0 1rem;
  position: absolute;
  right: 0;
  display: inline-block;
  vertical-align: text-bottom;
  width: 0.5rem;
  height: 1rem;
  background-color: white;
  animation: blink 0.8s infinite;
}
</style>