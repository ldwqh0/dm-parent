import user from './user'
import role from './role'
import department from './department'
import menu from './menu'
import menuAuthority from './menuAuthority'
import resourceAuthority from './resourceAuthority'

export default {
  namespaced: true,
  state: {},
  actions: {},
  modules: {
    user,
    role,
    department,
    menu,
    menuAuthority,
    resourceAuthority
  }
}
