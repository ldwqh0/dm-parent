package com.dm.hibernate.dialect;

import org.hibernate.boot.Metadata;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.unique.DefaultUniqueDelegate;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.UniqueKey;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlServer2008UniqueDelegate extends DefaultUniqueDelegate {
    public SqlServer2008UniqueDelegate(Dialect dialect) {
        super(dialect);
    }

    @Override
    public String getAlterTableToAddUniqueKeyCommand(UniqueKey uniqueKey, Metadata metadata) {
        List<Column> nullableColumns = getNullableColumns(uniqueKey);
        if (nullableColumns.size() > 0) {
            JdbcEnvironment jdbcEnvironment = metadata.getDatabase().getJdbcEnvironment();
            final String tableName = jdbcEnvironment.getQualifiedObjectNameFormatter().format(
                uniqueKey.getTable().getQualifiedTableName(),
                dialect
            );
            final String constraintName = dialect.quote(uniqueKey.getName());
            StringBuilder buf = new StringBuilder("create unique index ")
                .append(constraintName)
                .append(" on ")
                .append(tableName)
                .append(" (");
            addColumns(uniqueKey, buf);
            buf.append(")");
            buf.append("where (");
            addWhereColumns(uniqueKey, buf);
            buf.append(")");
            return buf.toString();
        } else {
            return super.getAlterTableToAddUniqueKeyCommand(uniqueKey, metadata);
        }
    }

    private void addColumns(UniqueKey index, StringBuilder buf) {
        final Iterator<Column> columnItr = index.getColumnIterator();
        final Map<Column, String> columnOrderMap = index.getColumnOrderMap();
        boolean first = true;
        while (columnItr.hasNext()) {
            final Column column = columnItr.next();
            if (first) {
                first = false;
            } else {
                buf.append(", ");
            }
            buf.append((column.getQuotedName(dialect)));
            if (columnOrderMap.containsKey(column)) {
                buf.append(" ").append(columnOrderMap.get(column));
            }
        }
    }

    private void addWhereColumns(UniqueKey index, StringBuilder buf) {
        final Iterator<Column> columnItr = index.getColumnIterator();
        boolean first = true;
        while (columnItr.hasNext()) {
            final Column column = columnItr.next();
            if (first) {
                first = false;
            } else {
                buf.append(" and ");
            }
            buf.append((column.getQuotedName(dialect))).append(" is not null");
        }
    }

    private List<Column> getNullableColumns(UniqueKey uniqueKey) {
        final Table table = uniqueKey.getTable();
        return uniqueKey.getColumns().stream()
            .map(table::getColumn)
            .filter(Column::isNullable)
            .collect(Collectors.toList());
    }
}

