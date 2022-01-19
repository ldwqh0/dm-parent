package com.dm.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.Objects;

/**
 * 表示移动操作的移动方式
 *
 * @author LiDong
 */
@JsonInclude(Include.NON_ABSENT)
public class OrderDto implements Serializable {
    /**
     * 移动的顺序
     *
     * @author LiDong
     */
    public enum Position {
        UP, DOWN
    }

    private Position position;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    }

    public OrderDto(Position position) {
        this.position = position;
    }
}
