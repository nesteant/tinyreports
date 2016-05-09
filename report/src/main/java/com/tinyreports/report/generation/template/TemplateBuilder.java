package com.tinyreports.report.generation.template;

import com.tinyreports.common.UniqueTemplate;
import com.tinyreports.common.exceptions.TinyReportException;
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
