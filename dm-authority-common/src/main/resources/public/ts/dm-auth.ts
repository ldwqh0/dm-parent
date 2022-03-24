/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.32.889 on 2022-03-24 18:23:13.

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
    order?: number;
    parent?: MenuDto;
    openInNewWindow?: boolean;
    childrenCount?: number;
    hasChildren?: boolean;
}

export interface MenuTreeDto extends Serializable, IdentifiableDto<number> {
    id?: number;
    name?: string;
    title?: string;
    enabled?: boolean;
    url?: string;
    icon?: string;
    description?: string;
    type?: MenuType;
    openInNewWindow?: boolean;
    children?: MenuTreeDto[];
}

export interface OrderDto extends Serializable {
    position?: Position;
}

export interface ResourceDto extends Serializable, IdentifiableDto<number> {
    id?: number;
    name?: string;
    methods?: HttpMethod[];
    matcher?: string;
    description?: string;
    scope?: string[];
    matchType?: MatchType;
    accessAuthorities?: RoleDto[];
    denyAuthorities?: RoleDto[];
}

export interface RoleDto extends IdentifiableDto<number>, Serializable {
    id?: number;
    name?: string;
    description?: string;
    state?: Status;
    group?: string;
    fullName?: string;
}

export interface Page<T> extends Slice<T> {
    totalElements?: number;
    totalPages?: number;
}

export interface Serializable {
}

export interface Sort extends Streamable<Order>, Serializable {
    unsorted?: boolean;
    sorted?: boolean;
}

export interface Pageable {
    offset?: number;
    sort?: Sort;
    paged?: boolean;
    pageNumber?: number;
    unpaged?: boolean;
    pageSize?: number;
}

export interface IdentifiableDto<ID> extends Serializable {
    id?: ID;
}

export interface Slice<T> extends Streamable<T> {
    size?: number;
    content?: T[];
    number?: number;
    sort?: Sort;
    first?: boolean;
    pageable?: Pageable;
    last?: boolean;
    numberOfElements?: number;
}

export interface Streamable<T> extends Iterable<T>, Supplier<Stream<T>> {
    empty?: boolean;
}

export interface Order extends Serializable {
    direction?: Direction;
    property?: string;
    ignoreCase?: boolean;
    nullHandling?: NullHandling;
    ascending?: boolean;
    descending?: boolean;
}

export interface Iterable<T> {
}

export interface Supplier<T> {
}

export interface Stream<T> extends BaseStream<T, Stream<T>> {
}

export interface BaseStream<T, S> extends AutoCloseable {
    parallel?: boolean;
}

export interface AutoCloseable {
}

export const enum MenuType {
    COMPONENT = "COMPONENT",
    HYPERLINK = "HYPERLINK",
    SUBMENU = "SUBMENU",
}

export const enum Position {
    UP = "UP",
    DOWN = "DOWN",
}

export const enum HttpMethod {
    GET = "GET",
    HEAD = "HEAD",
    POST = "POST",
    PUT = "PUT",
    PATCH = "PATCH",
    DELETE = "DELETE",
    OPTIONS = "OPTIONS",
    TRACE = "TRACE",
}

export const enum MatchType {
    ANT_PATH = "ANT_PATH",
    REGEXP = "REGEXP",
}

export const enum Status {
    ENABLED = "ENABLED",
    DISABLED = "DISABLED",
}

export const enum Direction {
    ASC = "ASC",
    DESC = "DESC",
}

export const enum NullHandling {
    NATIVE = "NATIVE",
    NULLS_FIRST = "NULLS_FIRST",
    NULLS_LAST = "NULLS_LAST",
}
