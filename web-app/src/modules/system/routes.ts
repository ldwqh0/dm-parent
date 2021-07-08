import { ImportedComponent } from 'vue/types/options'

export default [{
  path: '/system',
  component: (): Promise<ImportedComponent> => import('../templates/LeftTemplate.vue'),
  children: [{
    name: 'menus',
    path: 'menus',
    component: (): Promise<ImportedComponent> => import('./views/Menus.vue')
  }, {
    name: 'resources',
    path: 'resources',
    component: (): Promise<ImportedComponent> => import('./views/Resources.vue')
  }, {
    name: 'resource',
    path: 'resources/:id',
    props: true,
    component: (): Promise<ImportedComponent> => import('./views/Resource.vue')
  }, {
    name: 'roles',
    path: 'roles',
    component: (): Promise<ImportedComponent> => import('./views/Roles.vue')
  }, {
    name: 'departments',
    path: 'departments',
    component: (): Promise<ImportedComponent> => import('./views/Departments.vue')
  }, {
    name: 'clients',
    path: 'clients',
    component: (): Promise<ImportedComponent> => import('./views/Clients.vue')
  }, {
    name: 'client',
    path: 'clients/:id',
    props: true,
    component: (): Promise<ImportedComponent> => import('./views/Client.vue')
  }/*, {
    name: 'users',
    path: 'users',
    component: (): Promise<ImportedComponent> => import('./views/Users.vue')
  } */]
}]
