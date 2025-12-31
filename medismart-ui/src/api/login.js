import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/login',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return Promise.resolve({
    code: 200,
    message: "操作成功",
    data: {
      userId: 2,
      userName: "jackson",
      nickName: "jackson",
      phonenumber: "13456785432",
      email: "23233232@qq.com",
      roles: ["user"],
      permissions: ["system:user:upload"],
      sex: "1",
      createTime: "2024-04-21"
    }
  })
  // TODO: Implement this method
  // return request({
  //   url: '/getInfo',
  //   method: 'get'
  // })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}