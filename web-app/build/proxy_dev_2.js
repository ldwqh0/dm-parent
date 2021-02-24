module.exports = (env) => {
  // 这个配置oauth2和gateway通过一个端口暴露
  return {
    '/oauth2/': {
      target: 'http://10.10.1.105',
      cookiePathRewrite: {
        '/': '/oauth2/'
      }
    },
    [`${env.CONTEXT_PATH}gw/`]: {
      target: 'http://10.10.1.105',
      cookiePathRewrite: {
        '/': `${env.CONTEXT_PATH}`
      }
    }
  }
}
