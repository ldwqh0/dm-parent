import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `${CONTEXT_PATH}u/departments`,
    departments: []
  },
  getters: {
    departments: ({ departments }) => departments,
    getParents: ({ departments }, { getDepartment }) => {
      return (id) => {
        let result = [id]
        let dep = getDepartment(id)
        while (dep && dep.parentId) {
          dep = getDepartment(dep.parentId)
          result.push(dep.id)
        }
        return result.reverse()
      }
    },
    getDepartment ({ departments }) {
      return (id) => {
        let result

        // 查找结点
        let findNode = (id$, departments$) => {
          for (let d of departments$) {
            if (d.id === id$) {
              result = d
              return
            } else if (d.children !== undefined && d.children !== null) {
              findNode(id$, d.children)
            }
          }
        }
        findNode(id, departments)
        return result
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
