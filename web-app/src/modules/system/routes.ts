import { EsModuleComponent } from 'vue/types/options'

export default [{
  path: '/system',
  component: (): Promise<EsModuleComponent> => import('../templates/LeftTemplate.vue'),
  children: [{
    name: 'menus',
    path: 'menus',
    component: (): Promise<EsModuleComponent> => import('./views/Menus.vue')
  }, {
    name: 'resources',
    path: 'resources',
    component: (): Promise<EsModuleComponent> => import('./views/Resources.vue')
  }, {
    name: 'resource',
    path: 'resources/:id',
    props: true,
    component: (): Promise<EsModuleComponent> => import('./views/Resource.vue')
  }, {
    name: 'roles',
    path: 'roles',
    component: (): Promise<EsModuleComponent> => import('./views/Roles.vue')
  }, {
    name: 'departments',
    path: 'departments',
    component: (): Promise<EsModuleComponent> => import('./views/Departments.vue')
  }, {
    name: 'clients',
    path: 'clients',
    component: (): Promise<EsModuleComponent> => import('./views/Clients.vue')
  }, {
    name: 'client',
    path: 'clients/:id',
    props: true,
    component: (): Promise<EsModuleComponent> => import('./views/Client.vue')
  }/*, {
    name: 'users',
    path: 'users',
    component: (): Promise<EsModuleComponent> => import('./views/Users.vue')
  } */]
}]
