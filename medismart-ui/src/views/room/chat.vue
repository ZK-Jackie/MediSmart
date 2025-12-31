<template>
  <div>
    <transition name="el-fade-in-linear">
      <iframe :src="'http://47.236.98.162:5500/html/index.html?token=' + token"
              title="chatRoom"
              class="frame">
      </iframe>
    </transition>
  </div>
</template>

<script>
import {getToken} from "@/utils/auth";

export default {
  name: 'ChatRoom',
  data() {
    return {
      token: ''
    }
  },
  mounted() {
    this.token = getToken();
  }
}
</script>

<style scoped>
.frame {
  border: none; /* 消除边框 */
  width: 100%; /* 宽度设置为100% */
  height: 800px; /* 高度设置为100% */
}

</style>