import Vue from 'vue'
import Vuex, { Store } from 'vuex'

export interface RootState {
  title: string,
}

Vue.use(Vuex)
export default new Store<RootState>({
  state: {
    title: '应用'
  },
  mutations: {
    title (state, payload) {
      if (payload.title) {
        state.title = payload.title
      }
    }
  }
})
