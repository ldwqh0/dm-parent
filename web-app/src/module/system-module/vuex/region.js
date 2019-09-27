import Vue from 'vue'
import { StringUtils } from '@/utils'

export default {
  namespaced: true,
  state: {
    url: `${CONTEXT_PATH}u/regions`,
    regions: []
  },
  getters: {
    regionTree ({ regions }) {
      return regions.filter(({ parentCode }) => parentCode == null || parentCode === undefined)
    },
    codeToArray () {
      let findArray = (code, regionArray, result) => {
        if (code !== null && code !== undefined) {
          let region = regionArray.find(({ code: regionCode }) => code.startsWith(StringUtils.stripEnd(regionCode, '0')))
          if (region !== undefined) {
            result.push(region.code)
            if (region.children !== undefined && region.children !== null) {
              findArray(code, region.children, result)
            }
          }
        }
      }
      return (code, regionArray) => {
        let result = []
        findArray(code, regionArray, result)
        return result
      }
    }
  },
  mutations: {
    regions (state, regions) {
      state.regions = regions
    }
  },
  actions: {
    loadRegions ({ commit, state }) {
      Vue.http.get(state.url, { params: { type: 'tree' } }).then(rsp => {
        commit('regions', rsp)
      })
    }
  }
}
