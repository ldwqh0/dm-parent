import { MenuDto } from './service-gateway'

export interface MenuTreeItem extends MenuDto {
  children?: MenuTreeItem[]
}
