package com.tinyreports.report.facade;

import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.report.models.transfer.GroupingReport;
import com.tinyreports.report.models.transfer.csv.GroupingCsvReport;
import com.tinyreports.report.rendering.layout.GroupingReportRenderer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * @author Anton Nesterenko
 * @since 0.6.0
 */
public final class TinyReportsRenderer {

	public static GroupingCsvReport csvRender(GroupingReport groupingReport) throws TinyReportException {
		return new GroupingReportRenderer(groupingReport).csvRender();
	}

	public static String render(GroupingReport groupingReport) throws TinyReportException {
		return new GroupingReportRenderer(groupingReport).render();
	}

	//TODO define normal handling
	public static void render(GroupingReport groupingReport, OutputStream os) throws TinyReportException {
		try {
			IOUtils.write(render(groupingReport), os);
		} catch (IOException e) {
			throw new TinyReportException("", e);
		}
	}

	//TODO define normal handling
	public static void render(GroupingReport groupingReport, Writer wr) throws TinyReportException {
		try {
			IOUtils.write(render(groupingReport), wr);
		} catch (IOException e) {
			throw new TinyReportException("", e);
		}
	}


}
