/*
 * Tinyreports
 * Copyright (c) 2013. Anton Nesterenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tinyreports.report.generation.layout;

import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.exceptions.TinyReportException;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class ComplexNodeVisitor extends LayoutVisitor implements Visitable {

	protected ComplexNodeVisitor(BuildInfo buildInfo) {
		super(buildInfo);
	}

	@Override
	public void visit(Node n) throws TinyReportException, InterruptedException {

		Element complexElement = (Element) n;
		String printWhenExpression = complexElement.getAttribute(PRINT_WHEN_REPORT_ATTR);

		boolean shouldPrint = true;
		if (!StringUtils.isEmpty(printWhenExpression)) {
			shouldPrint = evaluateLayoutExpression(globalContext, dataProvider, printWhenExpression, Boolean.class);
		}

		if (shouldPrint) {
			String complexAttrName = NodeVisitor.COMPLEX.name().toLowerCase();
			if (complexElement.hasAttribute(TemplateVariables.CLASS_ATTR)) {
				complexElement.setAttribute(TemplateVariables.CLASS_ATTR, complexElement.getAttribute(TemplateVariables.CLASS_ATTR) + " " + complexAttrName);
			} else {
				complexElement.setAttribute(TemplateVariables.CLASS_ATTR, complexAttrName);
			}
			NodeVisitor.visitChildren(buildInfo, n);
		} else {
			Node parent = n.getParentNode();
			parent.removeChild(n);
		}
	}
}
