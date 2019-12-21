import routes from './routes'

export default {
  install (Vue, { store, router }) {
    router.addRoutes(routes)
    // store.registerModule('security')
  }
}
