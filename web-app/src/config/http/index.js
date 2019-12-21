// import store from '@/store'
import axios, { CancelToken } from 'axios'
import router from '../router'
import qs from 'qs'

class Queue {
  holder = new Map()
  counter = 0

  put (config) {
    config.key = this.counter++
    this.holder.set(config.key, config)
  }

  delete ({ key }) {
    this.holder.delete(key)
  }

  cancelAll () {
    this.holder.forEach(request => {
      request.cancelSource.cancel(request)
      this.holder.delete(request.key)
    })
  }
}

const queue = new Queue()
const instance = axios.create()

instance.interceptors.request.use(config => {
  // 取消
  config.cancelSource = CancelToken.source()
  config.paramsSerializer = (params) => {
    return qs.stringify(params, { arrayFormat: 'repeat' })
  }
  // 添加取消
  config.cancelToken = config.cancelSource.token

  // const authorization = store.getters['oauth/token']
  // if (authorization !== null) {
  //   config.headers.Authorization = authorization
  // }
  queue.put(config)
  return config
}, error => {
  return Promise.reject(error)
})

instance.interceptors.response.use(response => {
  // 将请求从队列中删除
  queue.delete(response.config)
  return response
}, error => {
  // 响应之后，首先从队列中删除
  if (error.config) {
    queue.delete(error.config)
  }
  // 如果结果是服务器错误
  if (error.response) {
    const { response: { status } } = error
    // 检测到401时，取消所有队列中的请求
    if (status === 401) {
      console.debug('检测到未登录，取消所有请求')
      queue.cancelAll()
      const { name, query, params } = router.history.current
      const redirect = { name, query, params }
      router.replace({ name: 'login', query: { redirect: JSON.stringify(redirect) } })
    } else {
      // store.commit('addError', error)
    }
  }
  return Promise.reject(error)
})
export default instance
