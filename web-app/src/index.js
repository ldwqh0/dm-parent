import Vue from 'vue'
import App from './App.vue'
import router from './config/router'
import store from './store'
import ElementUi from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import EleDataTables from './components/ele-datatables'
import { security, system } from './modules'
import { AjaxPlugin } from './plugins'
import httpInstance from './config/http'
import './styles/default.less'

Vue.use(security, { router, store })
Vue.use(system, { router, store })
Vue.use(ElementUi)
Vue.use(AjaxPlugin, httpInstance)
Vue.use(EleDataTables, { httpInstance })
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
