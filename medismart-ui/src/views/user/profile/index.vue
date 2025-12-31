<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar/>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <div>
                  <!--                  <svg-icon icon-class="user"/>-->
                  用户组
                </div>
                <div class="pull-right">{{ roleGroup }}</div>
              </li>
              <li class="list-group-item">
                <div>
<!--                  <svg-icon icon-class="user"/>-->
                  用户名称
                </div>
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <div>
<!--                  <svg-icon icon-class="user"/>-->
                  用户名称
                </div>
                <div class="pull-right">{{ user.nickName }}</div>
              </li>
              <li class="list-group-item">
                <div>
<!--                  <svg-icon icon-class="phone"/>-->
                  手机号码
                </div>
                <div class="pull-right">{{ user.phonenumber }}</div>
              </li>
              <li class="list-group-item">
                <div>
<!--                  <svg-icon icon-class="email"/>-->
                  用户邮箱
                </div>
                <div class="pull-right">{{ user.email }}</div>
              </li>
              <li class="list-group-item">
                <div>
<!--                  <svg-icon icon-class="date"/>-->
                  创建日期
                </div>
                <div class="pull-right">{{ user.createTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user"/>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd/>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import {getUserProfile} from "@/api/user";

export default {
  name: "Profile",
  components: {userAvatar, userInfo, resetPwd},
  data() {
    return {
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo"
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        // console.log(response);
        this.user = response.data.user;
        this.roleGroup = response.data.roleGroup;
        this.postGroup = response.data.postGroup;
      });
    }
  }
};
</script>

<style scoped lang="scss">
.app-container {
  padding: 20px;
}

.el-card__header {
  padding: 14px 15px 7px;
  min-height: 40px;
}

.el-card__body {
  padding: 15px 20px 20px 20px;
}

.text-center {
  text-align: center
}

.list-group {
  padding-left: 0px;
  list-style: none;
}

.list-group-item {
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0px;
  font-size: 13px;
}

.list-group-striped > .list-group-item {
  border-left: 0;
  border-right: 0;
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
}

.pull-right {
  float: right;
}
</style>