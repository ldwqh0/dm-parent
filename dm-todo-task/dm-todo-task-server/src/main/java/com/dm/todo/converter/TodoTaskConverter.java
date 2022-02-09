package com.dm.todo.converter;

import com.dm.collections.Sets;
import com.dm.todo.domain.Attribute;
import com.dm.todo.domain.TodoTaskResponse;
import com.dm.todo.entity.TodoTask;

public final class TodoTaskConverter {

    private TodoTaskConverter() {
    }

    public static TodoTaskResponse toDto(TodoTask model) {
        return new TodoTaskResponse(
            model.getId(),
            model.getUserid(),
            model.getTitle(),
            model.getCreatedTime(),
            model.getSourceName(),
            model.getBizId(),
            Sets.transform(model.getAttributes(), it -> new Attribute(it.getTitle(), it.getContent())),
            model.getUrl(),
            model.getPcUrl(),
            model.getCompletedTime()
        );
    }


}
