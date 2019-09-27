import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `${CONTEXT_PATH}p/resourceAuthorities`
  },
  actions: {
    save ({ state }, data) {
      return Vue.http.post(state.url, data)
    },
    list ({ state }, id) {
      return Vue.http.get(`${state.url}/${id}`)
    }
  }
}
