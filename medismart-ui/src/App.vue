<template>
  <div id="app">
    <navigator id="navigator"/>
    <transition name="fade-transform" mode="out-in">
      <router-view id="router-box"></router-view>
    </transition>
  </div>
</template>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0% 2%;
}

p {
  margin: 0;
}

#router-box {
  position: relative;
  top: calc(70px + 2vh);
  width: 100%;
  height: 100%;
}

#navigator {
  background-color: #FFF;
  position: fixed;
  top: 0;

  z-index: 1000;
  width: 96%;
  height: 70px;
  border-radius: 10px;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
}

.fade-transform-enter-active, .fade-transform-leave-active {
  transition: opacity .5s;
}
.fade-transform-enter, .fade-transform-leave-to {
  opacity: 0;
}
</style>

<script>
import navigator from "@/layout/navigator.vue";

export default {
  components: {
    navigator,
  },
};
</script>