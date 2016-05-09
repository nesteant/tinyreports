package com.tinyreports.report.generation.layout;

import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.utils.ContextUtils;
import com.tinyreports.common.utils.DomOperations;
import com.tinyreports.common.utils.XmlDeserializer;
import com.tinyreports.report.models.templates.HtmlTemplate;
import org.w3c.dom.Node;

/**
 * @author Anton Nesterenko
 */
public class HtmlNodeVisitor extends LayoutVisitor implements Visitable {
    protected HtmlNodeVisitor(BuildInfo buildInfo) {
        super(buildInfo);
    }

    @Override
    public void visit(Node n) throws TinyReportException, InterruptedException {
        HtmlTemplate htmlTemplate = new XmlDeserializer<HtmlTemplate>(HtmlTemplate.class).deserialize(n);
        String content = htmlTemplate.getHtml();
        if (content.contains(TemplateVariables.EXPRESSION_CONTAINS_PATTERN)) {
            ContextUtils.loadToContext(globalContext, null, dataProvider, content);
            content = (String) ContextUtils.evaluateNonSafeExpression(globalContext, content, null, String.class);
        }
        Node htmlNode = DomOperations.toHtmlNode(content);
        Node parent = n.getParentNode();
        Node importedNode = layout.adoptNode(htmlNode);
        parent.replaceChild(importedNode, n);
        NodeVisitor.visitChildren(buildInfo, importedNode);
    }
}
