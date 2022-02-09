package com.dm.todo.repository;

import com.dm.todo.entity.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface TodoTaskRepository extends JpaRepository<TodoTask, Long>, QuerydslPredicateExecutor<TodoTask> {
    @Query("select max(task.id) from TodoTask task")
    Optional<Long> findMaxId();
}
