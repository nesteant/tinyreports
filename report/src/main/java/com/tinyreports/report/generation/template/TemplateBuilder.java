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

package com.tinyreports.report.generation.template;


import com.tinyreports.common.UniqueTemplate;
import com.tinyreports.common.exceptions.TinyMarshallerException;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.exceptions.TinyReportTemplateException;
import org.w3c.dom.Node;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public abstract class TemplateBuilder<T extends UniqueTemplate> {

	//TODO remove node impl. Move to Sax
	public T convertToTemplate(Node node) throws TinyReportException {
		T template = parse(node);
		validate(template);
		return template;
	}

	//TODO remove node impl. Move to Sax
	public abstract T parse(Node node) throws TinyReportException;

	public abstract void validate(T template);
}
