package com.dm.data.domain;

import com.dm.common.exception.DataValidateException;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", pkColumnName = "table_name_", table = "auto_pk_support_", valueColumnName = "next_id_")
    @Column(name = "id_", updatable = false)
    private Long id;

    @Version
    @Column(name = "version_", nullable = false)
    private Long version = 0L;

    @Override
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    @Override
    @Transient
    public boolean isNew() {
        // 子类可能复写id,所以通过判断 getId()能否获取到数据决定是否新对象，而不是通过本类中的id属性判断是否新对象
        return Objects.isNull(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    public Long getVersion() {
        return version;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }

    /**
     * <p>
     * 对实体进行版本检查
     * </p>
     * 1.在某些情况下，用户A打开页面对数据data进行操作，并将数据停留在页面上。<br>
     * 2.用户B打开页面,对data进行修改<br>
     * 3.用户A提交数据<br>
     * 4.用户B提交数据<br>
     * 当用户A提交数据后，如果用户B尝试提交数据，用户B实际上修改的数据是用户A修改之前的版本，而不是数据最新的版本，因为它没有感知到用户A对数据的提交<br>
     * hibernate的默认乐观锁实现仅仅在事务并发时才会生效。而我们需要的业务可能线程并没有并发，仅仅是人的并行编辑的动作，因此需要手动的对version进行检查<br>
     * 这个检查不是必须的，由各个实体根据自身的业务需求而定
     *
     * @param version 待检查的传入的版本号
     */
    public void checkVersion(Long version) {
        if (!Objects.equals(this.version, version)) {
            throw new DataValidateException("该资源在其它地方被修改，请刷新后重试");
        }
    }
}
