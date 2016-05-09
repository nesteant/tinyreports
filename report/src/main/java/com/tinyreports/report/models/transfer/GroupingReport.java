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
