//package com.dm.server.authorization.repository.impl
//
//import com.dm.server.authorization.entity.QClient
//import com.querydsl.jpa.impl.JPAQueryFactory
//import org.springframework.data.domain.Page
//import org.springframework.data.domain.PageImpl
//import org.springframework.data.domain.Pageable
//
//class ClientRepositoryImpl(private val queryFactory: JPAQueryFactory) {
//
//    private val qClient: QClient = QClient.client;
//
//    fun findScopes(keyword: String = "", pageable: Pageable): Page<String> {
//        var query = this.queryFactory.selectDistinct(qClient.scopes.any())
//            .from(qClient);
//        if (keyword.isNotBlank()) {
//            query = query.where(qClient.scopes.any().containsIgnoreCase(keyword))
//        }
//        val count = query.fetchCount();
//        query = query.offset(pageable.offset).limit(pageable.pageSize.toLong())
//        return PageImpl(query.fetch(), pageable, count);
//    }
//}
