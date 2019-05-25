const translateUrl = function (url) {
  let [protocol, , host, ...pathArr] = url.split('/')

  let hostname, port
  if (host !== undefined && host !== null) {
    [hostname, port] = host.split(':')
  }
  let pathname = pathArr.join('/')

  return {
    protocol, hostname, host, port, pathname
  }
}

export default translateUrl
