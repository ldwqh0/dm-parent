import routes from './routes'
import vuex from './vuex'

export default {
  install (Vue, { store, router }) {
    router.addRoutes(routes)
    store.registerModule('system', vuex)
  }
}
