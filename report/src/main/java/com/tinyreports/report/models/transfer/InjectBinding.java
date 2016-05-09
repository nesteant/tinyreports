package com.tinyreports.report.models.transfer;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author Anton Nesterenko
 * @since 2.0
 */
@XmlRootElement(name = "ij")
public class InjectBinding implements SerializableBinding {

	@XmlAttribute(name = "uid")
	private String uuid;

	@XmlCDATA
	@XmlValue
	private String value;

	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
