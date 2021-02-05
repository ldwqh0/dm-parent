const path = require('path')
const CopyWebpackPlugin = require('copy-webpack-plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const VueLoaderPlugin = require('vue-loader/lib/plugin')
const ESLintWebpackPlugin = require('eslint-webpack-plugin')
const webpack = require('webpack')

module.exports = env => {
  return {
    entry: {
      main: ['core-js/stable', path.resolve(__dirname, '..', env.entry ?? 'src')]
    },
    module: {
      rules: [{
        test: /((m?j)|t)s$/,
        exclude: (path) =>
          /(node_modules|bower_components)/.test(path) &&
          !/node_modules(\\|\/)element-datatables/.test(path) &&
          !/node_modules(\\|\/)vue2-uploader/.test(path) &&
          !/node_modules(\\|\/)vuex/.test(path),
        loader: 'ts-loader',
        options: {
          appendTsSuffixTo: [/\.vue$/],
          transpileOnly: true
        }
      }, {
        test: /\.vue$/,
        loader: 'vue-loader'
      }, {
        test: /\.(woff|ttf)$/,
        use: [{
          loader: 'file-loader',
          options: {
            esModule: false,
            outputPath: 'static/fonts'
          }
        }]
      }, {
        test: /\.(png|jpe?g|gif)$/i,
        use: [{
          loader: 'file-loader',
          options: {
            esModule: false,
            outputPath: 'static/images'
          }
        }]
      }]
    },
    resolve: {
      extensions: ['.js', '.ts', '.vue', '.json', '.d.ts'],
      alias: {
        '@': path.resolve(__dirname, '../', 'src')
      }
    },
    plugins: [
      new HtmlWebpackPlugin({
        template: path.resolve(__dirname, '../', 'public', 'index.html')
      }),
      new VueLoaderPlugin(),
      new ESLintWebpackPlugin({
        extensions: ['ts', 'js', 'vue'],
        // formatter: 'eslint-formatter-friendly'
      }),
      new CopyWebpackPlugin({
        patterns: [{
          from: path.resolve(__dirname, '../public/static'),
          to: path.resolve(__dirname, `..${env.CONTEXT_PATH}`)
        }]
      }),
      new webpack.DefinePlugin({
        // 将配置对象env抽象为全局对象
        env: Object.entries(env).reduce((acc, [key, value]) => ({
          ...acc,
          [key]: JSON.stringify(value)
        }), {})
      })
    ],
    output: {
      publicPath: env.CONTEXT_PATH,
    }
  }
}
