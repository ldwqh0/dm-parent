package com.dm.file.repository.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.file.entity.QFileInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class FileInfoRepositoryImpl {

    @Autowired
    private final JPAQueryFactory jqf;

    private final QFileInfo qFileInfo = QFileInfo.fileInfo;

    public FileInfoRepositoryImpl(@Autowired JPAQueryFactory queryFactory) {
        this.jqf = queryFactory;
    }

    public Optional<UUID> findMaxId() {
        return Optional.ofNullable(
                jqf.select(qFileInfo.id).from(qFileInfo)
                        .orderBy(qFileInfo.id.desc())
                        .fetchFirst());
    }
}
