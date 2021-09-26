export default function (env) {
  // 这个配置oauth2和gateway没有通过一个端口暴露
  // 这个字符串是context_path去掉前后斜杠组成的
  return {
    '/': {
      target: 'http://10.10.1.104',
      xfwd: true,
      bypass (req, res, option) {
        if (req.headers.accept.indexOf('html') !== -1
          && req.url !== '/admin/gw/login'
          && !req.url.startsWith('/login/oauth2/')
          && !req.url.startsWith('/oauth2/')
        ) {
          return '/admin/index.html'
        }
      }
    }
  }
}
