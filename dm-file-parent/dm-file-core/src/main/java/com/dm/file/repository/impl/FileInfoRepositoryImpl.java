package com.dm.file.repository.impl;

import com.dm.file.entity.QFileInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public class FileInfoRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    private final QFileInfo qFileInfo = QFileInfo.fileInfo;

    public FileInfoRepositoryImpl(@Autowired JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Transactional(readOnly = true)
    public Optional<UUID> findMaxId() {
        return Optional.ofNullable(
            queryFactory.select(qFileInfo.id).from(qFileInfo)
                .orderBy(qFileInfo.id.desc())
                .fetchFirst());
    }
}
