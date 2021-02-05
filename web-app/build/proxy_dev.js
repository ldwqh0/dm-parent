module.exports = {
  '/pm/gw/': {
    target: 'http://localhost:8080',
    pathRewrite: { '^/pm/gw/': '/' },
    cookiePathRewrite:{
      "/":"/pm/"
    }
  },
  '/pw/gw/': {
    target: 'http://localhost:8080',
    pathRewrite: { '^/pw/gw/': '/' },
    cookiePathRewrite:{
      "/":"/pw/"
    }
  }
}
