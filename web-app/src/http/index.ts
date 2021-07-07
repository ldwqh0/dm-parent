import http from './http'
import _Vue, { PluginObject } from 'vue'
import store from './vuex'
import httpOptions, { PluginOptions } from './pluginOptions'

export interface ErrorState {
  count: number,
  message: string
}

export interface HttpState {
  error: ErrorState,
  requestKey: number
  requestCount: number,
  requests: {
    [key: string]: any
  }
}

const plugin: PluginObject<PluginOptions> = {
  install (Vue: typeof _Vue, options?: PluginOptions): void {
    options?.store?.registerModule('http', store)
    httpOptions.store = options?.store
  }
}

export default http

export { default as tableHttp } from './tableHttp'

export { plugin, store, httpOptions }
