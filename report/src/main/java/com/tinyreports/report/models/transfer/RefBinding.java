package com.tinyreports.report.models.transfer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Anton Nesterenko
 * @since 2.0
 */
@XmlRootElement(name = "ref")
public class RefBinding implements SerializableBinding {

	@XmlAttribute
	private String uuid;

	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
