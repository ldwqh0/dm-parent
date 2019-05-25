export default [{
  name: 'login',
  path: '/login',
  props: ({ params, query }) => ({ ...params, ...query }),
  component: () => import('../component/Login')
}]
