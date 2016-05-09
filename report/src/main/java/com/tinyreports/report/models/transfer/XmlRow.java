package com.tinyreports.report.models.transfer;

import com.tinyreports.report.models.templates.AdditionalAttributeHolder;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
@XmlRootElement(name = "r")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlRow extends AdditionalAttributeHolder {
    @XmlElement(name = "c")
    private XmlCell iteratorCell;
    @XmlTransient
    private Object objectIterator;
    @XmlTransient
    private Boolean hasNonBlankValues = false;
    @XmlTransient
    private Map<String, List<XmlCell>> childCellMap;

    public List<XmlCell> accessColumn(String columnName) {
        if (childCellMap == null) {
            prepareCellMap();
        }
        return childCellMap.get(columnName);
    }

    public void prepareCellMap() {
        childCellMap = new HashMap<String, List<XmlCell>>();
        resolveChildren(iteratorCell);
    }

    private void resolveChildren(XmlCell xmlCell) {
        String cellColumnId = xmlCell.getColumnId();
        if (!childCellMap.containsKey(cellColumnId)) {
            childCellMap.put(cellColumnId, new ArrayList<XmlCell>());
        }
        childCellMap.get(cellColumnId).add(xmlCell);
        List<XmlCell> childCells = xmlCell.getChildCells();
        if (childCells == null || childCells.isEmpty()) {
        } else {
            for (XmlCell childCell : childCells) {
                resolveChildren(childCell);
            }
        }
    }

    public Integer getMaxHeight() {
        return iteratorCell.getCellHeight();
    }

    public Object getObjectIterator() {
        return objectIterator;
    }

    public void setObjectIterator(Object objectIterator) {
        this.objectIterator = objectIterator;
    }

    public XmlCell getIteratorCell() {
        return iteratorCell;
    }

    public void setIteratorCell(XmlCell iteratorCell) {
        this.iteratorCell = iteratorCell;
    }

    public Map<String, List<XmlCell>> getChildCellMap() {
        return childCellMap;
    }

    public void setChildCellMap(Map<String, List<XmlCell>> childCellMap) {
        this.childCellMap = childCellMap;
    }

    public Boolean getHasNonBlankValues() {
        return hasNonBlankValues;
    }

    public void setHasNonBlankValues(Boolean hasNonBlankValues) {
        this.hasNonBlankValues = hasNonBlankValues;
    }
}
