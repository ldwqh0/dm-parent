import user from './user'
import role from './role'
import menu from './menu'
import menuAuthority from './menuAuthority'
import resourceAuthority from './resourceAuthority'
import resource from './resource'
import client from './client'
import region from './region'
import roleGroup from './roleGroup'

export default {
  namespaced: true,
  state: {},
  actions: {},
  modules: {
    user,
    role,
    menu,
    menuAuthority,
    resourceAuthority,
    resource,
    client,
    region,
    roleGroup
  }
}
