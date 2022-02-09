package com.dm.todo.controller;

import com.dm.data.domain.RangePage;
import com.dm.data.domain.RangePageable;
import com.dm.todo.domain.TodoTaskRequest;
import com.dm.todo.domain.TodoTaskResponse;
import com.dm.todo.service.TodoTaskService;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 待办事项
 */
@RestController
@RequestMapping("todos")
public class TodoTaskController {

    private final TodoTaskService todoTaskService;

    public TodoTaskController(TodoTaskService todoTaskService) {
        this.todoTaskService = todoTaskService;
    }

    /**
     * 新增一个待办事项
     *
     * @param task 待办事项请求
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoTaskResponse save(@RequestBody TodoTaskRequest task) {
        return todoTaskService.save(task);
    }

    /**
     * 获取一个待办事项的详情
     *
     * @param id 要获取的待办事项的id
     */
    @GetMapping("{id}")
    public TodoTaskResponse get(@PathVariable("id") Long id) {
        return todoTaskService.findById(id);
    }

    /**
     * 删除一个待办事项
     *
     * @param id 要删除的待办事项的id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        todoTaskService.deleteById(id);
    }

    /**
     * 获取所有的待办事项
     *
     * @param pageable 分页参数
     */
    @GetMapping(params = {"scope=all"})
    public RangePage<TodoTaskResponse, Long> find(@PageableDefault RangePageable<Long> pageable) {
        return todoTaskService.find(pageable);
    }

    /**
     * 获取我的待办事项
     *
     * @param userid    要获取的用户的id
     * @param completed 用于过滤待办事项是否完成
     * @param pageable  分页参数
     * @ignoreParams userid
     */
    @GetMapping
    public RangePage<TodoTaskResponse, Long> findMyTask(
        @AuthenticationPrincipal(expression = "id") long userid,
        @RequestParam(value = "completed", defaultValue = "false") Boolean completed,
        @PageableDefault RangePageable<Long> pageable) {
        return todoTaskService.find(userid, completed, pageable);
    }

}
