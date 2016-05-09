package com.tinyreports.report.models.templates;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
@XmlRootElement(name = "insert")
public class InsertTemplate {

	@XmlAttribute
	private String ref;

	@XmlList
	@XmlElement(name = "value")
	private List<Value> values;

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}

	@XmlRootElement(name = "value")
	public static class Value {

		@XmlAttribute
		private String var;
		@XmlValue
		private String value;

		public Value() {
		}

		public String getVar() {
			return var;
		}

		public void setVar(String var) {
			this.var = var;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
