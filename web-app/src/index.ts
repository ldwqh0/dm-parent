import Vue from 'vue'
import App from '@/App.vue'
import router from '@/router'
import store from '@/store'
import ElementUi from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import http, { plugin as httpPlugin } from '@/http'
import EleDataTables from 'element-datatables'
import { plugin as commonPlugin } from '@/common'

Vue.use(ElementUi)
Vue.use(EleDataTables, { httpInstance: http })

document.title = '张家港农业农村局项目管理系统 - 申报端'

const rootConfig = {
  router, store
}
Vue.use(commonPlugin)
Vue.use(httpPlugin, { store })
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
