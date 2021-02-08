import routes from './routes'
import vuex from './vuex'
import _Vue from 'vue'
import { ModuleOptions } from '../ModuleOptions'

export default {
  install (Vue: typeof _Vue, { router, store }: ModuleOptions): void {
    routes.forEach(route => router.addRoute(route))
    store.registerModule('system', vuex)
  }
}
