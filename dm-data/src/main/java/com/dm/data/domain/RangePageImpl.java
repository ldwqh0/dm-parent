package com.dm.data.domain;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public class RangePageImpl<T, M extends Comparable<?> & Serializable> extends PageImpl<T> implements RangePage<T, M> {

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
