import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `${CONTEXT_PATH}u/roles`,
    roles: []
  },
  mutations: {
    updateRoles (state, roles) {
      state.roles = roles
    }
  },
  actions: {
    save ({ commit, state }, role) {
      return Vue.http.post(state.url, role)
    },
    update ({ commit, state }, role) {
      return Vue.http.put(`${state.url}/${role.id}`, role)
    },
    listAll ({ state, commit }) {
      return Vue.http.get(state.url).then(roles => {
        commit('updateRoles', roles)
        return roles
      })
    },
    get ({ commit, state }, { id }) {
      return Vue.http.get(`${state.url}/${id}`)
    },
    delete ({ state }, { id }) {
      return Vue.http.delete(`${state.url}/${id}`)
    }
  }
}
