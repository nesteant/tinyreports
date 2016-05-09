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
