<script>
import {getCodeImg} from "@/api/login";

export default {
  props: {
    disfiex: {
      type: Number,
      default: 0,
      required: false
    }
  },
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.registerFormData.checkPassword !== '') {
          this.$refs.registerForm.validateField('checkPassword');
        }
        callback();
      }
    };
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.registerFormData.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      isButtonLoading: false,
      /** 登录/注册 样式切换 **/
      overlaylong: 'overlaylong',
      overlaytitle: 'overlaytitle',
      /** 验证码 **/
      codeUrl: '',
      /** 登录信息 **/
      loginFormData: {
        username: '',
        password: '',
        code: ''
      },
      loginFormRules: {
        username: [{
          required: true,
          message: '请输入用户名',
          trigger: 'blur'
        }],
        password: [{
          required: true,
          message: '请输入密码',
          trigger: 'blur'
        }],
        code: [{
          required: true,
          message: '请输入验证码',
          trigger: 'blur'
        }]
      },
      /** 注册信息 **/
      registerFormData: {
        username: '',
        password: '',
        checkPassword: '',
        code: ''
      },
      registerFormRules: {
        username: [{
          required: true,
          message: '请输入用户名',
          trigger: 'blur'
        }],
        password: [{
          validator: validatePass,
          trigger: 'blur'
        }],
        checkPassword: [{
          validator: validatePass2,
          trigger: 'blur'
        }],
        code: [{
          required: true,
          message: '请输入验证码',
          trigger: 'blur'
        }]
      }
    }
  },
  created() {
    this.getCode();
  },
  mounted() {
    // 登录/注册 切换
    if (this.disfiex === 1) {
      this.overlaylong = "overlaylongleft";
      this.overlaytitle = "overlaytitleright";
    } else {
      this.overlaylong = "overlaylongright";
      this.overlaytitle = "overlaytitleleft";
    }
    // 检查url中是否含重定向信息
    if (this.$route.query.redirect !== undefined) {
      this.$message({
        message: '请先登录',
        type: 'warning'
      });
    }
    // 提示测试用户信息
    this.$notify({
      title: '测试用户',
      message: '账号：jackson\n密码：123456',
      duration: 0
    });
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.codeUrl = "data:image/gif;base64," + res.data.img;
        this.loginFormData.uuid = res.data.uuid;
      });
    },
    Signin() {
      // return;
      this.overlaylong = "overlaylongleft"
      this.overlaytitle = "overlaytitleright"
      setTimeout(() => {
        this.disfiex = 1
      }, 200)
    },
    Signup() {
      this.overlaylong = "overlaylongright"
      this.overlaytitle = "overlaytitleleft"
      setTimeout(() => {
        this.disfiex = 0
      }, 200)
    },
    onLogin() {
      this.isButtonLoading = true;
      this.$refs["loginForm"].validate((valid) => {
        if (valid) {
          this.$store.dispatch("Login", this.loginFormData).then(() => {
            this.$router.push({path: this.redirect || "/"}).catch(() => {
            });
            this.$message({
              message: '登录成功',
              type: 'success'
            });
            this.isButtonLoading = false;
          }).catch(() => {
            this.getCode();
            this.isButtonLoading = false;
          });
        }
      });
    },
    onRegister() {
      this.$refs["registerForm"].validate((valid) => {
        if (valid) {
          // alert('submit!');
        } else {
          // console.log('error submit!!');
          return false;
        }
      });
    }
  }
}
</script>

