package com.tinyreports.report.generation.template;

import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.utils.XmlDeserializer;
import com.tinyreports.report.models.templates.ChartTemplate;
import org.w3c.dom.Node;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ChartTemplateBuilderImpl extends TemplateBuilder<ChartTemplate> {
    @Override
    public ChartTemplate parse(Node node) throws TinyReportException {
        return new XmlDeserializer<ChartTemplate>(ChartTemplate.class).deserialize(node);
    }

    @Override
    public void validate(ChartTemplate template) {
    }
}
