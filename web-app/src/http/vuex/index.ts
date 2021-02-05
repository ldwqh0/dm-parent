import { ActionContext, Module } from 'vuex'
import { RootState } from '@/store'
import axios, { AxiosRequestConfig, CancelTokenSource } from 'axios'
import { HttpState } from '@/http'

interface ExtAxiosRequestConfig extends AxiosRequestConfig {
  cancelSource?: CancelTokenSource,
  key: number
}

const module: Module<HttpState, RootState> = {
  namespaced: true,
  state: {
    error: {
      count: 0,
      message: ''
    },
    requestKey: 0,
    requestCount: 0,
    requests: {}
  },
  getters: {
    loading ({ requestCount }: HttpState): boolean {
      return requestCount > 0
    },
    loadingCount ({ requestCount }: HttpState): number {
      return requestCount
    }
  },
  mutations: {
    error (state: HttpState, message: string): void {
      state.error.count++
      state.error.message = message
    },
    request (state: HttpState, config: ExtAxiosRequestConfig): void {
      const key = state.requestKey++
      config.cancelSource = axios.CancelToken.source()
      config.cancelToken = config.cancelSource.token
      config.key = key
      state.requestCount++
      state.requests[key] = config
    },
    complete (state: HttpState, { key }: ExtAxiosRequestConfig): void {
      delete state.requests[key]
      state.requestCount--
    }
  },
  actions: {
    cancelAll ({ state: { requests }, commit }: ActionContext<HttpState, RootState>): void {
      const keys: string[] = Object.keys(requests)
      for (const key of keys) {
        const config = requests[key]
        config?.cancelSource.cancel(config?.cancelToken)
        commit('complete', { key })
      }
    }
  }
}

export default module
