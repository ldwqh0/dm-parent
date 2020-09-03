package com.dm.datasource.service;

import com.dm.datasource.dto.DmDataSourceDto;
import com.dm.datasource.entity.DmDataSource;
import com.dm.jdbc.TableMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DmDataSourceService {
    public DmDataSource save(DmDataSourceDto connection);

    public DmDataSource update(Long id, DmDataSourceDto connection);

    public Optional<DmDataSource> findById(Long id);

    public Page<DmDataSource> list(String keyword, Pageable pageable);

    public List<DmDataSource> listAll();

    public List<TableMeta> listTables(Long connection);
}
