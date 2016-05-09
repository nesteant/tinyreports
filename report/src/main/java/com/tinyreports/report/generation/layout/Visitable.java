package com.tinyreports.report.generation.layout;

import com.tinyreports.common.exceptions.TinyReportException;
import org.w3c.dom.Node;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public interface Visitable {
    void visit(Node n) throws TinyReportException, InterruptedException;
}
