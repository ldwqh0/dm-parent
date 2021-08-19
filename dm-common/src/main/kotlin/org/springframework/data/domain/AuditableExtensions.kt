package org.springframework.data.domain

import java.time.temporal.TemporalAccessor

val <U, ID, T : TemporalAccessor> Auditable<U, ID, T>.createdUser: U?
    get() = createdBy.orElse(null)
val <U, ID, T : TemporalAccessor> Auditable<U, ID, T>.createdTime: T?
    get() = createdDate.orElse(null)
val <U, ID, T : TemporalAccessor>Auditable<U, ID, T>.lastModifiedUser: U?
    get() = lastModifiedBy.orElse(null)
val <U, ID, T : TemporalAccessor>Auditable<U, ID, T>.lastModifiedTime: T?
    get() = lastModifiedDate.orElse(null)
