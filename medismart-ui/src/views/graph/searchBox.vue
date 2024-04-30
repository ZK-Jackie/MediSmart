<template>
  <div class="search-wrapper" :class="{ active: isActive }">
    <div class="input-holder">
      <input type="text" class="search-input" placeholder="请输入想知道的内容" v-model="searchInfo.keyword" />
      <button class="search-icon" @click="toggleSearch"><span></span></button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isActive: false,
      searchInfo: {
        keyword: '',
        pageNow: 1,
        pageSize: 5,
        pageTotal: 0,
      },
    }
  },
  methods: {
    toggleSearch() {
      // TODO  加载搜索过程动画
      if(!this.isActive) {
        this.isActive = true;
        this.searchInfo.keyword = '';
      } else {
        this.$store.dispatch('listSearch', this.searchInfo).then(res => {
          console.log(this.searchInfo.keyword + " list search success");
          this.$emit('alert', true);
        });
        this.isActive = false;
      }
    },
  }
}
</script>


<style scoped>
.search-wrapper .input-holder {
  height: 40px;
  width:40px;
  overflow: hidden;
  background: rgba(255,255,255,0); /* 修改这里 */
  border-radius:3px;
  position: relative;
  transition: all 0.3s ease-in-out;
}
.search-wrapper.active .input-holder {
  width:450px;
  height:45px;
  border-radius: 50px;
  transition: all .5s cubic-bezier(0.000, 0.105, 0.035, 1.570);
  border: 2px solid blue; /* 添加这一行，设置边框颜色为蓝色 */
}
.search-wrapper .input-holder .search-input {
  width:100%;
  height: 38px;
  padding:0px 70px 0 20px;
  opacity: 0;
  position: absolute;
  top:0px;
  left:0px;
  background: transparent;
  box-sizing: border-box;
  border:none;
  outline:none;
  font-size: 16px;
  font-weight: 400;
  line-height: 20px;
  color: #886767;
  transform: translate(0, 60px);
  transition: all .3s cubic-bezier(0.000, 0.105, 0.035, 1.570);
  transition-delay: 0.3s;
  display: flex; /* 添加这一行 */
  align-items: center; /* 添加这一行 */
}
.search-wrapper.active .input-holder .search-input {
  opacity: 1;
  transform: translate(0, 10px);
}
.search-wrapper .input-holder .search-icon {
  width:35px;
  height:35px;
  border:none;
  border-radius:6px;
  margin: 8px;
  background: transparent;
  padding:0px;
  outline:none;
  position: relative;
  z-index: 2;
  float:right;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
  filter: hue-rotate(200deg); /* 添加这一行 */
}
.search-wrapper.active .input-holder .search-icon {
  width: 35px;
  height:35px;
  margin: 8px;
  border-radius: 30px;
}
.search-wrapper .input-holder .search-icon span {
  width:30px;
  height:30px;
  display: inline-block;
  vertical-align: middle;
  position:relative;
  transform: rotate(45deg);
  transition: all .4s cubic-bezier(0.650, -0.600, 0.240, 1.650);
}
.search-wrapper.active .input-holder .search-icon span {
  transform: rotate(-45deg);
}
.search-wrapper .input-holder .search-icon span::before,
.search-wrapper .input-holder .search-icon span::after {
  position: absolute;
  content:'';
}
.search-wrapper .input-holder .search-icon span::before {
  width: 5px;
  height: 11px;
  left: 9px;
  top: 18px;
  border-radius: 2px;
  background: #0c0b0b;
}
.search-wrapper .input-holder .search-icon span::after {
  width: 14px;
  height: 14px;
  left: 0px;
  top: 0px;
  border-radius: 16px;
  border: 4px solid #FE5F55;
}
.search-input::-webkit-input-placeholder {
  font-weight: bold;
}

/* Firefox */
.search-input::-moz-placeholder {
  font-weight: bold;
}

/* IE */
.search-input:-ms-input-placeholder {
  font-weight: bold;
}

</style>