module.exports = (env) => {
  return {
    '/oauth2/': {
      target: 'http://10.10.1.105',
      cookiePathRewrite: {
        '/': '/oauth2/'
      }
    },
    [`${env.CONTEXT_PATH}gw/`]: {
      target: 'http://10.10.1.105',
      // xfwd: true,
      pathRewrite: { [`^${env.CONTEXT_PATH}gw/`]: '/app/gw/' },
      cookiePathRewrite: {
        '/': `${env.CONTEXT_PATH}`
      }
    }
  }
}
