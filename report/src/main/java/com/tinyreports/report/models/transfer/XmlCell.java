package com.tinyreports.report.models.transfer;

import com.tinyreports.common.adapters.SetAdapter;
import com.tinyreports.report.models.templates.AdditionalAttributeHolder;
import com.tinyreports.report.models.templates.AutoClassAssignable;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
@XmlRootElement(name = "c")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlCell extends AdditionalAttributeHolder implements AutoClassAssignable {
    @XmlID
    @XmlAttribute(name = "u")
    private String cellId;
    @XmlTransient
    private Object objectValue;
    @XmlAttribute(name = "i")
    private String columnId;
    @XmlIDREF
    @XmlElement(name = "p")
    private XmlCell parentCell;
    @XmlElementWrapper(name = "cs")
    @XmlElement(name = "c")
    private List<XmlCell> childCells;
    @XmlJavaTypeAdapter(SetAdapter.class)
    @XmlAttribute(name = "r")
    private Set<String> relatedColumnIds;
    @XmlCDATA
    @XmlElement(name = "v")
    private String content;
    @XmlAttribute(name = "t")
    private Boolean isTagContent;
    @XmlTransient
    private Integer verticalPosition;
    @XmlTransient
    private Integer cellHeight;
    @XmlAttribute(name = "f")
    private boolean isFirst = false;

    public XmlCell() {
    }

    public XmlCell(Object objectValue, XmlCell parentCell) {
        this.cellId = XmlCellIdGenerator.getId().toString();
        this.objectValue = objectValue;
        this.parentCell = parentCell;
    }

    @XmlTransient
    public Integer getHeightOfChildren(String columnId) {
        Integer commonHeightOfCells = 0;
        List<XmlCell> childReportCellsForCurrentCell = getChildCellsById(columnId);
        for (XmlCell reportCell : childReportCellsForCurrentCell) {
            commonHeightOfCells += reportCell.getCellHeight();
        }
        return commonHeightOfCells;
    }

    @XmlTransient
    public List<XmlCell> getChildCellsById(String columnId) {
        List<XmlCell> cells = new ArrayList<XmlCell>();
        if (childCells == null || childCells.isEmpty()) {
            return cells;
        }
        for (XmlCell childCell : childCells) {
            if (childCell.getColumnId().equals(columnId)) {
                cells.add(childCell);
            }
        }
        return cells;
    }

    @XmlTransient
    public Map<String, List<XmlCell>> getChildCellMap() {
        Map<String, List<XmlCell>> childCellsMap = new HashMap<String, List<XmlCell>>();
        if (relatedColumnIds == null || relatedColumnIds.isEmpty()) {
            return childCellsMap;
        }
        for (String relatedColumnId : relatedColumnIds) {
            List<XmlCell> cells = getChildCellsById(relatedColumnId);
            childCellsMap.put(relatedColumnId, cells);
        }
        return childCellsMap;
    }

    public Object getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(Object objectValue) {
        this.objectValue = objectValue;
    }

    public XmlCell getParentCell() {
        return parentCell;
    }

    public void setParentCell(XmlCell parentCell) {
        this.parentCell = parentCell;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getTagContent() {
        return isTagContent;
    }

    public void setTagContent(Boolean tagContent) {
        isTagContent = tagContent;
    }

    public Integer getVerticalPosition() {
        return verticalPosition;
    }

    public void setVerticalPosition(Integer verticalPosition) {
        this.verticalPosition = verticalPosition;
    }

    public Integer getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(Integer cellHeight) {
        this.cellHeight = cellHeight;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public List<XmlCell> getChildCells() {
        return childCells;
    }

    public void setChildCells(List<XmlCell> childCells) {
        this.childCells = childCells;
    }

    public Set<String> getRelatedColumnIds() {
        return relatedColumnIds;
    }

    public void setRelatedColumnIds(Set<String> relatedColumnIds) {
        this.relatedColumnIds = relatedColumnIds;
    }

    @Override
    public String getId() {
        return columnId;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}
