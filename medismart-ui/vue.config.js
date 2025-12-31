const { defineConfig } = require('@vue/cli-service')
const CompressionPlugin = require('compression-webpack-plugin')
const TerserPlugin = require('terser-webpack-plugin')
const path = require("path");
const port = process.env.port || process.env.npm_config_port || 8085 // 端口
const name = process.env.VUE_APP_TITLE || 'MediSmart' // 网页标题
function resolve(dir) {
  return path.join(__dirname, dir)
}

module.exports = defineConfig({
  publicPath: './',
  //Vue CLI 服务会将依赖项的源代码转译为 ES5 代码，以确保代码在旧版浏览器中也能正常运行
  // transpileDependencies: true,
  // 生产环境关闭sourcemap以减小体积
  productionSourceMap: process.env.NODE_ENV !== 'production',
  devServer: {
    allowedHosts: "all",
    host: '0.0.0.0',
    port: port,
    open: true,
    // 添加 historyApiFallback 支持 SPA 路由
    historyApiFallback: {
      index: '/index.html',
      rewrites: [
        { from: /.*/, to: '/index.html' }
      ]
    },
    client: {
      webSocketURL: 'auto://0.0.0.0:0', // 关闭主机检查
      overlay: false, // 不要全屏报错
    },
    proxy: {
      //反向代理，前端解决开发时的跨域问题
      [process.env.VUE_APP_BASE_API]: {
        target: `http://localhost:8081`,
        changeOrigin: true,
        //重写路径
        pathRewrite: {
          ['^' + process.env.VUE_APP_BASE_API]: ''
        }
      }
    },
    // 添加对public/lib目录的静态文件支持
    static: {
      directory: path.join(__dirname, 'public'),
      publicPath: '/'
    }
  },
  css: {
    extract: process.env.NODE_ENV === 'production', // 生产环境提取CSS
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
    plugins: [
      // 添加 gzip 压缩
      new CompressionPlugin({
        test: /\.(js|css|html|svg|json)(\?.*)?$/i,
        threshold: 10240, // 超过 10kb 才压缩
        minRatio: 0.8, // 压缩比
        deleteOriginalAssets: false  // 保留原始文件
      })
    ],
    optimization: {
      minimize: true,
      minimizer: [
        new TerserPlugin({
          terserOptions: {
            compress: {
              drop_console: process.env.NODE_ENV === 'production', // 仅生产环境移除
              drop_debugger: process.env.NODE_ENV === 'production', // 仅生产环境移除
            },
            output: {
              comments: false // 移除注释
            },
            extractComments: false // 不提取注释文件
          }
        }),
      ],
      // 代码分割配置
      splitChunks: {
        chunks: 'async',
        minSize: 20000,
        maxSize: 244 * 1024, // 244KB
        cacheGroups: {
          vendors: {
            test: /[\\/]node_modules[\\/]/,
            priority: -10,
            reuseExistingChunk: true
          },
          default: {
            minChunks: 2,
            priority: -20,
            reuseExistingChunk: true
          }
        }
      }
    }
  },
  // 链式配置（高级）
  chainWebpack: config => {
    // 移除预加载的ElementUI资源
    config.plugins.delete('prefetch')
    config.plugins.delete('preload')

    // 设置外部依赖
    config.externals({
      'vue': 'Vue',
      'vuex': 'Vuex',
      'vue-router': 'VueRouter'
    })

    // 禁用对public/lib下文件的处理
    config.module
      .rule('js')
      .exclude
      .add(/public[\\/]lib[\\/].*\.js$/)

    // 生产环境配置
    if (process.env.NODE_ENV === 'production') {
      // 禁用预加载插件，防止流量偷跑
      // config.plugins.delete('preload')
      // config.plugins.delete('prefetch')

      // 可选：启用Bundle分析
      if (process.env.ANALYZER) {
        const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
        config.plugin('webpack-bundle-analyzer')
          .use(BundleAnalyzerPlugin)
      }
    }
  }
})