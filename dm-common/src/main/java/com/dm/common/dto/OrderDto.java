package com.dm.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

/**
 * 表示移动操作的移动方式
 *
 * @author LiDong
 */
@JsonInclude( NON_ABSENT)
public class OrderDto implements Serializable {

    private static final long serialVersionUID = -4893753356546615887L;

    /**
     * 移动的顺序
     *
     * @author LiDong
     */
    public enum Position {
        UP, DOWN
    }

    private final Position position;

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return position == orderDto.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    public OrderDto() {
        this(Position.UP);
    }

    public OrderDto(Position position) {
        this.position = position;
    }
}
