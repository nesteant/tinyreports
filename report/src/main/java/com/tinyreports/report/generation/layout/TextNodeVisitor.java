package com.tinyreports.report.generation.layout;

import com.tinyreports.common.exceptions.TinyReportException;
import org.w3c.dom.Node;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class TextNodeVisitor extends LayoutVisitor implements Visitable {
    protected TextNodeVisitor(BuildInfo buildInfo) {
        super(buildInfo);
    }

    @Override
    public void visit(Node n) throws TinyReportException, InterruptedException {
        String content = n.getTextContent();
        n.setTextContent(evaluateNodeExpression(content));
    }
}
