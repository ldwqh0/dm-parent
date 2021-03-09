import axios, { AxiosRequestConfig } from 'axios'
import qs from 'qs'
import pluginOption from './pluginOptions'

const http = axios.create()

function commitRequest (config: AxiosRequestConfig) {
  return pluginOption.store?.commit('http/request', config)
}

function commitComplete (config: AxiosRequestConfig) {
  return pluginOption.store?.commit('http/complete', config)
}

function commitError (error: string) {
  return pluginOption.store?.commit('http/error', error)
}

function cancelAll (): Promise<unknown> {
  return Promise.resolve().then(() => pluginOption.store?.dispatch('http/cancelAll'))
}

http.interceptors.request.use(config => {
  // console.log('从指定地址加载数据', config.url, config)
  config.paramsSerializer = (params) => qs.stringify(params, { arrayFormat: 'repeat' })
  commitRequest(config)
  return config
}, error => {
  return Promise.reject(error)
})

http.interceptors.response.use(response => {
  if (process.env.NODE_ENV === 'development') {
    console.debug(`url: ${response.config?.url}`, response)
  }
  commitComplete(response.config)
  return response
}, error => {
  commitComplete(error.config)
  return Promise.reject(error)
})

http.interceptors.response.use(data => data, (error): Promise<unknown> => {
  if (error.response?.status === 401) {
    // const redirect = window.location.href
    // cancelAll().then(() => router.replace({
    //   name: 'login',
    //   query: { redirect }
    // }))
    // 如果检测到401时，
    window.location.reload()
  }
  commitError(error?.response?.data?.message ?? '服务器发生未知错误')
  return Promise.reject(error)
})

export default http
