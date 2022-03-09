package com.dm.hibernate.dialect;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.unique.DefaultUniqueDelegate;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.UniqueKey;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 一个基于sqlserver 2008的{@link org.hibernate.dialect.unique.UniqueDelegate}。<br>
 * 在 sql server 中，创建唯一键和唯一索引是有区别的。<br>
 * 唯一键要求整个表中最多一个null列，如果多个null值，会被判断为重复值（mysql中的null值不会参与比较）,而唯一索引则可以通过where过滤来确定对哪些数据进行索引。<br>
 * 且创建键的语句是 ALTER TABLE [table] ADD CONSTRAINT [name] UNIQUE NONCLUSTERED.<br>
 * 创建唯一键索引的命令是 CREATE UNIQUE INDEX [name] ON [table] ( [column] ASC , [column] DESC) where (condition) <br>
 */
public class SqlServer2008UniqueDelegate extends DefaultUniqueDelegate {
    public SqlServer2008UniqueDelegate(Dialect dialect) {
        super(dialect);
    }

    @Override
    public String getAlterTableToAddUniqueKeyCommand(UniqueKey uniqueKey, Metadata metadata, SqlStringGenerationContext context) {
        List<Column> nullableColumns = getNullableColumns(uniqueKey);
        if (nullableColumns.size() > 0) {
            final String tableName = context.format(uniqueKey.getTable().getQualifiedTableName());
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
            return super.getAlterTableToAddUniqueKeyCommand(uniqueKey, metadata, context);
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

