package com.tinyreports.report.models.layout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Anton Nesterenko
 */
@XmlRootElement(name = "sorting")
@XmlAccessorType(XmlAccessType.FIELD)
public class DetachedSorting {

	@XmlElement(name = "sort")
	private List<SortingElement> sorting;

	public List<SortingElement> getSorting() {
		return sorting;
	}

	public void setSorting(List<SortingElement> sorting) {
		this.sorting = sorting;
	}
}
