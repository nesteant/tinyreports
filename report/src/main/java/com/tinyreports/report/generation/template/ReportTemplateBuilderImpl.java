package com.tinyreports.report.generation.template;

import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.utils.XmlDeserializer;
import com.tinyreports.report.models.templates.ColumnTemplate;
import com.tinyreports.report.models.templates.RelationTemplate;
import com.tinyreports.report.models.templates.ReportTemplate;
import com.tinyreports.report.resolvers.ElResolver;
import com.tinyreports.report.resolvers.SimpleResolver;
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
