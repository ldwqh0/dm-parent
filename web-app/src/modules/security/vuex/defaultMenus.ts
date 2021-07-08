import { MenuDto } from '@/types/service'

export default [{
  id: 1,
  title: '部门管理',
  url: '/system/departments'
}, {
  id: 2,
  title: '角色管理',
  url: '/system/roles'
}, {
  id: 3,
  title: '菜单管理',
  url: '/system/menus'
}, {
  id: 4,
  title: '资源管理',
  url: '/system/resources'
}, {
  id: 5,
  title: '应用管理',
  url: '/system/clients'
}] as MenuDto[]
