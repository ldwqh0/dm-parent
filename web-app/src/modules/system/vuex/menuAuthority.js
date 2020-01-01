import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `/gw/p/menuAuthorities`
  },
  actions: {
    update ({ commit, state }, data) {
      return Vue.http.put(`${state.url}/${data.roleName}`, data)
    },
    get ({ state }, params) {
      return Vue.http.get(`${state.url}/${params.name}`, params)
    }
  }
}
