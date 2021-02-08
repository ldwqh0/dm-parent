import Vue from 'vue'
import App from '@/App.vue'
import router from '@/router'
import store from '@/store'
import ElementUi from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import http, { plugin as httpPlugin } from '@/http'
import EleDataTables from 'element-datatables'
import { plugin as commonPlugin } from '@/common'

import { system, security } from './modules'

Vue.use(ElementUi)
Vue.use(EleDataTables, { httpInstance: http })

const rootConfig = {
  router, store
}
Vue.use(commonPlugin)
Vue.use(httpPlugin, rootConfig)
Vue.use(security, rootConfig)
Vue.use(system, rootConfig)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
