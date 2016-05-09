/*
 * Tinyreports
 * Copyright (c) 2013. Anton Nesterenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tinyreports.report;

import com.tinyreports.common.DataProvider;
import com.tinyreports.report.facade.TinyReportsDeserializer;
import com.tinyreports.report.facade.TinyReportsGenerator;
import com.tinyreports.report.facade.TinyReportsRenderer;
import com.tinyreports.report.facade.TinyReportsSerializer;
import com.tinyreports.report.facade.XmlReportGenerator;
import com.tinyreports.report.facade.XmlReportRenderer;
import com.tinyreports.report.factory.TemplateFactory;
import com.tinyreports.report.models.templates.ReportTemplate;
import com.tinyreports.report.models.transfer.GroupingReport;
import com.tinyreports.report.models.transfer.XmlReport;
import com.tinyreports.report.models.transfer.csv.CsvReport;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * @author Anton Nesterenko (TEAM International)
 * @since 0.5.6
 */
public class BaseTest {


	public static void main(String[] args) throws Exception {

		DataProvider dataProvider = new DataProvider();
		dataProvider.putObject("REPORT", TemplateFactory.newInstance(BaseTest.class.getResourceAsStream("/com/tinyreports/report/test.xml")));
		dataProvider.putObject("REPORT_ITERATOR", new ArrayList<String>() {{
			add("a");
			add("b");
			add("c");
			add("d");
			add("e");
			add("f");
			add("g");
			add("h");
			add("i");
		}});
		dataProvider.putObject("TEST", "<div>test: #{#testtt}</div>");

		GroupingReport report = TinyReportsGenerator.generate(dataProvider, BaseTest.class.getResourceAsStream("/com/tinyreports/report/layout.html"));
		FileOutputStream fos = new FileOutputStream("/tmp/tttttest.xml");
		TinyReportsSerializer.serialize(report, fos);

		fos.close();
		FileReader fileReader = new FileReader("/tmp/tttttest.xml");

		FileWriter wr = new FileWriter("/tmp/render.html");
		TinyReportsRenderer.render(report, wr);
		wr.close();
		TinyReportsDeserializer.deserialize(fileReader);
		System.out.println(TinyReportsRenderer.render(report));
		fileReader.close();

		XmlReport r = XmlReportGenerator.generate(dataProvider, (ReportTemplate) TemplateFactory.newInstance(BaseTest.class.getResourceAsStream("/com/tinyreports/report/test.xml")));
		FileWriter wr1 = new FileWriter("/tmp/render2.html");
		CsvReport csvReport = XmlReportRenderer.csvRender(r);
		wr1.close();
//
//		XmlReport r = XmlReportGenerator.generate(dataProvider, (ReportTemplate) TemplateFactory.newInstance(BaseTest.class.getResourceAsStream("/com/tinyreports/report/test.xml")));
//		FileWriter wr1 = new FileWriter("/tmp/render2.html");
//		XmlReportRenderer.render(r, wr1);
//		wr1.close();
	}

}
