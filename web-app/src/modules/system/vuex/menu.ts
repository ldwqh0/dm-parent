import { ActionContext, Module } from 'vuex'
import http from '@/http'
import URLS from '../URLS'
import { RootState } from '@/store'
import { MenuDto, MenuTreeItem } from '@/types/service'

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
    tree ({ menus: originMenus }: MenuState): MenuTreeItem[] {
      const children: MenuTreeItem[] = []
      const menus: MenuTreeItem[] = originMenus.map(({
        id,
        title,
        parent,
        type,
        url,
        openInNewWindow,
        icon
      }: MenuDto) => {
        // 将menu转换为menuTreeItem
        const result: MenuTreeItem = {
          openInNewWindow,
          type,
          url,
          id,
          title,
          icon
        }
        if (parent !== undefined && parent !== null) {
          result.parent = {
            id: parent.id
          }
        }
        return result
      })
      menus.forEach(menu => {
        let parent: MenuTreeItem | undefined
        if (menu.parent) {
          parent = menus.find(e => e.id === menu.parent?.id)
        }
        // 如果没有找到上级节点，将节点添加到根目录，否则将节点添加到上级节点的children中
        if (parent) {
          if (!parent.children) {
            parent.children = []
          }
          parent.children.push(menu)
        } else {
          children.push(menu)
        }
      })

      return children
    }
  },
  actions: {
    loadAll ({ commit }: ActionContext<MenuState, RootState>): Promise<any> {
      return http.get(URLS.menus).then(({ data: menus }) => {
        commit('menus', menus)
        return menus
      })
    }
  }
}

export default menuModule
