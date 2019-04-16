import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `${CONTEXT_PATH}u/departments`,
    departments: []
  },
  getters: {
    departments: ({ departments }) => departments
  },
  mutations: {
    departments (state, data) {
      state.departments = data
    }
  },
  actions: {
    loadDepartments ({ state, commit }, params) {
      return Vue.http.get(state.url, { params: { ...params, type: 'tree' } }).then(({ data }) => {
        commit('departments', data)
        return data
      })
    },
    get ({ state }, { id }) {
      return Vue.http.get(`${state.url}/${id}`)
    },
    update ({ state }, data) {
      return Vue.http.update
    },
    save ({ state }, data) {
      return Vue.http.post(state.url, data)
    },
    del ({ state }, { id }) {
      return Vue.http.delete(`${state.url}/${id}`)
    }
  }
}
