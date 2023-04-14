/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.32.889 on 2023-04-07 12:36:38.

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
    userid?: ID;
    username?: NAME;
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
    unpaged?: boolean;
    paged?: boolean;
    pageNumber?: number;
    pageSize?: number;
}

export interface Slice<T> extends Streamable<T> {
    size?: number;
    content?: T[];
    number?: number;
    sort?: Sort;
    first?: boolean;
    last?: boolean;
    numberOfElements?: number;
    pageable?: Pageable;
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
