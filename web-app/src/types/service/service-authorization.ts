/* tslint:disable */
/* eslint-disable */

// Generated using typescript-generator version 2.28.785 on 2021-02-05 17:15:29.

export interface DepartmentDto extends Serializable {
  id?: number;
  fullname?: string;
  shortname?: string;
  description?: string;
  type?: Types;
  parent?: DepartmentDto;
  readonly hasChildren?: boolean
}

export interface LoginLogDto extends Serializable {
  id?: number;
  loginName?: string;
  ip?: string;
  type?: string;
  result?: string;
  time?: Date | string;
}

export interface OrderDto {
  position?: Position;
}

export interface UpdatePasswordDto {
  oldPassword?: string;
  password?: string;
  repassword?: string;
}

export interface UserDto extends Serializable, IdentifiableDto<number> {
  id?: number;
  username?: string;
  fullname?: string;
  enabled?: boolean;
  email?: string;
  mobile?: string;
  description?: string;
  roles?: Role[];
  scenicName?: string;
  regionCode?: string;
  posts?: UserPostDto[];
  password?: string;
}

export interface UserPostDto extends Serializable {
  department?: DepartmentDto;
  post?: string;
}

export interface ValidationResult extends Serializable {
  result?: Result;
  message?: string;
}

export interface Serializable {
}

export interface Role extends GrantedAuthority, Serializable {
  id?: number;
  group?: string;
  name?: string;
}

export interface GrantedAuthority extends Serializable {
  authority?: string;
}

export interface IdentifiableDto<ID> extends Serializable {
  id?: ID;
}

export const enum Types {
  ORGANS = 'ORGANS',
  DEPARTMENT = 'DEPARTMENT',
  GROUP = 'GROUP',
}

export const enum Position {
  UP = 'UP',
  DOWN = 'DOWN',
}

export const enum Result {
  success = 'success',
  failure = 'failure',
}
