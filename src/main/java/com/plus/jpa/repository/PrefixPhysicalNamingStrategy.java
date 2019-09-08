package com.plus.jpa.repository;

import lombok.var;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

/**
 * @author puddi
 */
@Component
public class PrefixPhysicalNamingStrategy implements PhysicalNamingStrategy {

    @Value("${spring.jpa.hibernate.naming.prefix:}")
    private String prefix;

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (!Objects.isNull(name)) {
            String nameText = name.getText();
            nameText = prefix + nameText;
            name = new Identifier(nameText, false);
        }
        return this.apply(name);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name);
    }

    private Identifier apply(Identifier name) {
        if (Objects.isNull(name)) {
            return null;
        } else {//在大写字母前加下划线：LoginName --> Login_Name
            StringBuilder builder = new StringBuilder(name.getText().replace('.', '_'));

            for (var i = 1; i < builder.length() - 1; ++i) {
                if (this.isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i), builder.charAt(i + 1))) {
                    builder.insert(i++, '_');
                }
            }

            return this.getIdentifier(builder.toString(), name.isQuoted());
        }
    }

    private Identifier getIdentifier(String name, boolean quoted) {
        name = name.toLowerCase(Locale.ROOT);
        return new Identifier(name, quoted);
    }

    private boolean isUnderscoreRequired(char before, char current, char after) {
        //2.将大写字母变为小写：Login_Name--->login_name
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
    }

}
