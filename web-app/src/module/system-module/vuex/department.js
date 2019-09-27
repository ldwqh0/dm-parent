import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `${CONTEXT_PATH}u/departments`,
    // 这里存储的是树形结构
    // 数据结构如下
    // [{
    //   "id":1
    //   "name":"节点名称",
    //   "children":[{
    //     "id":2
    //     "name":"子节点名称"，
    //     "parentId":1,
    //     "children":[{
    //       ...
    //     }]
    //   }]
    // }]
    departments: []
  },
  getters: {
    departments: ({ departments }) => departments,
    // 获取一个节点的上级节点到本节点的数组结构
    // 示例,假设数据结构如下
    // [{
    //   "id":1,
    //   "name":"根节点"
    //   "children":[{
    //     "id":2,
    //     "name":"子节点",
    //     "parentId":1
    //   }]
    // }]
    // 使用该getter getParents(2) 获取到的结果应该是 [1,2]
    //
    getParents: ({ departments }, { getDepartment }) => {
      return (id) => {
        let result = [id]
        let dep = getDepartment(id)
        while (dep && dep.parentId) {
          dep = getDepartment(dep.parentId)
          result.unshift(dep.id)
        }
        return result
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
