import { MenuDto } from './service-gateway'
import { DepartmentDto } from './service-authorization'

export interface MenuTreeItem extends MenuDto {
  children?: MenuTreeItem[]
}

export interface DepartmentTreeItem extends DepartmentDto {
  children?: DepartmentTreeItem[]
}
