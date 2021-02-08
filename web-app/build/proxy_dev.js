module.exports = (env) => {
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
