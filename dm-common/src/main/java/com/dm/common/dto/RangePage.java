package com.dm.common.dto;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * 有限范围内的分页
 * 
 * @author ldwqh0@outlook.com
 *
 * @param <T>
 */
public interface RangePage<T> extends Page<T> {
    /**
     * 范围的上线
     * 
     * @return
     */
    public long getMax();

    public <U> RangePage<U> map(Function<? super T, ? extends U> converter);

    public static <T> RangePage<T> of(long max, Page<T> page) {
        return new RangePageImpl<T>(max, page.getContent(), page.getPageable(), page.getTotalElements());
    }
}

class RangePageImpl<T> extends PageImpl<T> implements RangePage<T> {

    private static final long serialVersionUID = -2418518649668788225L;

    private long max;

    public RangePageImpl(long max, List<T> content) {
        super(content);
        this.max = max;
    }

    RangePageImpl(long max, List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
        this.max = max;
    }

    @Override
    public long getMax() {
        return this.max;
    }

    @Override
    public <U> RangePage<U> map(Function<? super T, ? extends U> converter) {
        return new RangePageImpl<U>(max, getConvertedContent(converter), getPageable(), getTotalElements());
    }
}