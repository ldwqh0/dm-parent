package com.dm.springboot.autoconfigure.todo;

import com.dm.springboot.autoconfigure.DmEntityScan;
import com.dm.todo.controller.TodoTaskActionController;
import com.dm.todo.controller.TodoTaskController;
import com.dm.todo.entity.TodoTask;
import com.dm.todo.repository.TodoTaskRepository;
import com.dm.todo.service.TodoTaskService;
import com.dm.todo.service.impl.TodoTaskServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;

@DmEntityScan("com.dm.todo")
@ConditionalOnClass(TodoTask.class)
public class DmTotoTaskConfiguration {

    @Bean
    public TodoTaskRepository todoTaskRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(TodoTaskRepository.class);
    }

    @Bean
    public TodoTaskService todoTaskService(TodoTaskRepository todoTaskRepository) {
        return new TodoTaskServiceImpl(todoTaskRepository);
    }

    @Bean
    public TodoTaskController todoTaskController(TodoTaskService todoTaskService) {
        return new TodoTaskController(todoTaskService);
    }

    @Bean
    public TodoTaskActionController todoActionController(TodoTaskService todoTaskService) {
        return new TodoTaskActionController(todoTaskService);
    }

}
