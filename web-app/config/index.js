'use strict'
// Template version: 1.3.1
// see http://vuejs-templates.github.io/webpack for documentation.

const path = require('path')

// 项目的发布路径
const CONTEXT_PATH = '/dc/'

module.exports = {
  dev: {
    mode: 'development',
    useSso: false, // 是否启用oauth单点登录
    // Paths
    assetsSubDirectory: 'static', // 静态资源的路径
    assetsPublicPath: CONTEXT_PATH, // 项目发布路径

    // 如果使用本地后台服务，请打开下列配置
    proxyTable: {

      //权限相关连接，包括登录，菜单授权控制
      [`${CONTEXT_PATH}p/`]: {
        changeOrigin: true,
        target: 'http://localhost:8080',
        pathRewrite: { [`${CONTEXT_PATH}p/`]: '' }
      },

      // 用户和角色配置
      [`${CONTEXT_PATH}u/`]: {
        changeOrigin: true,
        target: 'http://localhost:8080',
        pathRewrite: { [`${CONTEXT_PATH}u/`]: '' }
      }

    }, // devServer反向代理列表

    // Various Dev Server settings
    host: 'localhost', // can be overwritten by process.env.HOST
    port: 80, // 服务端口
    autoOpenBrowser: false, // 编译完成后是否自动打开浏览器
    errorOverlay: true, // 在devServer中，是否启用错误输出层
    notifyOnErrors: false, // 是否在系统通知区域提示错误
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-

    // 是否使用 Eslint Loader 在编译过程中检查书写错误?
    // 格式错误会在控制台显示出来
    useEslint: true, //
    // 是否在devServer中用一个单独的层显示格式错误
    showEslintErrorsInOverlay: true,

    /**
     * Source Maps
     */

    // https://webpack.js.org/configuration/devtool/#development
    devtool: 'source-map',

    // If you have problems debugging vue-files in devtools,
    // set this to false - it *may* help
    // https://vue-loader.vuejs.org/en/options.html#cachebusting
    cacheBusting: true,

    cssSourceMap: true
  },
  build: {
    mode: 'production',
    useSso: true, // 是否启用oauth单点登录
    // Template for index.html
    index: path.resolve(__dirname, '../dist/index.html'),

    // Paths
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static', // 静态资源的路径
    assetsPublicPath: CONTEXT_PATH, // 项目的的发布路径，必须以'/'结尾,建议使用 '/CONTEXT_PATH'的模式

    /**
     * Source Maps
     */

    productionSourceMap: false,
    // https://webpack.js.org/configuration/devtool/#production
    devtool: '#source-map',

    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false, // 是否启用Gzip压缩，如果使用nginx发布，必须选择false
    productionGzipExtensions: ['js', 'css'],

    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
