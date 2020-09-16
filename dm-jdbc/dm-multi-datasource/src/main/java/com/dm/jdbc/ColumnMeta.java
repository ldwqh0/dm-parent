package com.dm.jdbc;


public class ColumnMeta {
    private String tableCat;  //1
    private String tableSchem;//2
    private String tableName;//3
    private String columnName;//4
    private int dataType;//5
    private String typeName;//6
    private String columnSize;//7
    // this is not used
    // private String bufferLength;//8
    private int decimalDigits;//9
    private int numPrecRadix;//10
    private int nullable;//11
    private String remarks;//12
    private String columnDef;//13
    private int sqlDataType;//14
    private int sqlDatetimeSub;//15
    private int charOctetLength;//16
    private int ordinalPosition;//17
    private String isNullable;//18
    private String scopeCatalog;//19
    private String scopeSchema;//20
    private String scopeTable;//21
    private short sourceDataType;//22
    private String isAutoincrement;//23
    private String isGeneratedcolumn;//24

    public ColumnMeta() {
    }

    public ColumnMeta(String tableCat,
                      String tableSchem,
                      String tableName,
                      String columnName,
                      int dataType,
                      String typeName,
                      String columnSize,
                      int decimalDigits,
                      int numPrecRadix,
                      int nullable,
                      String remarks,
                      String columnDef,
                      int sqlDataType,
                      int sqlDatetimeSub,
                      int charOctetLength,
                      int ordinalPosition,
                      String isNullable,
                      String scopeCatalog,
                      String scopeSchema,
                      String scopeTable,
                      short sourceDataType,
                      String isAutoincrement,
                      String isGeneratedcolumn) {
        this.tableCat = tableCat;
        this.tableSchem = tableSchem;
        this.tableName = tableName;
        this.columnName = columnName;
        this.dataType = dataType;
        this.typeName = typeName;
        this.columnSize = columnSize;
        this.decimalDigits = decimalDigits;
        this.numPrecRadix = numPrecRadix;
        this.nullable = nullable;
        this.remarks = remarks;
        this.columnDef = columnDef;
        this.sqlDataType = sqlDataType;
        this.sqlDatetimeSub = sqlDatetimeSub;
        this.charOctetLength = charOctetLength;
        this.ordinalPosition = ordinalPosition;
        this.isNullable = isNullable;
        this.scopeCatalog = scopeCatalog;
        this.scopeSchema = scopeSchema;
        this.scopeTable = scopeTable;
        this.sourceDataType = sourceDataType;
        this.isAutoincrement = isAutoincrement;
        this.isGeneratedcolumn = isGeneratedcolumn;
    }

    public String getTableCat() {
        return tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getDataType() {
        return dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getColumnSize() {
        return columnSize;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public int getNumPrecRadix() {
        return numPrecRadix;
    }

    public int getNullable() {
        return nullable;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getColumnDef() {
        return columnDef;
    }

    public int getSqlDataType() {
        return sqlDataType;
    }

    public int getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public int getCharOctetLength() {
        return charOctetLength;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public String getScopeSchema() {
        return scopeSchema;
    }

    public String getScopeTable() {
        return scopeTable;
    }

    public short getSourceDataType() {
        return sourceDataType;
    }

    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    public String getIsGeneratedcolumn() {
        return isGeneratedcolumn;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setColumnSize(String columnSize) {
        this.columnSize = columnSize;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public void setNumPrecRadix(int numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setColumnDef(String columnDef) {
        this.columnDef = columnDef;
    }

    public void setSqlDataType(int sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public void setSqlDatetimeSub(int sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    public void setCharOctetLength(int charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public void setScopeCatalog(String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    public void setScopeSchema(String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    public void setScopeTable(String scopeTable) {
        this.scopeTable = scopeTable;
    }

    public void setSourceDataType(short sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    public void setIsAutoincrement(String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    public void setIsGeneratedcolumn(String isGeneratedcolumn) {
        this.isGeneratedcolumn = isGeneratedcolumn;
    }
}
