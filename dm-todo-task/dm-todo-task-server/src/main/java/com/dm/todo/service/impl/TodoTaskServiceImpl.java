package com.dm.todo.service.impl;

import com.dm.collections.CollectionUtils;
import com.dm.collections.Lists;
import com.dm.collections.Sets;
import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import com.dm.data.domain.RangePage;
import com.dm.data.domain.RangePageable;
import com.dm.todo.converter.TodoActionConverter;
import com.dm.todo.converter.TodoTaskConverter;
import com.dm.todo.domain.TodoTaskActionRequest;
import com.dm.todo.domain.TodoTaskActionResponse;
import com.dm.todo.domain.TodoTaskRequest;
import com.dm.todo.domain.TodoTaskResponse;
import com.dm.todo.entity.Attribute;
import com.dm.todo.entity.QTodoTask;
import com.dm.todo.entity.TodoTask;
import com.dm.todo.entity.TodoTaskAction;
import com.dm.todo.repository.TodoTaskRepository;
import com.dm.todo.service.TodoTaskService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class TodoTaskServiceImpl implements TodoTaskService {

    private final TodoTaskRepository todoTaskRepository;

    private final QTodoTask qTodoTask = QTodoTask.todoTask;

    public TodoTaskServiceImpl(TodoTaskRepository todoTaskRepository) {
        this.todoTaskRepository = todoTaskRepository;
    }

    @Override
    @Transactional
    public TodoTaskResponse save(TodoTaskRequest todoTask) {
        TodoTask result = todoTaskRepository.save(copyProperties(new TodoTask(), todoTask));
        return TodoTaskConverter.toDto(result);
    }

    @Override
    @Transactional(readOnly = true)
    public TodoTaskResponse findById(long id) {
        return todoTaskRepository.findById(id)
            .map(TodoTaskConverter::toDto)
            .orElseThrow(DataNotExistException::new);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        todoTaskRepository.deleteById(id);
    }


    /**
     * 给待办事项添加一个处理动作
     *
     * @param input 修改任务状态的动作
     */
    @Override
    @Transactional
    public TodoTaskActionResponse addAction(long taskId, TodoTaskActionRequest input) {
        TodoTask task = todoTaskRepository.getById(taskId);
        if (task.isCompleted()) {
            throw new DataValidateException("已经完成的待办事项不支持添加动作");
        }
        TodoTaskAction action = task.addAction(input.getContent(), input.isCompleted());
        if (input.isCompleted()) {
            task.setCompletedTime(action.getCreatedTime());
        }
        return TodoActionConverter.toDto(taskId, action);
    }

    @Override
    public TodoTaskResponse completeTask(long taskId, String content) {
        TodoTask task = todoTaskRepository.getById(taskId);
        if (task.isCompleted()) {
            throw new DataValidateException("已经完成的待办事项不支持添加动作");
        }
        TodoTaskAction action = task.addAction(content, true);
        task.setCompletedTime(action.getCreatedTime());
        return TodoTaskConverter.toDto(todoTaskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public RangePage<TodoTaskResponse, Long> find(RangePageable<Long> pageable) {
        Long max = pageable.getMax().or(todoTaskRepository::findMaxId).orElse(0L);
        Page<TodoTask> result = todoTaskRepository.findAll(pageable);
        return RangePage.of(max, result).map(TodoTaskConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public RangePage<TodoTaskResponse, Long> find(long userid, boolean completed, RangePageable<Long> pageable) {
        Long max = pageable.getMax().or(todoTaskRepository::findMaxId).orElse(0L);
        BooleanBuilder query = new BooleanBuilder(qTodoTask.userid.eq(userid));
        if (completed) {
            query.and(qTodoTask.completedTime.isNotNull());
        } else {
            query.and(qTodoTask.completedTime.isNull());
        }
        Page<TodoTask> result = todoTaskRepository.findAll(query, pageable);
        return RangePage.of(max, result).map(TodoTaskConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoTaskActionResponse> listActions(long taskId) {
        return Lists.transform(todoTaskRepository.getById(taskId).getActions(), it -> TodoActionConverter.toDto(taskId, it));
    }

    private TodoTask copyProperties(TodoTask model, TodoTaskRequest source) {
        model.setUserid(source.getUserid());
        model.setTitle(source.getTitle());
        model.setSourceName(source.getSourceName());
        model.setBizId(source.getBizId());
        if (CollectionUtils.isNotEmpty(source.getAttributes())) {
            model.setAttributes(Sets.transform(source.getAttributes(), it -> new Attribute(it.getTitle(), it.getContent())));
        }
        model.setUrl(source.getUrl());
        model.setPcUrl(source.getPcUrl());
        return model;
    }
}
