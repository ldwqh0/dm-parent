import VueRouter, { Route } from 'vue-router'
import Vue from 'vue'
import { EsModuleComponent } from 'vue/types/options'

Vue.use(VueRouter)

/**
 * 创建默认的路由配置
 *
 * 这里只对基本的路由进行配置，所有其它的模块是通过模块的方式动态注册到整个app中
 */
const router = new VueRouter({
  // 路由使用html history模式
  mode: 'history',
  // 指定路由的base
  base: env.CONTEXT_PATH,
  routes: [{
    path: '/errors/:code',
    name: 'error',
    props: ({ params, query }: Route) => ({ ...params, ...query }),
    component: (): Promise<EsModuleComponent> => import('../pages/ErrorPage.vue')
  }, {
    path: '*',
    props: ({ params, query }: Route) => ({ code: '404' }),
    component: (): Promise<EsModuleComponent> => import('../pages/ErrorPage.vue')
  }]
})

export default router
