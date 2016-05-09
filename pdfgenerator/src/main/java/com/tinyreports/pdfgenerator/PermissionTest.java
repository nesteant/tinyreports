package com.tinyreports.pdfgenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Anton Nesterenko
 * @since 1.7.3
 */
public class PermissionTest {

	public static void main(String[] args) throws InterruptedException, IOException {
		phantomCall("\"/opt/tomcat/apache-tomcat-7.0.29/temp/exec/phantomjs\" --ignore-ssl-errors=yes \"/opt/tomcat/apache-tomcat-7.0.29/temp/exec/render.js\" \"file:/opt/tomcat/apache-tomcat-7.0.29/temp/render/76f5a8a3-adcf-4400-b1a4-6cfae357e91c.html\"");
	}

	public static int phantomCall(String str) throws InterruptedException, IOException {
		Runtime rt = Runtime.getRuntime();


		Process proc;
		try {
			proc = rt.exec(new String[] {"/opt/tomcat/apache-tomcat-7.0.29/temp/exec/phantomjs",
					"--ignore-ssl-errors=yes",
					"/opt/tomcat/apache-tomcat-7.0.29/temp/exec/render.js",
					"file:/opt/tomcat/apache-tomcat-7.0.29/temp/render/76f5a8a3-adcf-4400-b1a4-6cfae357e91c.html" });

			StreamWorker normalFlowWorker = new StreamWorker(proc.getInputStream(), System.out);
			StreamWorker errorFlowWorker = new StreamWorker(proc.getErrorStream(), System.err);
			normalFlowWorker.start();
			errorFlowWorker.start();

			int exitVal = proc.waitFor();
			normalFlowWorker.join();
			errorFlowWorker.join();

			return exitVal;
		} catch (InterruptedException e) {
			throw e;
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
				byte[] buffer = new byte[2048];
				long count = 0;
				int n = 0;
				while (-1 != (n = inputStream.read(buffer))) {
					outputStream.write(buffer, 0, n);
					count += n;
				}
			} catch (IOException e) {
				exception = e;
			}
		}
	}
}
