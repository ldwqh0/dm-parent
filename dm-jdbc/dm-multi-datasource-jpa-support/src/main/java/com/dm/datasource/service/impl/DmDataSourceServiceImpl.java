package com.dm.datasource.service.impl;

import com.dm.common.exception.DataNotExistException;
import com.dm.datasource.converter.DmDataSourceConverter;
import com.dm.datasource.dto.DmDataSourceDto;
import com.dm.datasource.entity.DmDataSource;
import com.dm.datasource.entity.QDmDataSource;
import com.dm.datasource.mulit.DataSourceHolder;
import com.dm.datasource.provider.DataSourceProviderHolder;
import com.dm.datasource.repository.DmDataSourceRepository;
import com.dm.datasource.service.DmDataSourceService;
import com.dm.jdbc.ConnectionUtils;
import com.dm.jdbc.TableMeta;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

@Service
public class DmDataSourceServiceImpl implements DmDataSourceService {

    private final DmDataSourceRepository dmDataSourceRepository;
    private final QDmDataSource qDataSource = QDmDataSource.dmDataSource;

    private DataSourceHolder dataSourceHolder;

    @Autowired(required = false)
    public void setDataSourceHolder(DataSourceHolder dataSourceHolder) {
        this.dataSourceHolder = dataSourceHolder;
    }

    public DmDataSourceServiceImpl(
        DmDataSourceRepository dataSourceRepository) {
        this.dmDataSourceRepository = dataSourceRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DmDataSource save(DmDataSourceDto connection) {
        return dmDataSourceRepository.save(copyProperties(new DmDataSource(), connection));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DmDataSource update(Long id, DmDataSourceDto connection) {
        return dmDataSourceRepository.findById(id).map(cnn -> {
            closeConnection(cnn);
            DmDataSource result = copyProperties(cnn, connection);
            result.checkVersion(connection.getVersion());
            return dmDataSourceRepository.saveAndFlush(result);
        }).orElseThrow(DataNotExistException::new);
    }

    @Override
    public Optional<DmDataSource> findById(Long id) {
        return dmDataSourceRepository.findById(id);
    }

    @Override
    public Page<DmDataSource> list(String keyword, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            query.and(qDataSource.name.containsIgnoreCase(keyword)
                .or(qDataSource.host.containsIgnoreCase(keyword))
                .or(qDataSource.username.containsIgnoreCase(keyword)));
        }
        return dmDataSourceRepository.findAll(query, pageable);
    }

    @Override
    public List<DmDataSource> listAll() {
        return dmDataSourceRepository.findAll();
    }

    @Override
    public List<TableMeta> listTables(Long connection) {
        return dmDataSourceRepository.findById(connection)
            .map(DmDataSourceConverter::toDataSourceProperties)
            .map(properties -> {
                try (Connection cnn = DataSourceProviderHolder.getConnection(properties)) {
                    return ConnectionUtils.listTables(cnn);
                } catch (Exception throwable) {
                    throw new RuntimeException(throwable);
                }
            }).orElseThrow(DataNotExistException::new);
    }

    /**
     * 将连接从池中移除,并关闭连接池
     *
     * @param dataSource 要移除的数据源
     */
    private void closeConnection(DmDataSource dataSource) {
        if (this.dataSourceHolder != null) {
            dataSourceHolder.closeAndRemove(DmDataSourceConverter.toDataSourceProperties(dataSource));
        }
    }


    private DmDataSource copyProperties(DmDataSource model, DmDataSourceDto source) {
        model.setName(source.getName());
        model.setDatabase(source.getDatabase());
        model.setDbType(source.getDbType());
        model.setHost(source.getHost());
        model.setPassword(source.getPassword());
        model.setPort(source.getPort());
        model.setProperties(source.getProperties());
        model.setRemark(source.getRemark());
        model.setUsername(source.getUsername());
        return model;
    }
}
