package com.tinyreports.report.models.transfer.csv;

import java.util.List;

/**
 * @author Anton Nesterenko
 */
public class CsvReport {
    private String name;
    private List<String> rows;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRows() {
        return rows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }
}
