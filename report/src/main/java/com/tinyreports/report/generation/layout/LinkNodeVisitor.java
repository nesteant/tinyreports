package com.tinyreports.report.generation.layout;

import com.tinyreports.common.exceptions.TinyReportException;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class LinkNodeVisitor extends LayoutVisitor implements Visitable {
    protected LinkNodeVisitor(BuildInfo buildInfo) {
        super(buildInfo);
    }

    @Override
    public void visit(Node n) throws TinyReportException, InterruptedException {
        NamedNodeMap namedNodeMap = n.getAttributes();
        for (int j = 0; j < namedNodeMap.getLength(); j++) {
            Node attribute = namedNodeMap.item(j);
            processAttrNode(attribute);
        }
    }

    private void processAttrNode(Node attrNode) {
        String content = attrNode.getNodeValue();
        String evaluatedContent = StringEscapeUtils.escapeHtml(evaluateNodeExpression(content));
        attrNode.setNodeValue(evaluatedContent);
    }
}
