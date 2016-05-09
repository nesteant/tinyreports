package com.tinyreports.report.facade;

import com.tinyreports.common.DataProvider;
import com.tinyreports.common.exceptions.TinyReportTemplateException;
import com.tinyreports.common.utils.ArrayUtils;
import com.tinyreports.report.generation.report.ReportBuilder;
import com.tinyreports.report.models.templates.ReportTemplate;
import com.tinyreports.report.models.transfer.XmlReport;

import java.util.HashMap;

/**
 * @author Anton Nesterenko
 * @since 0.6.0
 */
public final class XmlReportGenerator {

	public static XmlReport generate(DataProvider dataProvider, ReportTemplate reportTemplate) throws TinyReportTemplateException {
		ReportBuilder reportBuilder = new ReportBuilder(dataProvider, reportTemplate, new HashMap<String, Object>());
		Object iteratorObject = dataProvider.getDataObjectByKey(reportTemplate.getIteratorId());
		reportBuilder.generate(ArrayUtils.linearalizeArrayOfObjects(iteratorObject));
		return reportBuilder.getXmlReport();
	}
}
