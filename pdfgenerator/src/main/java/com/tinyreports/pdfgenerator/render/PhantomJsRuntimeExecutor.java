package com.tinyreports.pdfgenerator.render;

import com.tinyreports.common.exceptions.TinyReportRenderException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.5.4
 */
public class PhantomJsRuntimeExecutor {

	private static Logger LOGGER = LoggerFactory.getLogger(PhantomJsRuntimeExecutor.class);

	public static int phantomExec(String executable, String script, List<String> params, OutputStream normalOut, OutputStream errorOut) throws TinyReportRenderException {
		Runtime rt = Runtime.getRuntime();

		List<String> executableParams = new ArrayList<String>();
		executableParams.add(executable);
		executableParams.add("--ignore-ssl-errors=yes");
		executableParams.add(script);
		executableParams.addAll(params);

		String[] fullParams = executableParams.toArray(new String[executableParams.size()]);

		Process proc;
		try {
			String exec = StringUtils.join(fullParams, " ");
			LOGGER.debug("Executing next String: {}", exec);
			proc = rt.exec(fullParams);

			StreamWorker normalFlowWorker = new StreamWorker(proc.getInputStream(), normalOut);
			StreamWorker errorFlowWorker = new StreamWorker(proc.getErrorStream(), errorOut);

			normalFlowWorker.start();
			errorFlowWorker.start();

			int exitVal = proc.waitFor();
			normalFlowWorker.join();
			errorFlowWorker.join();
			return exitVal;

		} catch (IOException e) {
			LOGGER.error("", e);
			throw new TinyReportRenderException(e);
		} catch (InterruptedException e) {
			LOGGER.error("", e);
			throw new TinyReportRenderException(e);
		}
	}

	public static int phantomCall(URL url, OutputStream normalOut, OutputStream errorOut, String phantomExec, String renderjs) throws TinyReportRenderException {
		Runtime rt = Runtime.getRuntime();

		String phantomKeys = "--ignore-ssl-errors=yes";
		String phantomExecutionString = String.format("\"%s\" %s \"%s\" \"%s\"", phantomExec, phantomKeys, renderjs, url);
		LOGGER.debug("Execution String {}", phantomExecutionString);
		Process proc;
		try {
			proc = rt.exec(new String[]{phantomExec, phantomKeys, renderjs, url.toString()});

			StreamWorker normalFlowWorker = new StreamWorker(proc.getInputStream(), normalOut);
			StreamWorker errorFlowWorker = new StreamWorker(proc.getErrorStream(), errorOut);

			normalFlowWorker.start();
			errorFlowWorker.start();

			int exitVal = proc.waitFor();
			normalFlowWorker.join();
			errorFlowWorker.join();

			return exitVal;
		} catch (IOException e) {
			LOGGER.error("", e);
			throw new TinyReportRenderException(e);
		} catch (InterruptedException e) {
			LOGGER.error("", e);
			throw new TinyReportRenderException(e);
		}
	}

	static class StreamWorker extends Thread {

		private InputStream inputStream;
		private OutputStream outputStream;
		private Exception exception;

		StreamWorker(InputStream inputStream, OutputStream outputStream) {
			this.inputStream = inputStream;
			this.outputStream = outputStream;
		}

		@Override
		public void run() {
			try {
				IOUtils.copyLarge(inputStream, outputStream);
			} catch (IOException e) {
				LOGGER.error("", e);
				exception = e;
			}
		}
	}
}
