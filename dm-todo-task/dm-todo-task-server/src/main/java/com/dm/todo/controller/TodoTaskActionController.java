package com.dm.todo.controller;

import com.dm.todo.domain.TodoTaskActionRequest;
import com.dm.todo.domain.TodoTaskActionResponse;
import com.dm.todo.service.TodoTaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * 待办事项处理
 */
@RestController
@RequestMapping("todos/{taskId}/actions")
public class TodoTaskActionController {

    private final TodoTaskService taskService;

    public TodoTaskActionController(TodoTaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 给指定的待办事项新增一个处理动作
     *
     * @param taskId 事项的id
     * @param action 动作内容
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public TodoTaskActionResponse saveAction(@PathVariable("taskId") Long taskId, @RequestBody TodoTaskActionRequest action) {
        return taskService.addAction(taskId, action);
    }

    /**
     * 获取指定任务的处理过程
     *
     * @param taskId 要获取的待办事项的id
     * @return 处理流程的列表
     */
    @GetMapping
    public List<TodoTaskActionResponse> listActions(@PathVariable("taskId") Long taskId) {
        return taskService.listActions(taskId);
    }
}
