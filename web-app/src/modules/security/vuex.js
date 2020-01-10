import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: {
      currentUser: `/gw/authorities/current`,
      login: `/gw/p/login`,
      token: '/oauth/token',
      authorityMenus: `/gw/p/menuAuthorities/current`
    },
    token: {},
    menus: [],
    currentUser: {}
  },
  mutations: {
    token: (state, token) => (state.token = token),
    menus: (state, menus) => (state.menus = menus),
    currentUser: (state, data) => (state.currentUser = data)
  },
  getters: {
    menuTree ({menus}, {menuMap}) {
      const result = []
      menus.forEach(menu => {
        let parent
        if (menu.parent) {
          parent = menuMap[menu.parent.id]
        }
        if (parent) {
          if (!parent.children) {
            parent.children = []
          }
          parent.children.push(menu)
        } else {
          result.push(menu)
        }
      })
      return result
    },
    menuMap: ({menus}) => menus.reduce((acc, cur) => {
      acc[cur.id] = cur
      return acc
    }, {})

  },
  actions: {
    loadMenus ({commit, state: {url: {authorityMenus}}}) {
      return Vue.http.get(authorityMenus).then(({data}) => {
        commit('menus', data)
        return data
      })
    },
    loadCurrentUser ({commit, state: {url: {currentUser}}}) {
      Vue.http.get(currentUser).then(({data}) => {
        commit('currentUser', data)
      })
    }
  }
}
