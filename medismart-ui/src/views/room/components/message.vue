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
    }
  },
  data() {
    return {
      showCursor: false,
      currentText: ''
    }
  },
  watch: {
    text() {
      this.showCursor = false;
      this.currentText = '';
      if (this.animation) {
        this.typingEffect();
      }
    }
  },
  methods: {
    typingEffect() {
      let index = 0;
      const intervalId = setInterval(() => {
        if (index < this.text.length) {
          this.currentText += this.text[index];
          index++;
        } else {
          clearInterval(intervalId);
          this.showCursor = false;
        }
      }, 40); // 每50毫秒添加一个字符
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
      this.showCursor = true;
      this.typingEffect();
    } else if (this.text !== '' && !this.animation) {
      // 如果当前是历史消息，就不需要动画效果
      this.showCursor = true;
      this.typingEffect();
      // this.currentText = this.text;
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
    <div class="content frame" v-html="toMarkdown(text)">
    </div>
    <div class="avatar">
      <img src="@/assets/images/user.png" alt="用户头像"/>
    </div>
  </div>
  <div :class="'other-message-box message-box' + (animation? ' slide-fade-in': '')"
       v-else>
    <div class="avatar">
      <img src="@/assets/images/bot.png" alt="AI头像"/>
    </div>
    <div class="content frame loading">
      <div v-html="toMarkdown(currentText)"></div>
      <div class="cursor" v-show="showCursor"></div>
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

  .content {
    position: relative;
    display: flex;
    flex-direction: column;
    gap: 18px;
    min-width: 0;
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
    /* position: absolute; */
    background-color: #9cc;
  }

  .loading {
    position: relative;
    display: flex;
    flex-direction: row-reverse;
    justify-content: flex-end;
  }

  .content {
    text-align: left;
    position: relative;
    display: flex;
    flex-direction: column;
    gap: 18px;
    min-width: 0;
    color: #000;

    p, li, code {
      margin: 0 !important;
      padding: 0 !important;
      position: relative;
      font-size: 15px;
      line-height: 1.3;
    }
  }
}

.cursor {
  position: absolute;
  right: 0;
  display: inline-block;
  vertical-align: text-bottom;
  width: 2px;
  height: 1rem;
  background-color: white;
  animation: blink 1s infinite;
}

p, li, code {
  margin: 0 !important;
  padding: 0 !important;
  position: relative;
  font-size: 15px;
  line-height: 1.3;
}
</style>