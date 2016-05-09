package com.tinyreports.report.facade;

import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.report.models.transfer.XmlReport;
import com.tinyreports.report.models.transfer.csv.CsvReport;
import com.tinyreports.report.rendering.report.ReportRenderer;

import java.io.OutputStream;
import java.io.Writer;

/**
 * @author Anton Nesterenko
 * @since 0.6.0
 */
public final class XmlReportRenderer {

	public static void render(XmlReport xmlReport, OutputStream os) throws TinyReportException {
		new ReportRenderer(xmlReport).render(os);
	}
	public static void render(XmlReport xmlReport, Writer wr) throws TinyReportException {
		new ReportRenderer(xmlReport).render(wr);
	}

	public static CsvReport csvRender(XmlReport xmlReport) throws TinyReportException {
		return new ReportRenderer(xmlReport).render(CsvReport.class);
	}
}
