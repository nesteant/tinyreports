package com.tinyreports.report.models.transfer;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.5.4
 */
@XmlRootElement(name = "gr")
public class GroupingReport {
    @XmlElement(name = "lt")
    @XmlCDATA
    private String layout;
    @XmlElementWrapper(name = "ijs")
    @XmlElement(name = "ij")
    private List<InjectBinding> injections = new ArrayList<InjectBinding>();
    @XmlElementWrapper(name = "rbs")
    @XmlElement(name = "rb")
    private List<ReportBinding> reportBindings = new ArrayList<ReportBinding>();
    @XmlElementWrapper(name = "cbs")
    @XmlElement(name = "cb")
    private List<ChartBinding> chartBindings = new ArrayList<ChartBinding>();

    public SerializableBinding getBinding(String uuid) {
        for (ReportBinding reportBinding : reportBindings) {
            if (reportBinding.getUuid().equals(uuid)) {
                return reportBinding;
            }
        }
        for (ChartBinding chartBinding : chartBindings) {
            if (chartBinding.getUuid().equals(uuid)) {
                return chartBinding;
            }
        }
        for (InjectBinding injection : injections) {
            if (injection.getUuid().equals(uuid)) {
                return injection;
            }
        }
        return null;
    }

    public List<InjectBinding> getInjections() {
        return injections;
    }

    public List<ReportBinding> getReportBindings() {
        return reportBindings;
    }

    public List<ChartBinding> getChartBindings() {
        return chartBindings;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }
}
