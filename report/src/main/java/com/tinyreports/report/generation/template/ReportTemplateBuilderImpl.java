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

import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.utils.XmlDeserializer;
import com.tinyreports.report.resolvers.ElResolver;
import com.tinyreports.report.resolvers.SimpleResolver;
import com.tinyreports.report.models.templates.ColumnTemplate;
import com.tinyreports.report.models.templates.RelationTemplate;
import com.tinyreports.report.models.templates.ReportTemplate;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ReportTemplateBuilderImpl extends TemplateBuilder<ReportTemplate> {

	@Override
	public ReportTemplate parse(Node node) throws TinyReportException {
		return new XmlDeserializer<ReportTemplate>(ReportTemplate.class).deserialize(node);
	}

	@Override
	public void validate(ReportTemplate template) {
		String templateId = template.getId();
		String iteratorId = template.getIteratorId();
		String templateName = template.getCaption().getName();
		List<ColumnTemplate> columnTemplates = template.getColumns();
		for (ColumnTemplate columnTemplate : columnTemplates) {
			String columnId = columnTemplate.getId();
			String columnName = columnTemplate.getName();
			validateFieldsAreNotNull(columnId, columnName);

			setResolverClass(columnTemplate);
		}

		validateFieldsAreNotNull(templateId, iteratorId, templateName);
	}


	private void setResolverClass(ColumnTemplate template) {
		RelationTemplate relationTemplate = template.getRelationTemplate();
		if (relationTemplate == null) {
			RelationTemplate newRelationTemplate = new RelationTemplate();
			newRelationTemplate.setRelationResolverClass(SimpleResolver.class);
			template.setRelationTemplate(newRelationTemplate);
		} else if (relationTemplate.getRelationResolverClass() == null) {
			if (relationTemplate.getExpression() == null) {
				relationTemplate.setRelationResolverClass(SimpleResolver.class);
			} else {
				relationTemplate.setRelationResolverClass(ElResolver.class);
			}
		}
		if (StringUtils.isEmpty(template.getRelationTemplate().getColumnId())) {
			template.getRelationTemplate().setColumnId("iterator");
		}
	}

	private void validateFieldsAreNotNull(Object... fields) {

		for (Object field : fields) {
			if (field == null) {
				throw new NullPointerException("Report contains null values");
			}
		}
	}
}
