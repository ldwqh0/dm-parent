const { merge } = require('webpack-merge')
const common = require('./webpack.config.common')
const proxy = require('./proxy_localhost')
module.exports = env => {
  return merge(common(env), {
    mode: 'development',
    module: {
      rules: [{
        test: /\.css$/,
        use: [
          'style-loader',
          'css-loader'
        ]
      }, {
        test: /\.less$/,
        use: [
          'style-loader',
          'css-loader',
          'less-loader'
        ]
      }]
    },
    devServer: {
      publicPath: env.CONTEXT_PATH,
      contentBase: `.${env.CONTEXT_PATH}`,
      overlay: {
        warnings: false,
        errors: true
      },
      // 将这个改到一个不存在的地方，才能触发后台校验
      index: 'cc.html',
      host: '0.0.0.0',
      port: 80,
      proxy: proxy(env),
      historyApiFallback: {
        rewrites: [{
          from: new RegExp(`^${env.CONTEXT_PATH}`),
          to: env.CONTEXT_PATH
        }]
      }
    }
  })
}
