import { RouteConfig } from 'vue-router'

const routes: RouteConfig[] = [{
  path: '/login',
  name: 'login',
  props: ({ query }) => ({ ...query }),
  component: () => import('./views/Login.vue')
}, {
  path: '/login/oauth2/code/:registrationId',
  name: 'oauth2Login',
  props: ({ query, params }) => ({ ...query, ...params }),
  component: () => import('./views/OAuth2Login.vue')
}]
export default routes
