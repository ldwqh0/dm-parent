import http from '@/http'
import { ActionContext, Module } from 'vuex'
import { RootState } from '@/store'
import { MenuDto, MenuTreeItem, UserDto } from '@/types/service'
import urls from '../urls'
import isEmpty from 'lodash/isEmpty'
import defaultMenus from './defaultMenus'

interface SecurityState {
  menus: MenuTreeItem[],
  user: UserDto
}

const module: Module<SecurityState, RootState> = {
  namespaced: true,
  state: {
    menus: [],
    user: {}
  },
  mutations: {
    menus (state: SecurityState, menus: MenuDto[]): void {
      state.menus = menus
    },
    user (state: SecurityState, user: UserDto): void {
      state.user = user
    }
  },
  getters: {
    menuTree ({ menus }: SecurityState, { menuMap }: { menuMap: { [x: string]: MenuDto } }): MenuTreeItem[] {
      const result: MenuTreeItem[] = []
      menus.forEach((menu) => {
        let parent: MenuTreeItem | null = null
        if (menu.parent) {
          parent = menuMap[`${menu.parent.id}`]
        }
        if (parent !== null) {
          if (!parent.children) {
            parent.children = []
          }
          parent.children?.push(menu)
        } else {
          result.push(menu)
        }
      })
      return result
      // const root = result.find(({ name }) => name === '管理部门端')
      // return root ? (root.children ?? []) : []
    },
    menuMap ({ menus }: { menus: MenuDto[] }): { [x: string]: MenuDto } {
      return menus.reduce(
        (acc: { [x: string]: MenuDto }, cur) => {
          acc[`${cur.id}`] = cur
          return acc
        }, {})
    },
    hasAuthority ({ user }: SecurityState): (v: string) => boolean {
      return (authority: string) => {
        return user.roles?.map(role => role.authority).includes(authority) ?? false
      }
    },
    authorities ({ user }: SecurityState): string[] {
      return user.roles?.map(role => (role.authority ?? '')).filter(v => !isEmpty(v)) ?? []
    }
  },
  actions: {
    loadMenus ({
      commit,
      state
    }: ActionContext<SecurityState, RootState>): Promise<MenuDto[]> {
      if (isEmpty(state.menus)) {
        return http.get(urls.currentMenu)
          .then(({ data }) => {
            commit('menus', data)
            return data
          }).catch(() => {
            return []
          })
      } else {
        return Promise.resolve(state.menus)
      }
    },
    loadUserInfo ({
      commit,
      state
    }: ActionContext<SecurityState, RootState>): Promise<UserDto> {
      if (isEmpty(state.user)) {
        return http.get(urls.currentUser).then(({ data }) => {
          commit('user', data)
          return data
        })
      } else {
        return Promise.resolve(state.user)
      }
    },
    logout ({ commit }: ActionContext<SecurityState, RootState>): Promise<unknown> {
      commit('menus', [])
      commit('user', {})
      return http.post(`${urls.logout}`).then(() => {
        console.debug('登出成功')
      })
    }
  }
}
export default module
