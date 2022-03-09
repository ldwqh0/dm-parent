package com.dm.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

/**
 * 表示移动操作的移动方式
 *
 * @author LiDong
 */
@JsonInclude(NON_ABSENT)
public class PositionRequest implements Serializable {

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
        PositionRequest that = (PositionRequest) o;
        return position == that.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    public PositionRequest() {
        this(Position.UP);
    }

    public PositionRequest(Position position) {
        this.position = position;
    }
}
