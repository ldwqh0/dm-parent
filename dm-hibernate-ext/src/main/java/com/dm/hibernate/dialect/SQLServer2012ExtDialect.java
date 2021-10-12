package com.dm.hibernate.dialect;

import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.dialect.unique.UniqueDelegate;

/**
 * 增强的Sqlserver方言，注意用于增强unique index的创建逻辑
 */
public class SQLServer2012ExtDialect extends SQLServer2012Dialect {

    private final UniqueDelegate uniqueDelegate = new SqlServer2008UniqueDelegate(this);

    @Override
    public UniqueDelegate getUniqueDelegate() {
        return uniqueDelegate;
    }
}
