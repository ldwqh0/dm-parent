package com.dm.springboot.autoconfigure.common;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration
@ConditionalOnClass(JPAQueryFactory.class)
public class QueryDslConfiguration {

	/**
	 * 可以在项目中直接注入JPAQueryFactory
	 * 
	 * @param em
	 * @return
	 */
	@ConditionalOnMissingBean(JPAQueryFactory.class)
	@Bean
	@Lazy
	public JPAQueryFactory queryFactory(@Autowired EntityManager em) {
		return new JPAQueryFactory(em);
	}
}
