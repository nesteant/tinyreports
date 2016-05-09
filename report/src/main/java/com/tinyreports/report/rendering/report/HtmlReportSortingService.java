package com.tinyreports.report.rendering.report;

import com.tinyreports.report.models.templates.ReportSortingTemplate;
import com.tinyreports.report.models.templates.ReportTemplate;
import com.tinyreports.report.models.transfer.XmlCell;
import com.tinyreports.report.models.transfer.XmlReport;
import com.tinyreports.report.models.transfer.XmlRow;
import org.apache.commons.lang.ArrayUtils;

import java.util.List;
import java.util.Set;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class HtmlReportSortingService {
    protected static Boolean checkSortIsApplied(ReportTemplate template) {
        return ArrayUtils.isNotEmpty(template.getSortObjects());
    }

    private static ReportSortingTemplate getSortingTemplate(String columnId, ReportSortingTemplate[] sortObjects) {
        for (ReportSortingTemplate sortObject : sortObjects) {
            if (sortObject.getColumnId().equals(columnId)) {
                return sortObject;
            }
        }
        return null;
    }

    public static void provideInnerSorting(XmlRow xmlRow, ReportTemplate template) {
        if (checkSortIsApplied(template)) {
            ReportSortingTemplate[] sortingTemplates = template.getSortObjects();
            XmlCell iteratorCell = xmlRow.getIteratorCell();
            recursivelySortChildCells(iteratorCell, sortingTemplates);
        }
    }

    private static void recursivelySortChildCells(XmlCell xmlCell, ReportSortingTemplate[] sortingTemplates) {
        Set<String> relatedColumnIds = xmlCell.getRelatedColumnIds();
        if (relatedColumnIds == null) {
            return;
        }
        for (String relatedColumnId : relatedColumnIds) {
            ReportSortingTemplate sortingTemplate = getSortingTemplate(relatedColumnId, sortingTemplates);
            if (sortingTemplate != null) {
                ModelSorter.sortCellCollectionByField(xmlCell.getChildCells(), sortingTemplate);
            }
        }
        List<XmlCell> childCellImpls = xmlCell.getChildCells();
        if (childCellImpls == null) {
            return;
        }
        for (XmlCell childCellImpl : childCellImpls) {
            recursivelySortChildCells(childCellImpl, sortingTemplates);
        }
    }

    public static void provideOuterSorting(XmlReport xmlReport) {
        ReportTemplate reportTemplate = xmlReport.getReportTemplate();
        ReportSortingTemplate[] sortObjects = reportTemplate.getSortObjects();
        if (checkSortIsApplied(reportTemplate)) {
            ModelSorter.outerSorting(xmlReport.getXmlRows(), sortObjects);
        }
    }
}
