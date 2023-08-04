package com.increff.pos.util;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class SnakeCasePhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {
    @Override
    public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv){
        String name = identifier.getText();
        if (name.endsWith("Pojo")) {
            name = name.substring(0, name.length() - 4);
        }
        name = convertCamelCaseToSnakeCase(name);;
        // Convert the name to lowercase
        String modifiedName = name.toLowerCase();
        return Identifier.toIdentifier(modifiedName);
    }
    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        String snakeCaseName = convertCamelCaseToSnakeCase(name.getText());
        return Identifier.toIdentifier(snakeCaseName);
    }
    private String convertCamelCaseToSnakeCase(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
