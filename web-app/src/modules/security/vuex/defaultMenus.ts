import { MenuDto } from '@/types/service'

export default [{
  id: 1,
  title: '部门人员',
  url: '/system/departments'
}, {
  id: 2,
  title: '角色权限',
  url: '/system/roles'
}, {
  id: 3,
  title: '功能菜单',
  url: '/system/menus'
}, {
  id: 4,
  title: '资源权限',
  url: '/system/resources'
}, {
  id: 5,
  title: '连接程序',
  url: '/system/clients'
}] as MenuDto[]
