import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `${CONTEXT_PATH}p/resources`,
    scopeUrl: `${CONTEXT_PATH}p/scopes`
  },
  actions: {
    save ({ commit, state }, obj) {
      return Vue.http.post(state.url, obj)
    },
    get ({ state }, params) {
      return Vue.http.get(`${state.url}/${params.id}`, params)
    },
    update ({ state }, resource) {
      return Vue.http.put(`${state.url}/${resource.id}`, resource)
    },
    delete ({ state }, id) {
      return Vue.http.delete(`${state.url}/${id}`)
    },
    list ({ state }) {
      return Vue.http.get(state.url)
    },
    listScopes ({ state }) {
      return Vue.http.get(state.scopeUrl)
    }
  }
}
