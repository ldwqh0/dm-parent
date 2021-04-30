/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.28.785 on 2021-04-29 10:02:58.

export interface ClientDto {
    id?: string | null;
    name?: string;
    secret?: string;
    authorizedGrantTypes?: string[];
    scopes?: string[];
    accessTokenValiditySeconds?: number;
    refreshTokenValiditySeconds?: number;
    registeredRedirectUris?: string[];
    requirePkce?: boolean;
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
    time?: Date|string;
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
    birthDate?: Date|string;
    profilePhoto?: string;
    password?: string;
}

export interface UserPostDto extends Serializable {
    department?: DepartmentDto;
    post?: string;
}

export interface Serializable {
}

export interface Role extends GrantedAuthority, Serializable {
    id?: number;
    group?: string;
    name?: string;
}

export interface IdentifiableDto<ID> extends Serializable {
    id?: ID;
}

export interface GrantedAuthority extends Serializable {
    authority?: string;
}

export const enum Types {
    ORGANS = "ORGANS",
    DEPARTMENT = "DEPARTMENT",
    GROUP = "GROUP",
}

export const enum Position {
    UP = "UP",
    DOWN = "DOWN",
}
