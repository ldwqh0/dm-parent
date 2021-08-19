import { RouteConfig } from 'vue-router'

const routes: RouteConfig[] = [{
  path: '/login',
  name: 'login',
  props: ({ query }) => ({ ...query }),
  component: () => import('./views/Login.vue')
}]
export default routes
