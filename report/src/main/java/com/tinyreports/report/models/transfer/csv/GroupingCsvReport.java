package com.tinyreports.report.models.transfer.csv;

import java.util.List;

/**
 * @author Anton Nesterenko
 */
public class GroupingCsvReport {
    List<CsvReport> csvReportList;

    public List<CsvReport> getCsvReportList() {
        return csvReportList;
    }

    public void setCsvReportList(List<CsvReport> csvReportList) {
        this.csvReportList = csvReportList;
    }
}
