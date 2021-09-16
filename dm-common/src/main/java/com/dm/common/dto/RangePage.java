package com.dm.common.dto;

import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * 有限范围内的分页
 *
 * @param <T> 数据类型
 * @param <M> 最大值的类型
 * @author ldwqh0@outlook.com
 */
public interface RangePage<T, M extends Comparable<?> & Serializable> extends Page<T> {
    /**
     * 范围的上限
     *
     * @return 返回上限信息
     */
    M getMax();

    @Override
    @NotNull <U> RangePage<U, M> map(@NotNull Function<? super T, ? extends U> converter);

    @NotNull
    static <T, M extends Comparable<?> & Serializable> RangePage<T, M> of(M max, @NotNull Page<T> page) {
        return new RangePageImpl<>(max, page.getContent(), page.getPageable(), page.getTotalElements());
    }
}

@EqualsAndHashCode(callSuper = false)
class RangePageImpl<T, M extends Comparable<?> & Serializable> extends PageImpl<T> implements RangePage<T, M> {

    private static final long serialVersionUID = -2418518649668788225L;

    private final M max;

    RangePageImpl(M max, List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
        this.max = max;
    }

    @Override
    public M getMax() {
        return this.max;
    }

    @Override
    public <R> RangePage<R, M> map(@NotNull Function<? super T, ? extends R> converter) {
        return new RangePageImpl<>(max, getConvertedContent(converter), getPageable(), getTotalElements());
    }

}
