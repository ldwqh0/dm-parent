import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `${CONTEXT_PATH}p/menus`,
    menus: []
  },
  getters: {
    tree ({ menus: datas }) {
      let root = []
      let findParent = (data, datas) => {
        if (data.parent === null || data.parent === undefined) {
          return null
        }
        for (let dat of datas) {
          if (dat.id === data.parent.id) {
            return dat
          }
        }
        return null
      }
      for (let dat of datas) {
        let parent = findParent(dat, datas)
        if (parent === null) {
          root.push(dat)
        } else {
          let children = parent.children ? parent.children : []
          children.push(dat)
          parent.children = children
        }
      }
      return root
    }
  },
  mutations: {
    updateMenus (state, data) {
      state.menus = data
    }
  },
  actions: {
    get ({ commit, state }, params) {
      return Vue.http.get(`${state.url}/${params.id}`, params).then(menu => {
        let parents = menu.parents = []
        let parent = menu.parent
        while (parent !== null && parent !== undefined) {
          parents.unshift(parent.id)
          parent = parent.parent
        }
        return menu
      })
    },
    save ({ commit, state }, menu) {
      if (menu.parents !== null && menu.parents !== undefined) {
        menu.parent = { id: menu.parents.pop() }
      }
      return Vue.http.post(state.url, menu)
    },
    update ({ commit, state }, menu) {
      menu.parent = { id: menu.parents.pop() }
      return Vue.http.put(`${state.url}/${menu.id}`, menu)
    },
    listAll ({ state, commit }) {
      return Vue.http.get(state.url).then(menus => {
        commit('updateMenus', menus)
        return menus
      })
    },
    move ({ state, commit, dispatch }, { id, position }) {
      return Vue.http.put(`${state.url}/${id}/order`, { position }).then(menu => {
        dispatch('listAll')
        return menu
      })
    },
    delete ({ state, dispatch }, { id }) {
      return Vue.http.delete(`${state.url}/${id}`).then(() => {
        dispatch('listAll')
      })
    },
    patch ({ state, commit, dispatch }, menu) {
      return Vue.http.patch(`${state.url}/${menu.id}`, menu).then(menu => {
        dispatch('listAll')
        return menu
      })
    }
  }
}
