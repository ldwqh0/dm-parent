package com.dm.springboot.autoconfigure.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@ConditionalOnBean(EntityManagerFactory.class)
@ConditionalOnClass(JPAQueryFactory.class)
@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
public class QueryDslConfiguration {

    /**
     * 可以在项目中直接注入JPAQueryFactory
     *
     * @param em an instance of {@link EntityManager}
     * @return {@link JPAQueryFactory}
     * @see JPAQueryFactory
     * @see EntityManager
     */
    @Bean
    @ConditionalOnMissingBean(JPAQueryFactory.class)
    public JPAQueryFactory queryFactory(@Autowired EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
