package com.dm.jdbc;

import com.dm.collections.Lists;

import java.sql.*;
import java.util.List;

public final class ConnectionUtils {
    private ConnectionUtils() {
    }

    public static CheckResult checkState(String url, String driverClass, String username, String password) {
        try (Connection cnn_ = createConnection(url, driverClass, username, password)) {
            // 尝试打开和关闭一次连接，以便确认连接的正确性
            Statement statement = cnn_.createStatement();
            statement.close();
            return CheckResult.success();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return CheckResult.failure(e);
        }
    }

    public static List<TableMeta> listTables(Connection connection) throws SQLException {
        List<TableMeta> tableList = Lists.arrayList();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet metaResult = metaData.getTables(connection.getCatalog(), null, null,
            new String[]{"TABLE"});
        while (metaResult.next()) {
            tableList.add(TableMeta.of(metaResult.getString("TABLE_NAME"), metaResult.getString("TABLE_TYPE"), null));
        }
        return tableList;
    }

    public static Connection createConnection(String url, String driverClass, String username, String password) throws ClassNotFoundException, SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
