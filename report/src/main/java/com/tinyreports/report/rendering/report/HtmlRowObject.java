package com.tinyreports.report.rendering.report;

import org.w3c.dom.Element;

import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class HtmlRowObject {
    private List<Element> trs;
    private Integer rowHeight;
    private Boolean odd;

    public void appendRow(Element element) {
        for (Element tr : trs) {
            element.appendChild(tr);
        }
    }

    public void insertTo(Element element) {
        for (Element tr : trs) {
            element.appendChild(tr);
        }
    }

    public List<Element> getTrs() {
        return trs;
    }

    public void setTrs(List<Element> trs) {
        this.trs = trs;
    }

    public Integer getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(Integer rowHeight) {
        this.rowHeight = rowHeight;
    }

    public boolean isOdd() {
        return odd;
    }

    public void setOdd(Boolean odd) {
        this.odd = odd;
    }
}
