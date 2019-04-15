import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `${CONTEXT_PATH}u/departments`,
    departments: []
  },
  getters: {
    departments ({ departments }) {}
  },
  mutations: {
    departments (state, data) {
      state.departments = data
    }
  },
  actions: {
    loadDepartments ({ state, commit }, params) {
      return Vue.http.get(state.url, { params: { ...params, type: 'tree' } }).then(({ data }) => {
        console.log(data)
      })
    }
  }
}
