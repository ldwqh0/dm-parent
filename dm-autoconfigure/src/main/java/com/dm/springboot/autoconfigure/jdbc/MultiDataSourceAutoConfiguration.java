package com.dm.springboot.autoconfigure.jdbc;

import com.dm.datasource.mulit.AutoCreateRoutingDataSource;
import com.dm.datasource.provider.DataSourceProvider;
import com.dm.datasource.provider.DataSourceProviderHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.List;

import static com.dm.collections.Lists.arrayList;

@ConditionalOnBean(AutoCreateRoutingDataSource.class)
@Import(MultiDataSourceBeanDefine.class)
public class MultiDataSourceAutoConfiguration implements InitializingBean {

    private final List<DataSourceProvider> providers;

    public MultiDataSourceAutoConfiguration(List<DataSourceProvider> providers) {
        this.providers = arrayList(providers);
    }

    @Override
    public void afterPropertiesSet() {
        this.providers.forEach(DataSourceProviderHolder::registerProvider);
    }
}
