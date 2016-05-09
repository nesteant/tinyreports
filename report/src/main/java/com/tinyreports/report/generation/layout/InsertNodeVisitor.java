package com.tinyreports.report.generation.layout;

import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.utils.ContextUtils;
import com.tinyreports.common.utils.DomOperations;
import com.tinyreports.common.utils.XmlDeserializer;
import com.tinyreports.report.models.templates.InsertTemplate;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class InsertNodeVisitor extends LayoutVisitor implements Visitable {

	protected InsertNodeVisitor(BuildInfo buildInfo) {
		super(buildInfo);
	}

	@Override
	public void visit(Node n) throws TinyReportException, InterruptedException {

		InsertTemplate insertTemplate = new XmlDeserializer<InsertTemplate>(InsertTemplate.class).deserialize(n);

		String refName = insertTemplate.getRef();

		if (StringUtils.isEmpty(refName)) {
			removeNode(n);
			return;
		}
		Object refObject = dataProvider.getDataObjectByKey(refName);

		if (refObject == null) {
			removeNode(n);
			return;
		}

		List<InsertTemplate.Value> values = insertTemplate.getValues();
		if (values != null) {
			for (InsertTemplate.Value value : values) {

				String expression = value.getValue();
				Object result;
				if (expression.contains(TemplateVariables.EXPRESSION_CONTAINS_PATTERN)) {
					ContextUtils.loadToContext(globalContext, null, dataProvider, expression);
					result = ContextUtils.evaluateNonSafeExpression(globalContext, expression);
				} else {
					result = value.getValue();
				}
				globalContext.setVariable(value.getVar(), result);
			}
		}


		Node node = null;
		if (refObject instanceof String) {
			node = DomOperations.toHtmlNode((String) refObject);
		} else if (Node.class.isAssignableFrom(refObject.getClass())) {
			node = (Node) refObject;
		}
		if (node == null) {
			removeNode(n);
			return;
		}

		Node parent = n.getParentNode();

		Node importedNode = layout.adoptNode(node);
		parent.replaceChild(importedNode, n);
		NodeVisitor.visitChildren(buildInfo, importedNode);
	}

	private void removeNode(Node n) {
		Node parent = n.getParentNode();
		parent.removeChild(n);
	}
}
