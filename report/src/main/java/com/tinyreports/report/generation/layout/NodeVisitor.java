package com.tinyreports.report.generation.layout;

import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.utils.ContextUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public enum NodeVisitor {

	OTHER {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws TinyReportException, InterruptedException {

			if (n.hasAttributes()) {
				NamedNodeMap attributes = n.getAttributes();
				for (int i = 0; i < attributes.getLength(); i++) {
					Node attrNode = attributes.item(i);
					String nodeValue = attrNode.getNodeValue();
					if (nodeValue.contains(TemplateVariables.EXPRESSION_CONTAINS_PATTERN)) {
						ContextUtils.loadToContext(buildInfo.getGlobalContext(), null, buildInfo.getDataProvider(), nodeValue);
						nodeValue = (String) ContextUtils.evaluateNonSafeExpression(buildInfo.getGlobalContext(), nodeValue, "", String.class);
						attrNode.setNodeValue(nodeValue);
					}
				}
			}
			visitChildren(buildInfo, n);
		}
	},
	TEXT {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws InterruptedException, TinyReportException {
			new TextNodeVisitor(buildInfo).visit(n);
		}
	},
	LINK {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws InterruptedException, TinyReportException {
			if (((Element) n).hasAttribute("href")) {
				new LinkNodeVisitor(buildInfo).visit(n);
			} else {
				new InjectNodeVisitor(buildInfo).visit(n);
			}
		}
	},
	SCRIPT {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws InterruptedException, TinyReportException {
			if (((Element) n).hasAttribute("src")) {
				new LinkNodeVisitor(buildInfo).visit(n);
			} else {
				new InjectNodeVisitor(buildInfo).visit(n);
			}
		}
	},
	IMG {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws InterruptedException, TinyReportException {
			new LinkNodeVisitor(buildInfo).visit(n);
		}
	},
	COMPLEX {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws TinyReportException, InterruptedException {
			new ComplexNodeVisitor(buildInfo).visit(n);
		}
	},
	GROUP,
	GROUPING {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws TinyReportException, InterruptedException {
			new GroupingNodeVisitor(buildInfo).visit(n);
		}
	},
	REPORTREF {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws InterruptedException, TinyReportException {
			new ReportRefNodeVisitor(buildInfo).visit(n);
		}
	},
	REPORT {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws InterruptedException, TinyReportException {
			new ReportNodeVisitor(buildInfo).visit(n);
		}
	},
	CHART {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws TinyReportException, InterruptedException {
			new ChartNodeVisitor(buildInfo).visit(n);
		}
	},
	INSERT {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws TinyReportException, InterruptedException {
			new InsertNodeVisitor(buildInfo).visit(n);
		}
	},
	HTML {
		@Override
		public void innerVisit(BuildInfo buildInfo, Node n) throws TinyReportException, InterruptedException {
			new HtmlNodeVisitor(buildInfo).visit(n);
		}
	};

	private static Logger LOGGER = LoggerFactory.getLogger(NodeVisitor.class);

	public void innerVisit(BuildInfo buildInfo, Node n) throws TinyReportException, InterruptedException {
	}

	public static void visitChildren(BuildInfo buildInfo, Node n) throws InterruptedException, TinyReportException {
		NodeList list = n.getChildNodes();
		visit(buildInfo, list);
	}

	public static void visit(BuildInfo buildInfo, NodeList nodeList) throws InterruptedException, TinyReportException {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			visit(buildInfo, childNode);
		}
	}

	public static void visit(BuildInfo buildInfo, Node n) throws TinyReportException, InterruptedException {
		short nodeType = n.getNodeType();
		if (nodeType == 3) {
			TEXT.innerVisit(buildInfo, n);
		} else if (nodeType == 8) {
		} else {
			String nodeName = n.getLocalName();
			if (nodeName == null) {
				LOGGER.warn("Unsupported node type: {}", n.getNodeName());
				return;
			}
			findVisitor(nodeName.toUpperCase()).innerVisit(buildInfo, n);
		}
	}

	private static NodeVisitor findVisitor(String name) {
		if (StringUtils.isEmpty(name)) {
			return OTHER;
		} else {
			for (NodeVisitor nodeVisitor : values()) {
				if (nodeVisitor.name().equals(name)) {
					return nodeVisitor;
				}
			}
		}
		return OTHER;
	}
}
