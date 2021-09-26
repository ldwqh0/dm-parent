import _Vue from 'vue'
import { ModuleOptions } from '../ModuleOptions'
import vuex from './vuex'
import config from './configRoute'

export default {
  install (Vue: typeof _Vue, {
    router,
    store
  }: ModuleOptions): void {
    // routes.forEach(route => router.addRoute(route))
    store.registerModule('security', vuex)
    Vue.prototype.$hasAuthority = store.getters['templates/hasAuthority']
    config(router, store)
  }
}
