/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.32.889 on 2021-08-31 15:52:07.

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
    fullname?: string;
}

export interface Page<T> extends Slice<T> {
    totalPages?: number;
    totalElements?: number;
}

export interface Serializable {
}

export interface Sort extends Streamable<Order>, Serializable {
    sorted?: boolean;
    unsorted?: boolean;
}

export interface Pageable {
    offset?: number;
    sort?: Sort;
    pageNumber?: number;
    paged?: boolean;
    pageSize?: number;
    unpaged?: boolean;
}

export interface IdentifiableDto<ID> extends Serializable {
    id?: ID;
}

export interface Slice<T> extends Streamable<T> {
    size?: number;
    content?: T[];
    number?: number;
    sort?: Sort;
    last?: boolean;
    first?: boolean;
    pageable?: Pageable;
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
