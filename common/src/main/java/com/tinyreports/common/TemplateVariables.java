package com.tinyreports.common;

import java.util.regex.Pattern;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public interface TemplateVariables {
    String NS = "http://thelightapps.com/tiny";
    String XHTML_NS = "http://www.w3.org/1999/xhtml";
    String REPORT_TAG = "report";
    String CHART_TAG = "chart";
    String ITERATOR = "iterator";
    String CELL_OBJECT = "cell";
    String RESOLVING_CELL_OBJECT = "resolvingCell";
    String CAPTION_OBJECT = "objCaption";
    String CURRENT_OBJECT = "obj";
    String LOGIC_ROW = "row";
    String DATA_PROVIDER = "dataProvider";
    String RELATED_OBJECT = "relatedObject";
    String RELATED_OBJECTS = "relatedObjects";
    String EXPRESSION_CONTAINS_PATTERN = "#{";
    String CLASS_ATTR = "class";
    String UUID = "uuid";
    Pattern SERIALIZABLE_REPLACEMENT = Pattern.compile("(<[^>]*" + UUID + "=\"(.{36})\"[^>]*/>)");
    String GROUPING_WRAPPER_TAG = "grouping";
    String SORTING_WRAPPER_TAG = "sorting";
}
