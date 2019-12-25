import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `/gw/u/roleGroups`,
    roleGroups: []
  },
  mutations: {
    roleGroups (state, groups) {
      state.roleGroups = groups
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
      return Vue.http.get(state.url).then(({ data }) => {
        commit('roleGroups', data)
        return data
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
