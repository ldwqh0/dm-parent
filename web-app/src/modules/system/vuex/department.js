import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `/gw/u/departments`,
    departments: []
  },
  getters: {
    // 将部门列表转换为树型结构
    tree: ({ departments }) => {
      const results = JSON.parse(JSON.stringify(departments))
      const roots = []
      results.forEach(department => {
        let parent
        if (department.parent) {
          parent = results.find(e => (e.id === department.parent.id))
        }
        if (parent) {
          if (!parent.children) {
            parent.children = []
          }
          parent.children.push(department)
        } else {
          roots.push(department)
        }
      })
      return roots
    },
    getDepartment ({ departments }) {
      return (id) => {
        return departments.find(e => e.id === id)
      }
    }
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
      return Vue.http.get(`${state.url}/${id}`).then(({ data }) => data)
    },
    update ({ state }, data) {
      return Vue.http.put(`${state.url}/${data.id}`, data).then(({ data }) => data)
    },
    save ({ state }, data) {
      return Vue.http.post(state.url, data).then(({ data }) => data)
    },
    del ({ state }, { id }) {
      return Vue.http.delete(`${state.url}/${id}`)
    }
  }
}
