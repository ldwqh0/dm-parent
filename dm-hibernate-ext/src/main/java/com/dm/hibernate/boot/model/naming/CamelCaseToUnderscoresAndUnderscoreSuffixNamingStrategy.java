package com.dm.hibernate.boot.model.naming;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

/**
 * 一个命名策略，用于将驼峰表名称转换为下划线分割的名称，并且全部转换为小写字母，并且在表名称之后一定添加一个下划线（避免和关键字冲突）。
 *
 * @see PhysicalNamingStrategy
 */
public class CamelCaseToUnderscoresAndUnderscoreSuffixNamingStrategy implements PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name, jdbcEnvironment);
    }

    private Identifier apply(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (name == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(name.getText().replace('.', '_'));
        for (int i = 1; i < builder.length() - 1; i++) {
            if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i), builder.charAt(i + 1))) {
                builder.insert(i++, '_');
            }
        }
        // 如果名称不以下划线结尾，添加下划线
        String result = builder.toString();
        if (!StringUtils.endsWith(result, "_")) {
            result += "_";
        }
        return getIdentifier(result, name.isQuoted(), jdbcEnvironment);
    }

    /**
     * Get an identifier for the specified details. By default this method will return an
     * identifier with the name adapted based on the result of
     * {@link #isCaseInsensitive(JdbcEnvironment)}
     *
     * @param name            the name of the identifier
     * @param quoted          if the identifier is quoted
     * @param jdbcEnvironment the JDBC environment
     * @return an identifier instance
     */
    protected Identifier getIdentifier(String name, boolean quoted, JdbcEnvironment jdbcEnvironment) {
        if (isCaseInsensitive(jdbcEnvironment)) {
            name = name.toLowerCase(Locale.ROOT);
        }
        return new Identifier(name, quoted);
    }

    /**
     * Specify whether the database is case sensitive.
     *
     * @param jdbcEnvironment the JDBC environment which can be used to determine case
     * @return true if the database is case insensitive sensitivity
     */
    protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
        return true;
    }

    private boolean isUnderscoreRequired(char before, char current, char after) {
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
    }
}
