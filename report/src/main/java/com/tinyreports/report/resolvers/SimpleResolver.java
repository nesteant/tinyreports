package com.tinyreports.report.resolvers;

import com.tinyreports.common.DataProvider;
import com.tinyreports.report.models.templates.ColumnTemplate;
import com.tinyreports.report.models.transfer.XmlCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class SimpleResolver extends RelationResolver {
    @Override
    public List<Object> getRelatedObjects(ColumnTemplate columnTemplate, Map<String, String> expressionMap, Object iteratorObject, XmlCell resolvingCell, DataProvider dataProvider) {
        List<Object> wrapper = new ArrayList<Object>();
        wrapper.add(resolvingCell.getObjectValue());
        return wrapper;
    }
}
