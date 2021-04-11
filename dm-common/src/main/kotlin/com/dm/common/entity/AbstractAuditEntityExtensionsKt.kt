import com.dm.common.entity.AbstractAuditEntity
import com.dm.common.entity.Audit
import java.time.ZonedDateTime

val AbstractAuditEntity.createdUser: Audit<Long, String>?
    get() = createdBy.orElse(null)
val AbstractAuditEntity.createdTime: ZonedDateTime
    get() = createdDate.orElse(null)
val AbstractAuditEntity.lastModifiedUser: Audit<Long, String>?
    get() = lastModifiedBy.orElse(null)
val AbstractAuditEntity.lastModifiedTime: ZonedDateTime
    get() = lastModifiedDate.orElse(null)
