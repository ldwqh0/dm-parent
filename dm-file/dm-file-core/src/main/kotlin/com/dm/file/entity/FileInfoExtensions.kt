package com.dm.file.entity

import com.dm.common.entity.Audit
import java.time.ZonedDateTime

val FileInfo.createdUser: Audit<Long, String>?
    get() = createdBy.orElse(null)
val FileInfo.createdTime: ZonedDateTime
    get() = createdDate.get()
val FileInfo.lastModifiedUser: Audit<Long, String>?
    get() = lastModifiedBy.orElse(null)
val FileInfo.lastModifiedTime: ZonedDateTime
    get() = lastModifiedDate.get()
