const { defineConfig } = require('@vue/cli-service')
const path = require("path");
const port = process.env.port || process.env.npm_config_port || 8085 // 端口
const name = process.env.VUE_APP_TITLE || 'MediSmart' // 网页标题
function resolve(dir) {
  return path.join(__dirname, dir)
}

module.exports = defineConfig({
  //Vue CLI 服务会将依赖项的源代码转译为 ES5 代码，以确保代码在旧版浏览器中也能正常运行
  transpileDependencies: true,
  devServer: {
    host: '0.0.0.0',
    port: port,
    open: true,
    client: {
      webSocketURL: 'auto://0.0.0.0:0', // 关闭主机检查
      overlay: false, // 不要全屏报错
    },
    proxy: {
      //反向代理，前端解决跨域问题
      [process.env.VUE_APP_BASE_API]: {
        target: `http://localhost:8081`,
        changeOrigin: true,
        //重写路径
        pathRewrite: {
          ['^' + process.env.VUE_APP_BASE_API]: ''
        }
      }
    },
  },
  css: {
    loaderOptions: {
      sass: {
        sassOptions: { outputStyle: "expanded" }
      }
    }
  },
  configureWebpack: {
    name: name,
    module:{
      rules: [
        {
          test: /\.tsx?$/,
          use: 'ts-loader',
          exclude: /node_modules/,
        },
      ],
    },
    resolve: {
      extensions: ['.tsx', '.ts', '.js'],
      alias: {
        '@': resolve('src')
      }
    },
  },
})