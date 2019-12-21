export default [{
  name: 'system',
  path: '/system',
  component: () => import('@/components/templates/LeftTemplate'),
  children: [{
    name: 'users',
    path: 'users',
    component: () => import('./views/Users')
  }]
}]
