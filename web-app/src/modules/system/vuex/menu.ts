import { ActionContext, Module } from 'vuex'
import http from '@/http'
import URLS from '../URLS'
import { RootState } from '@/store'
import { MenuDto, MenuTreeItem } from '@/types/service'
import { listToTree } from '@/utils'

interface MenuState {
  menus: MenuDto[]
}

const menuModule: Module<MenuState, RootState> = {
  namespaced: true,
  state: {
    menus: []
  },
  mutations: {
    menus (state: MenuState, data: MenuDto[]): void {
      state.menus = data
    }
  },
  getters: {
    tree ({ menus }: MenuState): MenuTreeItem[] {
      return listToTree(menus)
    },
    map ({ menus }): { [key: string]: MenuDto } {
      return menus.reduce((acc, cur) => ({ ...acc, [`${cur.id}`]: cur }), {} as { [key: string]: MenuDto })
    }
  },
  actions: {
    loadAll ({ commit }: ActionContext<MenuState, RootState>): Promise<any> {
      return http.get(URLS.menus, { params: { scope: 'all', enabled: true } })
        .then(({ data: menus }) => {
          commit('menus', menus)
          return menus
        })
    }
  }
}

export default menuModule
