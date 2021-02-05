import Vue, { PluginObject } from 'vue'
import { formatDate } from './formatDate'
import { formatDatetime } from './formatDatetime'

const plugin: PluginObject<unknown> = {
  install (_Vue: typeof Vue) {
    _Vue.filter('formatDate', formatDate)
    _Vue.filter('formatDatetime', formatDatetime)
  }
}

export { plugin, formatDatetime, formatDate }
