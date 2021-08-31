/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.32.889 on 2021-08-31 15:51:34.

export interface AuditableDto extends Serializable {
    createBy?: Audit<number, string>;
    lastModifiedBy?: Audit<number, string>;
    createdTime?: Date|string;
    lastModifiedTime?: Date|string;
}

export interface IdentifiableDto<ID> extends Serializable {
    id?: ID;
}

export interface RangePage<T, M> extends Page<T> {
    max?: M;
}

export interface RangePageImpl<T, M> extends PageImpl<T>, RangePage<T, M> {
}

export interface ValidationResult extends Serializable {
    success?: boolean;
    message?: string;
}

export interface Page<T> extends Slice<T> {
    totalPages?: number;
    totalElements?: number;
}

export interface Audit<ID, NAME> {
    username?: NAME;
    userid?: ID;
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

export interface PageImpl<T> extends Chunk<T>, Page<T> {
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

export interface Chunk<T> extends Slice<T>, Serializable {
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
