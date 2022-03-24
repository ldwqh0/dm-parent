/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.32.889 on 2022-03-24 18:22:42.

export interface AuditableDto<UID, UNAME> extends Serializable {
    createBy?: Audit<UID, UNAME>;
    lastModifiedBy?: Audit<UID, UNAME>;
    createdTime?: Date|string;
    lastModifiedTime?: Date|string;
}

export interface IdentifiableDto<ID> extends Serializable {
    id?: ID;
}

export interface ValidationResult extends Serializable {
    success?: boolean;
    message?: string;
}

export interface Page<T> extends Slice<T> {
    totalElements?: number;
    totalPages?: number;
}

export interface Audit<ID, NAME> {
    username?: NAME;
    userid?: ID;
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

export const enum Direction {
    ASC = "ASC",
    DESC = "DESC",
}

export const enum NullHandling {
    NATIVE = "NATIVE",
    NULLS_FIRST = "NULLS_FIRST",
    NULLS_LAST = "NULLS_LAST",
}
