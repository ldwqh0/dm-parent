const { merge } = require('webpack-merge')
const common = require('./webpack.config.common')
const proxy = require('./proxy_dev')
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
      host: '0.0.0.0',
      proxy,
      historyApiFallback: {
        rewrites: [{
          from: new RegExp(`^${env.CONTEXT_PATH}`),
          to: env.CONTEXT_PATH
        }]
      }
    }
  })
}
