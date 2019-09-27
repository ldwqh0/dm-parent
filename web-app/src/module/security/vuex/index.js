import axios from 'axios'
import Vue from 'vue'
import qs from 'qs'

const loginInstance = axios.create({
  // 登录的数据格式，使用form表单格式
  transformRequest: [(data, headers) => qs.stringify(data, { arrayFormat: 'repeat' })]
})

export default {
  namespaced: true,
  state: {
    url: {
      currentUser: `${CONTEXT_PATH}p/users/current`,
      login: `${CONTEXT_PATH}p/login`,
      logout: `${CONTEXT_PATH}p/logout`
    },
    user: {
      fullname: '未知用户',
      username: 'unknown',
      region: [],
      regionCode: null
    }
  },
  mutations: {
    currentUser: (state, user) => (state.user = user)
  },
  actions: {
    login ({ commit, state }, data) {
      return loginInstance.post(state.url.login, data)
    },
    loadCurrentUser ({ commit, state: { url: { currentUser } } }) {
      return Vue.http.get(currentUser).then(({ data }) => {
        commit('currentUser', data)
        return data
      })
    },
    logout ({ commit, state: { url: { logout } } }) {
      return Vue.http.get(logout)
    }
  }
}