<template>
  <div class="center">
    <h1>登录中心</h1>
    <div class="logon">
      <div :class="overlaylong">
        <div class="overlaylong-Signin" v-if="disfiex === 0">
          <h2 class="overlaylongH2">登录账户</h2>
          <el-form :model="loginFormData" :rules="loginFormRules" ref="loginForm">
            <el-form-item prop="username" key="loginFormData.username">
              <el-input v-model="loginFormData.username" placeholder="用户名" @keyup.enter.native="onLogin"></el-input>
            </el-form-item>
            <el-form-item prop="password" key="loginFormData.password">
              <el-input v-model="loginFormData.password" type="password" placeholder="密码" @keyup.enter.native="onLogin"></el-input>
            </el-form-item>
            <el-form-item class="captcha-box" prop="captcha" key="loginFormData.captcha">
              <el-input class="captcha-input" v-model="loginFormData.code" placeholder="验证码" @keyup.enter.native="onLogin"></el-input>
              <img class="captcha-pic" :src="codeUrl" alt="验证码" @click="getCode" v-loading="codeUrl === ''"/>
            </el-form-item>
          </el-form>
          <!--          <h3>忘记密码?</h3>-->
          <el-button class="inupbutton"
                     type="success" round
                     @click="onLogin()"
                     :loading="isButtonLoading">登录</el-button>
        </div>
        <div class="overlaylong-Signup" v-if="disfiex === 1">
          <h2 class="overlaylongH2">注册账户</h2>
          <el-form :model="registerFormData" :rules="registerFormRules" ref="registerForm">
            <el-form-item prop="username">
              <el-input v-model="registerFormData.username" placeholder="用户名"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="registerFormData.password" placeholder="密码"></el-input>
            </el-form-item>
            <el-form-item prop="checkPassword">
              <el-input v-model="registerFormData.checkPassword" placeholder="再次输入密码"></el-input>
            </el-form-item>
            <el-form-item class="captcha-box" prop="captcha">
              <el-input class="captcha-input" v-model="registerFormData.code" placeholder="验证码"></el-input>
              <img class="captcha-pic" :src="codeUrl" alt="验证码" @click="getCode"  v-loading="codeUrl === ''"/>
            </el-form-item>
          </el-form>
          <el-button class="inupbutton" @click="onRegister()" round>注册</el-button>
        </div>
      </div>
      <div :class="overlaytitle">
        <div class="overlaytitle-Signin" v-if="disfiex === 0">
          <h2 class="overlaytitleH2">希望能为您解惑</h2>
          <p class="overlaytitleP">
            请输入您的用户名和密码以登录您的账户。
            如果您尚未注册，请点击下方按钮创建新账户。
          </p>
          <div class="buttongohs" @click="Signin">注册账户</div>
        </div>
        <div class="overlaytitle-Signup" v-if="disfiex === 1">
          <h2 class="overlaytitleH2">希望我们能帮到您</h2>
          <p class="overlaytitleP">填写信息注册一个新账户，开始使用我们的医疗问答服务。我们将竭力为您解惑。</p>
          <div class="buttongohs" @click="Signup">登录账户</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.center {
  width: 1920px;
  height: 1080px;
  background-size: 100% 100%;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

h1 {
  font-size: 30px;
  color: black;
}

.logon {
  border-radius: 10px;
  box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
  /* position: relative;
  overflow: hidden; */
  width: 768px;
  height: 400px;
  max-width: 100%;
  min-height: 480px;
  margin-top: 20px;
  display: flex;
  background: linear-gradient(to right, #4284db, #29eac4);
}

.overlaylong {
  border-radius: 10px 0 0 10px;
  width: 50%;
  height: 100%;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlaylongleft {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: #fff;
  transform: translateX(100%);
  transition: transform 0.6s ease-in-out;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlaylongright {
  border-radius: 10px 0 0 10px;
  width: 50%;
  height: 100%;
  background-color: #fff;
  transform: translateX(0%);
  transition: transform 0.6s ease-in-out;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlaytitle {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
}


.overlaytitleH2 {
  font-size: 30px;
  color: #fff;
  margin-top: 20px;
}

.overlaytitleP {
  font-size: 15px;
  color: #fff;
  margin-top: 20px;
}

.overlaytitleleft {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translateX(0%);
  transition: transform 0.6s ease-in-out;
}

.overlaytitleright {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translateX(-100%);
  transition: transform 0.6s ease-in-out;
}

.overlaytitle-Signin {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.overlaytitle-Signup {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.buttongohs {
  width: 180px;
  height: 40px;
  border-radius: 50px;
  border: 1px solid #fff;
  color: #fff;
  font-size: 15px;
  text-align: center;
  line-height: 40px;
  margin-top: 40px;
}

.overlaylongH2 {
  font-size: 25px;
  color: black;
  /* width: 250px; */
}

.overlaylong-Signin {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.overlaylong-Signup {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.el-input > input {
  width: 240px;
}

.el-input__inner:focus {
  border-color: rgb(53, 184, 207) !important;
}

.captcha-box > .el-form-item__content {
  display: flex;
  align-items: center;
  justify-content: center;
}

.captcha-pic {
  width: 80px;
  border: 1px solid #000;
}

h3 {
  font-size: 10px;
  margin-top: 10px;
  cursor: pointer;
}

.inupbutton {
  background-color: #29eac4;
  width: 180px;
}
.inupbutton:hover {
  background-color: #73e6ce !important;
}
</style>