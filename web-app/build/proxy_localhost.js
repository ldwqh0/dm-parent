const http = require('http')
module.exports = (env) => {
  // 这个配置oauth2和gateway没有通过一个端口暴露
  const APP_NAME = 'admin' //这个字符串是context_path去掉前后斜杠组成的
  let index = ''
  return {
    [`${env.CONTEXT_PATH}login`]: {
      target: 'http://localhost:8080',
      cookiePathRewrite: {
        '/': `${env.CONTEXT_PATH}`
      },
      pathRewrite: {
        [`${env.CONTEXT_PATH}`]: '/'
      }
    },
    [`${env.CONTEXT_PATH}gw/`]: {
      target: 'http://localhost:8080',
      pathRewrite: { [`^${env.CONTEXT_PATH}gw/`]: '/' },
      cookiePathRewrite: {
        '/': `${env.CONTEXT_PATH}`
      }
    },
    [`${env.CONTEXT_PATH}`]: {
      target: 'http://localhost:8080',
      xfwd: true,
      cookiePathRewrite: {
        '/': `${env.CONTEXT_PATH}`
      },
      pathRewrite: {
        [`${env.CONTEXT_PATH}oauth2/authorization/oauth2`]: '/oauth2/authorization/oauth2'
      },
      headers: {
        'x-forwarded-proxy-prefix': `${APP_NAME}`
      },
      onProxyRes ({ statusCode, headers }, req, res) {
        // 将index.html缓存起来，在后面用于写入，这个暂时还没有找到更好的解决方案
        // 只是有个小的bug,当用户已经登录，并且重启web服务器之后，第一次进入时会白屏，刷新一下就好了
        if (index === '') {
          let temp = ''
          http.get(`${req.protocol}://${req.headers.host}${env.CONTEXT_PATH}index.html`, res => {
            res.on('data', (chunk) => { temp += chunk })
            res.on('end', () => (index = temp))
          })
        }
        if (statusCode === 302) {
          const { location } = headers
          // 如果302的是授权请求，需要重新发回服务器
          if (location === '/oauth2/authorization/oauth2') {
            headers.location = `${env.CONTEXT_PATH}oauth2/authorization/oauth2`
          }
        } else if (headers['x-accel-redirect']) {
          // 针对单页面应用，直接返回响应体
          headers['content-type'] = 'text/html; charset=UTF-8'
          res.write(index, 'utf8', (a, b, c) => {})
          res.end()
        }
      }
    }
  }
}
