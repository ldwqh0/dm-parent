import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `${CONTEXT_PATH}u/clients`
  },
  actions: {
    save ({ state, commit }, client) {
      return Vue.http.post(state.url, client)
    },
    get ({ state, commit }, { id }) {
      return Vue.http.get(`${state.url}/${id}`)
    },
    update ({ state, commit }, { id, client }) {
      return Vue.http.put(`${state.url}/${id}`, client)
    },
    listToken ({ state, commit }, { client }) {
      return Vue.http.get(`${state.url}/${client}/tokens`)
    },
    delToken ({ state, commit }, { value: id }) {
      return Vue.http.delete(`${CONTEXT_PATH}u/clients/tokens/${id}`)
    }
  }
}
