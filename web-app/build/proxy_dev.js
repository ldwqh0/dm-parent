module.exports = (env) => {
  // 这个配置oauth2和gateway没有通过一个端口暴露
  return {
    [`${env.CONTEXT_PATH}gw/`]: {
      target: 'http://localhost:8080',
      xfwd: true,
      pathRewrite: { [`^${env.CONTEXT_PATH}gw/`]: '/' },
      cookiePathRewrite: {
        '/': `${env.CONTEXT_PATH}`
      }
    }
  }
}
