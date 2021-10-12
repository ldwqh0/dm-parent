package com.dm.data.domain;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Optional;

public class RangePageRequest<T extends Serializable> extends PageRequest implements RangePageable<T> {

    private static final long serialVersionUID = -4809501621841038092L;

    final T max;

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param page zero-based page index, must not be negative.
     * @param size the size of the page to be returned, must be greater than 0.
     * @param sort must not be {@literal null}, use {@link Sort#unsorted()} instead.
     */
    protected RangePageRequest(int page, int size, Sort sort, T max) {
        super(page, size, sort);
        this.max = max;
    }

    @Override
    public Optional<T> getMax() {
        return Optional.ofNullable(max);
    }

    public static <T extends Serializable> RangePageable<T> of(int page, int size, Sort sort, T max) {
        return new RangePageRequest<>(page, size, sort, max);
    }

    public static <T extends Serializable> RangePageable<T> of(Pageable pageable, T max) {
        return new RangePageRequest<>(pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSort(),
            max
        );
    }
}
