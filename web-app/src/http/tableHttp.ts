import axios from 'axios'

const http = axios.create()

// function commitRequest (config: AxiosRequestConfig) {
//   return pluginOption.store?.commit('http/request', config)
// }
//
// function commitComplete (config: AxiosRequestConfig) {
//   return pluginOption.store?.commit('http/complete', config)
// }
//
// function commitError (error: string) {
//   return pluginOption.store?.commit('http/error', error)
// }
//
// function cancelAll (): Promise<unknown> {
//   return Promise.resolve().then(() => pluginOption.store?.dispatch('http/cancelAll'))
// }
//
// http.interceptors.request.use(config => {
//   // console.log('从指定地址加载数据', config.url, config)
//   config.paramsSerializer = (params) => qs.stringify(params, { arrayFormat: 'repeat' })
//   commitRequest(config)
//   return config
// }, error => {
//   return Promise.reject(error)
// })
//
// http.interceptors.response.use(response => {
//   if (process.env.NODE_ENV === 'development') {
//     console.debug(`url: ${response.config?.url}`, response)
//   }
//   commitComplete(response.config)
//   return response
// }, error => {
//   commitComplete(error.config)
//   return Promise.reject(error)
// })

http.interceptors.response.use(data => data, (error): Promise<unknown> => {
  if (error.response?.status === 401) {
    window.location.reload()
  }
  return Promise.reject(error)
})

export default http
