package com.tinyreports.report.models.transfer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
class XmlCellIdGenerator {
    private static final XmlCellIdGenerator GENERATOR = new XmlCellIdGenerator();
    private static AtomicInteger ID = new AtomicInteger(0);

    private XmlCellIdGenerator() {
    }

    public static synchronized XmlCellIdGenerator getInstance() {
        return GENERATOR;
    }

    public static synchronized Integer getId() {
        return ID.incrementAndGet();
    }
}
