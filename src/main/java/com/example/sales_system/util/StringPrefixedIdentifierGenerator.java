package com.example.sales_system.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class StringPrefixedIdentifierGenerator implements IdentifierGenerator {
    private final String sequenceName;
    private final String prefix;
    private final String formatNumber;


    public StringPrefixedIdentifierGenerator(StringPrefixedIdentifier sequence) {
        sequenceName = sequence.sequenceName();
        prefix = sequence.prefix();
        formatNumber = sequence.formatNumber();
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object o) {
        var query = session.createQuery("select nextval('%s') ".formatted(sequenceName));
        Long id = (Long) query.uniqueResult();

        return prefix + String.format(formatNumber, id);
    }
}

