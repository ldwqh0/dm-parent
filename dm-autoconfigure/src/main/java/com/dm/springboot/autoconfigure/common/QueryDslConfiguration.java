package com.dm.springboot.autoconfigure.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.persistence.EntityManager;

@Configuration
@ConditionalOnClass(JPAQueryFactory.class)
public class QueryDslConfiguration {

    /**
     * 可以在项目中直接注入JPAQueryFactory
     *
     * @param em an instance of {@link EntityManager}
     * @return {@link JPAQueryFactory}
     * @see JPAQueryFactory
     * @see EntityManager
     */
    @ConditionalOnMissingBean(JPAQueryFactory.class)
    @Bean
    @Lazy
    public JPAQueryFactory queryFactory(@Autowired EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
