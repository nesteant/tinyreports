package com.tinyreports.report;

import com.tinyreports.common.DataProvider;
import com.tinyreports.report.facade.*;
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
