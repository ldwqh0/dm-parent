package com.dm.jdbc;

import java.util.List;

public interface TableMeta {

    public String getTableName();

    public String getType();

    public List<ColumnMeta> getColumns();


    static TableMeta of(String tableName, String type, List<ColumnMeta> columns) {
        TableMetaImpl data = new TableMetaImpl();
        data.setTableName(tableName);
        data.setColumns(columns);
        data.setType(type);
        return data;
    }

    static class TableMetaImpl implements TableMeta {

        private String tableName;

        private String type;

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

        @Override
        public String getType() {
            return type;
        }

        void setTableName(String tableName) {
            this.tableName = tableName;
        }

        void setColumns(List<ColumnMeta> columns) {
            this.columns = columns;
        }

        void setType(String type) {
            this.type = type;
        }
    }


}

