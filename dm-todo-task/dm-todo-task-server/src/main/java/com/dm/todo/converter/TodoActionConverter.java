package com.dm.todo.converter;

import com.dm.todo.domain.TodoTaskActionResponse;
import com.dm.todo.entity.TodoTaskAction;

public class TodoActionConverter {
    private TodoActionConverter() {

    }

    public static TodoTaskActionResponse toDto(Long taskId, TodoTaskAction action) {
        return new TodoTaskActionResponse(
                taskId,
                action.getContent(),
                action.isCompleted(),
                action.getCreatedTime()
        );
    }
}
