package com.dm.jdbc;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class TableMetaUtils {

    private TableMetaUtils() {
    }

    public static TableMeta getMeta(DataSource dataSource, @NotNull String tableName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return getTableMeta(connection, tableName);
        }
    }

    public static TableMeta getTableMeta(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columnsResultSet = metaData.getColumns(connection.getCatalog(), null, tableName, null);
        List<ColumnMeta> columns = new ArrayList<>();
        while (columnsResultSet.next()) {
            ColumnMeta column = new ColumnMeta(
                    columnsResultSet.getString("TABLE_CAT"), // 1
                    columnsResultSet.getString("TABLE_SCHEM"), // 2
                    columnsResultSet.getString("TABLE_NAME"), // 3
                    columnsResultSet.getString("COLUMN_NAME"), // 4
                    columnsResultSet.getInt("DATA_TYPE"), // 5
                    columnsResultSet.getString("TYPE_NAME"), // 6
                    columnsResultSet.getString("COLUMN_SIZE"), // 7
                    // this is not used
                    // columnsResultSet.getString("BUFFER_LENGTH"),//8
                    columnsResultSet.getInt("DECIMAL_DIGITS"), // 9
                    columnsResultSet.getInt("NUM_PREC_RADIX"), // 10
                    columnsResultSet.getInt("NULLABLE"), // 11
                    columnsResultSet.getString("REMARKS"), // 12
                    columnsResultSet.getString("COLUMN_DEF"), // 13
                    columnsResultSet.getInt("SQL_DATA_TYPE"), // 14
                    columnsResultSet.getInt("SQL_DATETIME_SUB"), // 15
                    columnsResultSet.getInt("CHAR_OCTET_LENGTH"), // 16
                    columnsResultSet.getInt("ORDINAL_POSITION"), // 17
                    columnsResultSet.getString("IS_NULLABLE"), // 18
                    columnsResultSet.getString("SCOPE_CATALOG"), // 19
                    columnsResultSet.getString("SCOPE_SCHEMA"), // 20
                    columnsResultSet.getString("SCOPE_TABLE"), // 21
                    columnsResultSet.getShort("SOURCE_DATA_TYPE"), // 22
                    columnsResultSet.getString("IS_AUTOINCREMENT"), // 23
                    columnsResultSet.getString("IS_GENERATEDCOLUMN")// 24
            );
            columns.add(column);
        }
        // TODO 这里待处理
        return TableMeta.of(tableName, "table", columns);
    }
}
