const http = require('http')
module.exports = (env) => {
  // 这个配置oauth2和gateway没有通过一个端口暴露
  let index = ''
  return {
    '/oauth2/': {
      autoRewrite: true,
      target: 'http://10.10.1.105',
    },
    [`${env.CONTEXT_PATH}`]: {
      target: 'http://10.10.1.105',
      xfwd: true,
      autoRewrite: true,
      changeOrigin: false,
      cookiePathRewrite: {
        '/': `${env.CONTEXT_PATH}`
      },
      pathRewrite: {},
      onProxyRes ({
        statusCode,
        headers
      }, req, res) {
        // 将index.html缓存起来，在后面用于写入，这个暂时还没有找到更好的解决方案
        // 只是有个小的bug,当用户已经登录，并且重启web服务器之后，第一次进入时会白屏，刷新一下就好了
        if (index === '') {
          let temp = ''
          http.get(`${req.protocol}://${req.headers.host}${env.CONTEXT_PATH}index.html`, res => {
            res.on('data', (chunk) => { temp += chunk })
            res.on('end', () => (index = temp))
          })
        }
        if (statusCode === 404 || headers['x-accel-redirect'] || (typeof headers['content-type'] === 'string' && headers['content-type'].startsWith('text/html'))) {
          // 针对单页面应用，直接返回响应体
          headers['content-type'] = 'text/html; charset=UTF-8'
          res.write(index, 'utf8', () => { })
          res.end()
        }
      }
    }
  }
}
