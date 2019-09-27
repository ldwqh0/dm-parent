export default [{
  name: 'system',
  path: '/system',
  component: () => import('@/components/templates/LeftTemplate'),
  children: [{
    name: 'users',
    path: 'users',
    component: () => import('../component/Users')
  }, {
    name: 'user',
    path: 'user/:id',
    props: true,
    component: () => import('../component/User')
  }, {
    name: 'roles',
    path: 'roles',
    component: () => import('../component/Roles')
  }, {
    name: 'role',
    path: 'roles/:id',
    props: true,
    component: () => import('../component/Role')
  }, {
    name: 'menus',
    path: 'menus',
    component: () => import('../component/Menus')
  }, {
    name: 'menu',
    path: 'menus/:id',
    props: true,
    component: () => import('../component/Menu')
  }, {
    name: 'menuAuthority',
    path: 'authorities/:name',
    props: true,
    component: () => import('../component/MenuAuthority')
  }, {
    name: 'resources',
    path: 'resources',
    component: () => import('../component/Resources')
  }, {
    name: 'resource',
    path: 'resources/:id',
    props: true,
    component: () => import('../component/Resource')
  }, {
    name: 'resourceAuthority',
    path: 'resourceAuthorites/:name',
    props: true,
    component: () => import('../component/ResourceAuthority')
  }, {
    name: 'clients',
    path: 'clients',
    component: () => import('../component/Clients')
  }, {
    name: 'client',
    path: 'clients/:id',
    props: true,
    component: () => import('../component/Client')
  }, {
    name: 'tokens',
    path: 'clients/:client/tokens',
    props: true,
    component: () => import('../component/Tokens')
  }]
}]
