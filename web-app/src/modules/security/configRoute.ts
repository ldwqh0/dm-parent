import VueRouter from 'vue-router'
import { Store } from 'vuex'
import whiteList from './whiteList'
import { MenuDto } from '@/types/Service'

function matches (target: string, list: string[]): boolean {
  return list.findIndex(i => target.startsWith(i)) > -1
}

export default function (router: VueRouter, store: Store<unknown>): void {
  router.beforeEach((to, from, next) => {
    if (matches(to.fullPath, whiteList)) {
      next()
    } else {
      Promise.all([
        store.dispatch('security/loadMenus'),
        store.dispatch('security/loadUserInfo')
      ]).then(([menus]) => {
        if (matches(to.fullPath, menus.map((menu: MenuDto) => menu.url))) {
          next()
        } else {
          store.dispatch('security/logout')
            .then(() => next({ name: 'error', params: { code: '403' } }))
        }
      }).catch((e) => {
        if (e.response?.status >= 500) {
          next({ name: 'error', params: { code: '500' } })
        }
      })
    }
  })
}
