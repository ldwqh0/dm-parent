package com.dm.data.domain;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Optional;

public interface RangePageable<T extends Serializable> extends Pageable {
    /**
     * 最大范围
     */
    Optional<T> getMax();
}

