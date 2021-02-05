import { Store } from 'vuex'

export interface PluginOptions<T = any> {
  store?: Store<T>
}

const options: PluginOptions = {}
export default options
