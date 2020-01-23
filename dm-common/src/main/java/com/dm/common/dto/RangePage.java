package com.dm.common.dto;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import lombok.EqualsAndHashCode;

/**
 * 有限范围内的分页
 * 
 * @author ldwqh0@outlook.com
 *
 * @param <T> 数据类型
 * @param <M> 最大值的类型
 */
public interface RangePage<T, M> extends Page<T> {
    /**
     * 范围的上限
     * 
     * @return
     */
    public M getMax();

    @Override
    public <U> RangePage<U, M> map(Function<? super T, ? extends U> converter);

    public static <T, M> RangePage<T, M> of(M max, Page<T> page) {
        return new RangePageImpl<T, M>(max, page.getContent(), page.getPageable(), page.getTotalElements());
    }
}

@EqualsAndHashCode(callSuper = true)
class RangePageImpl<T, M> extends PageImpl<T> implements RangePage<T, M> {

    private static final long serialVersionUID = -2418518649668788225L;

    private M max;

    public RangePageImpl(M max, List<T> content) {
        super(content);
        this.max = max;
    }

    RangePageImpl(M max, List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
        this.max = max;
    }

    @Override
    public M getMax() {
        return this.max;
    }

    @Override
    public <R> RangePage<R, M> map(Function<? super T, ? extends R> converter) {
        return new RangePageImpl<R, M>(max, getConvertedContent(converter), getPageable(), getTotalElements());
    }

}