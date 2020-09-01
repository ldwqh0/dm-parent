package com.dm.jdbc;

import java.util.List;

public interface TableMeta {

    public String getTableName();

    public List<ColumnMeta> getColumns();


    static TableMeta of(String tableName, List<ColumnMeta> columns) {
        TableMetaImpl data = new TableMetaImpl();
        data.setTableName(tableName);
        data.setColumns(columns);
        return data;
    }

    static class TableMetaImpl implements TableMeta {

        private String tableName;

        private List<ColumnMeta> columns;

        private TableMetaImpl() {
        }

        @Override
        public String getTableName() {
            return tableName;
        }

        @Override
        public List<ColumnMeta> getColumns() {
            return columns;
        }

        void setTableName(String tableName) {
            this.tableName = tableName;
        }

        void setColumns(List<ColumnMeta> columns) {
            this.columns = columns;
        }
    }


}

