import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    url: `/gw/p/menus`,
    menus: []
  },
  getters: {
    tree ({ menus: datas }) {
      const result = []
      const menus = JSON.parse(JSON.stringify(datas))

      menus.forEach(menu => {
        let parent
        if (menu.parent) {
          parent = menus.find(e => e.id === menu.parent.id)
        }
        // 如果没有找到上级节点，将节点添加到根目录，否则将节点添加到上级节点的children中
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

    getParents ({ menus }) {
      return (menu) => {
        const result = []
        if (menu.parent) {
          let parent = menus.find(({ id }) => id === menu.parent.id)
          while (parent !== undefined) {
            result.push(parent)
            parent = parent.parent
          }
        }
        return result
      }
    }
  },
  mutations: {
    updateMenus (state, data) {
      state.menus = data
    }
  },
  actions: {
    get ({ commit, state }, params) {
      return Vue.http.get(`${state.url}/${params.id}`, params)
    },
    save ({ commit, state }, menu) {
      return Vue.http.post(state.url, menu)
    },
    update ({ commit, state }, menu) {
      return Vue.http.put(`${state.url}/${menu.id}`, menu)
    },
    listAll ({ state, commit }) {
      return Vue.http.get(state.url).then(({ data: menus }) => {
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
