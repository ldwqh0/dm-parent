export default [{
  name: 'login',
  path: '/login',
  props: ({ query }) => ({ ...query }),
  component: () => import('./views/Login')
}]
