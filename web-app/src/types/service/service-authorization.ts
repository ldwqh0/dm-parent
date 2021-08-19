/* tslint:disable */
/* eslint-disable */

// Generated using typescript-generator version 2.28.785 on 2021-07-06 11:24:20.

export interface ClientDto {
  id?: string | null;
  name?: string;
  secret?: string;
  type?: ClientType;
  accessTokenValiditySeconds?: number;
  refreshTokenValiditySeconds?: number;
  autoApprove?: boolean;
  authorizedGrantTypes?: string[];
  scopes?: string[];
  registeredRedirectUris?: string[];
  enabled?: boolean;
  description?:string;
}

export interface DepartmentDto extends IdentifiableDto<number>, Serializable {
  id?: number;
  fullname?: string;
  shortname?: string;
  description?: string;
  type?: Types;
  hasChildren?: boolean;
  childrenCount?: number;
  userCount?: number;
  parent?: DepartmentDto;
  director?: UserDto;
  logo?: string;
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
  no?: string;
  givenName?: string;
  familyName?: string;
  middleName?: string;
  profile?: string;
  website?: string;
  gender?: Gender;
  username?: string;
  fullname?: string;
  enabled?: boolean;
  email?: string;
  emailVerified?: boolean;
  mobile?: string;
  phoneNumberVerified?: boolean;
  description?: string;
  roles?: Role[];
  scenicName?: string;
  regionCode?: string;
  posts?: UserPostDto[];
  birthDate?: Date | string;
  profilePhoto?: string;
  zoneinfo?: string;
  local?: string;
  address?: Address;
  password?: string;
}

export interface UserPostDto extends Serializable {
  department?: DepartmentDto;
  post?: string;
}

export interface Serializable {
}

export interface Role extends GrantedAuthority, Serializable, ManagedComposite, PersistentAttributeInterceptable {
  id?: number;
  group?: string;
  name?: string;
}

export interface Address extends Serializable, ManagedComposite, PersistentAttributeInterceptable {
  formatted?: string;
  streetAddress?: string;
  locality?: string;
  region?: string;
  postalCode?: string;
  country?: string;
}

export interface IdentifiableDto<ID> extends Serializable {
  id?: ID;
}

export interface GrantedAuthority extends Serializable {
  authority?: string;
}

export interface ManagedComposite extends Managed {
}

export interface PersistentAttributeInterceptable {
}

export interface Managed {
}

export const enum ClientType {
  CLIENT_PUBLIC = 'CLIENT_PUBLIC',
  CLIENT_CONFIDENTIAL = 'CLIENT_CONFIDENTIAL',
  CLIENT_RESOURCE = 'CLIENT_RESOURCE',
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

export const enum Gender {
  male = 'male',
  female = 'female',
}
