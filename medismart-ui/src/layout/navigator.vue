<template>
  <div class="navigator">
    <a class="nav-brand" @click="$router.push('/')" style="margin-left: 2vh">
      医智通 MediSmart
    </a>
    <div class="nav-menu">
      <el-menu mode="horizontal" class="menu" :default-active="activeIndex">
        <el-menu-item index="1" class="menu-item" @click="$router.push('/')">
          首页
        </el-menu-item>
        <el-menu-item index="2" class="menu-item" @click="$router.push('/chat')">医疗问询室
        </el-menu-item>
        <el-menu-item index="3" class="menu-item" @click="$router.push('/graph')">知识库
        </el-menu-item>
        <div class="buttons">
          <el-button type="primary"
                     round
                     @click="$router.push('/user')">用户中心
          </el-button>
        </div>
      </el-menu>
    </div>
  </div>
</template>

<script>

export default {
  name: 'navigator',
  data() {
    return {
      activeIndex: '1' // 默认激活的菜单项
    }
  },
  watch: {
    '$route'(to, from) {
      // 根据路由的变化来设置激活的菜单项
      if (to.path === '/') {
        this.activeIndex = '1';
      } else if (to.path === '/chat') {
        this.activeIndex = '2';
      } else if (to.path === '/graph') {
        this.activeIndex = '3';
      } else {
        this.activeIndex = '0';
      }
    },
  }
}
</script>

<style lang="scss" scoped>
.navigator {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  margin-bottom: 1%;
  border-bottom: 1px solid #e1e0e0;
}

.nav-brand {
  color: rgb(0, 153, 204);
  font-size: 1.5rem;
  white-space: nowrap;
}

.nav-menu {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  margin-right: 2vh;
}

.menu {
  display: flex;
  align-items: center;
  background-color: transparent;
}

.el-menu.el-menu--horizontal {
  border-bottom: none; // 设置el-menu下方不出现线
}

.menu-item, .buttons {
  margin-left: auto;
}

</style>