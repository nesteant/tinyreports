package com.tinyreports.pdfgenerator.render;

import com.tinyreports.common.exceptions.TinyReportRenderException;
import com.tinyreports.pdfgenerator.util.FileUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author Anton Nesterenko
 * @since 0.5.4
 */
public class RendererConfigurator {
    private static Logger LOGGER = LoggerFactory.getLogger(RendererConfigurator.class);
    private static final String RENDER_DIR_NAME = "render";
    private static final String EXEC_DIR_NAME = "exec";
    private static final String RENDER_SCRIPT_NAME = "render.js";
    private static final String SVG_SCRIPT_NAME = "svg.js";
    //	private static final String
    private File renderDir;
    private File execDir;
    private String phantomExec;
    private String scriptExec;
    private String svgScriptExec;
    private String baseDirectoryName;

    public RendererConfigurator(String baseDirectoryName) {
        this.baseDirectoryName = baseDirectoryName;
    }

    public String getPhantomExec() {
        return phantomExec;
    }

    public String getScriptExec() {
        return scriptExec;
    }

    public String getSvgScriptExec() {
        return svgScriptExec;
    }

    public File getRenderDir() {
        return renderDir;
    }

    public void checkBaseDirectory() throws TinyReportRenderException {
        File baseDir = FileUtil.safeCreateDirectory(baseDirectoryName);
        if (baseDir != null) {
            String path = new File(baseDirectoryName).getAbsolutePath();
            renderDir = FileUtil.safeRecreateDirectory(FileUtil.createCorrectFileName(path, RENDER_DIR_NAME));
            execDir = FileUtil.safeRecreateDirectory(FileUtil.createCorrectFileName(path, EXEC_DIR_NAME));
            if (renderDir != null && execDir != null) {
                return;
            }
        }
        throw new TinyReportRenderException("Cannot create directory or its subfolders: " + baseDirectoryName);
    }

    public void configureRenderer() throws TinyReportRenderException {
        OsType osType = getOs();
        InputStream executableStream = null;
        String phantomExecutable = null;
        switch (osType) {
            case LINUX32: {
                executableStream = RendererConfigurator.class.getResourceAsStream("/linux32/phantomjs");
                phantomExecutable = "phantomjs";
                break;
            }
            case LINUX64: {
                executableStream = RendererConfigurator.class.getResourceAsStream("/linux64/phantomjs");
                phantomExecutable = "phantomjs";
                break;
            }
            case WIN: {
                executableStream = RendererConfigurator.class.getResourceAsStream("/windows/phantomjs");
                phantomExecutable = "phantomjs.exe";
                break;
            }
            case MACOS: {
                executableStream = RendererConfigurator.class.getResourceAsStream("/macos/phantomjs");
                phantomExecutable = "phantomjs";
                break;
            }
        }
        if (executableStream == null) {
            throw new UnsupportedOperationException("Current Operating System is not Supported. Please verify that you're using correct library");
        }
        File renderScriptFile = FileUtil.createFile(execDir.getAbsolutePath(), RENDER_SCRIPT_NAME);
        File svgScriptFile = FileUtil.createFile(execDir.getAbsolutePath(), SVG_SCRIPT_NAME);
        File phantomJsExecFile = FileUtil.createFile(execDir.getAbsolutePath(), phantomExecutable);
        phantomExec = phantomJsExecFile.getAbsolutePath();
        scriptExec = renderScriptFile.getAbsolutePath();
        svgScriptExec = svgScriptFile.getAbsolutePath();
        saveScript(svgScriptFile);
        saveScript(renderScriptFile);
        if (!phantomJsExecFile.exists()) {
            FileOutputStream execOs = null;
            try {
                execOs = new FileOutputStream(phantomJsExecFile);
                IOUtils.copy(executableStream, execOs);
            } catch (FileNotFoundException e) {
                throw new TinyReportRenderException(e);
            } catch (IOException e) {
                throw new TinyReportRenderException(e);
            } finally {
                IOUtils.closeQuietly(executableStream);
                IOUtils.closeQuietly(execOs);
            }
        }
        boolean readableSet = phantomJsExecFile.setReadable(true, false);
        boolean writableSet = phantomJsExecFile.setWritable(true, false);
        boolean executableSet = phantomJsExecFile.setExecutable(true, false);
        if (!executableSet) {
            LOGGER.warn("Phantom executable cannot be launched because of permissions. Please add +x permission to {}", phantomJsExecFile.getAbsolutePath());
        }
        LOGGER.debug("Rendering module prepared");
    }

    private OsType getOs() {
        String osTypeString = System.getProperty("os.name").toLowerCase();
        OsType osType;
        if (osTypeString.contains("windows")) {
            osType = OsType.WIN;
        } else if (osTypeString.contains("linux")) {
            if (System.getProperty("os.arch").contains("64")) {
                osType = OsType.LINUX64;
            } else {
                osType = OsType.LINUX32;
            }
        } else if (osTypeString.contains("mac")) {
            osType = OsType.MACOS;
        } else {
            throw new UnsupportedOperationException("Current Operating System is not Supported. Please verify that you're using correct library");
        }
        LOGGER.info("OS Type defined. OS: {}", osType);
        return osType;
    }

    private void saveScript(File script) throws TinyReportRenderException {
        if (!script.exists()) {
            InputStream sourceRenderScriptStream = null;
            FileOutputStream scriptOs = null;
            try {
                sourceRenderScriptStream = RendererConfigurator.class.getResourceAsStream("/scripts/" + script.getName());
                scriptOs = new FileOutputStream(script);
                IOUtils.copy(sourceRenderScriptStream, scriptOs);
            } catch (FileNotFoundException e) {
                throw new TinyReportRenderException(e);
            } catch (IOException e) {
                throw new TinyReportRenderException(e);
            } finally {
                IOUtils.closeQuietly(scriptOs);
                IOUtils.closeQuietly(sourceRenderScriptStream);
            }
        }
    }
}
