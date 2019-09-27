import Vue from 'vue'
import { groupBy, reduce, flatMap } from 'rxjs/operators'
import { from } from 'rxjs'

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
  getters: {
    roleTree ({ roles }) {
      let roleTree = []
      from(roles).pipe(
        groupBy(role => role.group.id),
        flatMap(role => role.pipe(
          reduce(({ children }, { id, name, description, group: { id: groupId, name: groupName } }) => ({
            id: `group-${groupId}`,
            name: groupName,
            children: [...children, { id, name, description }]
          }), { children: [] })
        ))
      ).subscribe(next => roleTree.push(next))
      return roleTree
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
        commit('updateRoles', data)
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
