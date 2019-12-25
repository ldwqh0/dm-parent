const webpackProdConf = require('./config/webpack.prod.conf')
const webpackDevConf = require('./config/webpack.dev.conf')
const config = {
  assetsDir: process.env.ASSETS_DIR,
  publicPath: process.env.CONTEXT_PATH,
  devServer: {
    port: 1024,
    proxy: {
      '/gw/': {
        target: 'http://127.0.0.1:8080',
        pathRewrite: { '^/gw/': '/' },
        onProxyRes (proxyRes, req, res) {
          if (proxyRes.statusCode === 302) {
            let redirect = proxyRes.headers.location
            if (redirect) {
              proxyRes.headers.location = redirect.replace(/http:\/\/127.0.0.1:8080\//g, 'http://localhost:1024/gw/')
            }
            if (redirect.startsWith('/')) {
              proxyRes.headers.location = process.env.CONTEXT_PATH + redirect.substring(1)
            }
          }
        }
      }
    }
    // proxy: 'http://localhost:4000'
  }
}
if (process.env.BABEL_ENV === 'production') {
  config.configureWebpack = webpackProdConf
} else {
  config.configureWebpack = webpackDevConf
}
module.exports = config