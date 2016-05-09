package com.tinyreports.report.generation.layout;

import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.report.models.transfer.InjectBinding;
import com.tinyreports.report.models.transfer.RefBinding;
import com.tinyreports.report.models.transfer.SerializableBinding;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class InjectNodeVisitor extends LayoutVisitor implements Visitable {

	protected InjectNodeVisitor(BuildInfo buildInfo) {
		super(buildInfo);
	}

	@Override
	public void visit(Node n) throws TinyReportException, InterruptedException {

		RefBinding refBinding = new RefBinding();

		String text = n.getTextContent();
		if (StringUtils.isEmpty(text)) {
			text = StringUtils.EMPTY;
		}
		final InjectBinding binding = new InjectBinding();

		binding.setValue(evaluateLayoutExpression(text.trim(), String.class));
		String uuid = UUID.randomUUID().toString();
		binding.setUuid(uuid);
		refBinding.setUuid(uuid);

		Node ref = toNode(layout, refBinding);
		Node parent = n.getParentNode();
		parent.replaceChild(ref, n);

		//TODO define correct way to pass this type of node directly to grouping report
		executorServiceHandler.submitToReportPool(new Callable<SerializableBinding>() {
			@Override
			public SerializableBinding call() throws Exception {
				return binding;
			}
		});
	}
}
