package com.dm.todo.service;

import com.dm.data.domain.RangePage;
import com.dm.data.domain.RangePageable;
import com.dm.todo.domain.TodoTaskActionRequest;
import com.dm.todo.domain.TodoTaskActionResponse;
import com.dm.todo.domain.TodoTaskRequest;
import com.dm.todo.domain.TodoTaskResponse;

import java.util.List;

public interface TodoTaskService {
    /**
     * 保存一个待办事项
     *
     * @param todoTask 要保存的事项
     */
    TodoTaskResponse save(TodoTaskRequest todoTask);

    /**
     * 根据id获取待办事项
     *
     * @param id 要获取的待办事项的id
     */
    TodoTaskResponse findById(long id);

    /**
     * 删除待办事项
     *
     * @param id 要删除的事项的id
     */
    void deleteById(long id);

    TodoTaskActionResponse addAction(long taskId, TodoTaskActionRequest request);

    default TodoTaskActionResponse addAction(long taskId, boolean completed, String content) {
        return addAction(taskId, new TodoTaskActionRequest(completed, content));
    }

    /**
     * 完成一个待办事项
     *
     * @param taskId  事项id
     * @param content 内容
     */
    TodoTaskResponse completeTask(long taskId, String content);

    default TodoTaskResponse completeTask(long taskId) {
        return completeTask(taskId, "");
    }

    /**
     * 查看所有待办事项
     *
     * @param pageable 分页信息
     */
    RangePage<TodoTaskResponse, Long> find(RangePageable<Long> pageable);

    /**
     * 查找指定用户的待办事项
     *
     * @param userid   要查找的用户的id
     * @param pageable 分页信息
     */
    RangePage<TodoTaskResponse, Long> find(long userid, boolean completed, RangePageable<Long> pageable);

    /**
     * 获取某个待办事项的所有处理动作列表
     *
     * @param taskId 待办事项的id
     * @return 待办事项处理动作的列表
     */
    List<TodoTaskActionResponse> listActions(long taskId);
}
