package com.dm.datasource.controller;

import com.dm.collections.Lists;
import com.dm.common.exception.DataNotExistException;
import com.dm.datasource.converter.DmDataSourceConverter;
import com.dm.datasource.dto.DmDataSourceDto;
import com.dm.datasource.provider.DataSourceProperties;
import com.dm.datasource.provider.DataSourceProvider;
import com.dm.datasource.provider.DataSourceProviderHolder;
import com.dm.datasource.service.DmDataSourceService;
import com.dm.jdbc.ConnectionUtils;
import com.dm.jdbc.TableMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping({ "/connections", "/dataSources" })
public class DmDataSourceController {

    private DmDataSourceService cnnService;
    private DmDataSourceConverter cnnConverter;

    @Autowired
    public void setDataSourceConverter(DmDataSourceConverter cnnConverter) {
        this.cnnConverter = cnnConverter;
    }

    @Autowired
    public void setDataSourceService(DmDataSourceService cnnService) {
        this.cnnService = cnnService;
    }

    /**
     * 新增一条连接信息
     *
     * @param connection
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DmDataSourceDto save(@Validated(Default.class) @RequestBody DmDataSourceDto connection) {
        return cnnConverter.toDto(cnnService.save(connection));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public DmDataSourceDto update(@PathVariable("id") Long id,
            @RequestBody @Validated(Default.class) DmDataSourceDto connection) {
        return cnnConverter.toDto(cnnService.update(id, connection));
    }

    @GetMapping("{id}")
    public DmDataSourceDto get(@PathVariable("id") Long id) {
        return cnnService.findById(id).map(cnnConverter::toDto).orElseThrow(DataNotExistException::new);
    }

    @GetMapping(params = { "page", "size" })
    public Page<DmDataSourceDto> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault Pageable pageable) {
        return cnnService.list(keyword, pageable).map(cnnConverter::toSimpleDto);
    }

    @GetMapping(params = { "!page", "!size" })
    public List<DmDataSourceDto> list() {
        return Lists.transform(cnnService.listAll(), cnnConverter::toDto);
    }

    @GetMapping("{cnn}/state")
    public Boolean state(@PathVariable("cnn") Long id) throws SQLException {
        return cnnService.findById(id)
                .map(cnnConverter::toDataSourceProperties)
                .map(this::checkState)
                .orElseThrow(DataNotExistException::new);
    }

    @PostMapping(value = "state")
    public Boolean state(@RequestBody DmDataSourceDto cnn) throws SQLException {
        return checkState(cnnConverter.toDataSourceProperties(cnn));
    }

    private boolean checkState(DataSourceProperties properties) {
        DataSourceProvider provider = DataSourceProviderHolder.getProvider(properties.getDbType());
        return ConnectionUtils.checkState(
                provider.getUrl(properties),
                provider.getDriverClassName(),
                properties.getUsername(),
                properties.getPassword());
    }

    @GetMapping("{connection}/tables")
    public List<TableMeta> listTables(@PathVariable("connection") Long connection) {
        return cnnService.listTables(connection);
    }
}
