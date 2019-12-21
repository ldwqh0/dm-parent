import EleDataTables from './EleDataTables'
import config from './config'
import axios from 'axios'

EleDataTables.install = function (Vue, { httpInstance = axios.create() } = {}) {
  config.$http = httpInstance
  Vue.component('EleDataTables', EleDataTables)
}
export default EleDataTables
