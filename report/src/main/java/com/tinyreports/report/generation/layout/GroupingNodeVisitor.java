package com.tinyreports.report.generation.layout;

import com.tinyreports.common.DataProvider;
import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.exceptions.TinyReportTemplateException;
import com.tinyreports.common.utils.ArrayUtils;
import com.tinyreports.common.utils.ContextUtils;
import com.tinyreports.common.utils.DomOperations;
import com.tinyreports.common.utils.XmlDeserializer;
import com.tinyreports.common.utils.XmlSerializer;
import com.tinyreports.report.models.layout.DetachedSorting;
import com.tinyreports.report.models.layout.GroupingElement;
import com.tinyreports.report.models.layout.SortingElement;
import org.apache.commons.lang.StringUtils;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class GroupingNodeVisitor extends LayoutVisitor implements Visitable {

	protected GroupingNodeVisitor(BuildInfo buildInfo) {
		super(buildInfo);
	}

	@Override
	public void visit(Node n) throws TinyReportException, InterruptedException {

		GroupingElement grouping = new XmlDeserializer<GroupingElement>(GroupingElement.class).deserialize(n);

		grouping.setUuid(UUID.randomUUID().toString());
		String collection = grouping.getCollection();

		Collection<?> objects = prepareCollection(globalContext, dataProvider, collection);


		Node groupElement = DomOperations.findChildByLocalName(n, NodeVisitor.GROUP.name());

		List<Node> groups = new ArrayList<Node>();
		for (Object object : objects) {
			Node cloned = groupElement.cloneNode(true);

			//TODO one of properties is explicit
			globalContext.setVariable(CURRENT_OBJECT, object);
			//TODO probably this one
			globalContext.setVariable(CURRENT_OBJECT_VAR, grouping.getVar());
			globalContext.setVariable(grouping.getVar(), object);

			/** Sorting evaluation */
			DetachedSorting detachedGroupSorting = new DetachedSorting();
			detachedGroupSorting.setSorting(new ArrayList<SortingElement>());
			List<SortingElement> groupingSorts = grouping.getSortings();
			for (SortingElement groupingSort : groupingSorts) {
				SortingElement groupSort = new SortingElement(groupingSort);
				//todo define correct sorting in order to support sorting of strings
				String expression = String.format("#{#%s.%s}", grouping.getVar(), groupingSort.getField());
				String result = ContextUtils.evaluateSafeExpression(globalContext, expression, "");
				groupSort.setValue(result);
				detachedGroupSorting.getSorting().add(groupSort);
			}
			NodeVisitor.visitChildren(buildInfo, cloned);


			Node sorting = toNode(layout, detachedGroupSorting);
			cloned.appendChild(sorting);
			groups.add(cloned);
		}

		Node serializedGrouping = toNode(layout, grouping);

		for (Node group : groups) {
			serializedGrouping.appendChild(group);
		}

		Node parent = n.getParentNode();
		parent.replaceChild(serializedGrouping, n);
	}

	private Collection<?> prepareCollection(StandardEvaluationContext ctx, DataProvider dataProvider, String expression)
			throws TinyReportTemplateException {

		Object obj;
		if (StringUtils.isEmpty(expression)) {
			throw new TinyReportTemplateException("Collection is not specified");
		}

		if (expression.contains(TemplateVariables.EXPRESSION_CONTAINS_PATTERN)) {
			//TODO check this expression
			ContextUtils.loadToContext(ctx, null, dataProvider, expression);
			obj = ContextUtils.evaluateNonSafeExpression(ctx, expression);
		} else {
			obj = dataProvider.getDataObjectByKey(expression);
		}

		if (obj == null) {
			throw new TinyReportTemplateException("Collection is not in the dataProvider. CollectionName=%s", expression);
		}
		return ArrayUtils.linearalizeArrayOfObjects(obj);
	}
}
