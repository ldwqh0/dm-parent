import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `${CONTEXT_PATH}u/users`
  },
  actions: {
    save ({ commit, state }, user) {
      return Vue.http.post(state.url, user)
    },
    update ({ commit, state }, user) {
      return Vue.http.put(`${state.url}/${user.id}`, user)
    },
    get ({ commit, state }, params) {
      return Vue.http.get(`${state.url}/${params.id}`, { params })
    }
  }
}
