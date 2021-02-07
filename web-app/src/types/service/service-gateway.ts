/* tslint:disable */
/* eslint-disable */

// Generated using typescript-generator version 2.28.785 on 2021-02-05 17:18:32.

export interface MenuAuthorityDto extends Serializable {
  roleId?: number;
  roleName?: string;
  authorityMenus?: MenuDto[];
}

export interface MenuDto extends Serializable, IdentifiableDto<number> {
  id?: number;
  name?: string;
  title?: string;
  enabled?: boolean;
  url?: string;
  icon?: string;
  description?: string;
  type?: MenuType;
  parent?: MenuDto;
  openInNewWindow?: boolean;
}

export interface OrderDto extends Serializable {
  position?: Position;
}

export interface ResourceAuthorityDto extends Serializable {
  roleId?: number;
  roleName?: string;
  resourceAuthorities?: { [index: string]: ResourceOperation };
}

export interface ResourceDto extends Serializable {
  id?: number;
  name?: string;
  matcher?: string;
  description?: string;
  scope?: string[];
  matchType?: MatchType;
}

export interface RoleDto extends IdentifiableDto<number>, Serializable {
  id?: number;
  name?: string;
  description?: string;
  state?: Status;
  group?: string;
  fullname?: string;
}

export interface Serializable {
}

export interface ResourceOperation extends Serializable {
  readable?: boolean;
  saveable?: boolean;
  updateable?: boolean;
  deleteable?: boolean;
  patchable?: boolean;
}

export interface IdentifiableDto<ID> extends Serializable {
  id?: ID;
}

export const enum MenuType {
  COMPONENT = 'COMPONENT',
  HYPERLINK = 'HYPERLINK',
}

export const enum Position {
  UP = 'UP',
  DOWN = 'DOWN',
}

export const enum MatchType {
  ANT_PATH = 'ANT_PATH',
  REGEXP = 'REGEXP',
}

export const enum Status {
  ENABLED = 'ENABLED',
  DISABLED = 'DISABLED',
}
