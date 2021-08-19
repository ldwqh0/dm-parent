import http from './http'
import _Vue, { PluginObject } from 'vue'
import store from './vuex'
import httpOptions, { PluginOptions } from './pluginOptions'
import tableHttp from './tableHttp'

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

export { plugin, store, httpOptions, tableHttp, tableHttp as simpleHttp }
