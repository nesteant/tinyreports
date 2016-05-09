package com.tinyreports.pdfgenerator.render;

import com.tinyreports.common.exceptions.TinyMarshallerException;
import com.tinyreports.common.exceptions.TinyReportRenderException;
import com.tinyreports.pdfgenerator.util.FileUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Anton Nesterenko
 * @since 0.5.4
 */
public class TinyRenderer {
    private static Logger LOGGER = LoggerFactory.getLogger(TinyRenderer.class);
    private RendererConfigurator rendererConfigurator;
    private String baseDirectory;
    private static final Integer DEFAULT_DPI = 600;
    private static final Integer DEFAULT_WIDTH_PX = 1000;
    private static final PaperSize DEFAULT_PAPER_SIZE = PaperSize.A4V;
    private boolean unwrapSvg = false;
    private boolean skipSvg = false;

    public TinyRenderer() throws TinyReportRenderException {
        this(System.getProperty("user.dir"));
    }

    public TinyRenderer(String baseDirectory) throws TinyReportRenderException {
        this.baseDirectory = baseDirectory;
        initialize();
    }

    private void initialize() throws TinyReportRenderException {
        rendererConfigurator = new RendererConfigurator(baseDirectory);
        rendererConfigurator.checkBaseDirectory();
        rendererConfigurator.configureRenderer();
    }

    public void render(Reader reader, PaperSize paperSize, Integer dpi, Integer maximumWidth, OutputStream outputStream) throws TinyReportRenderException, TinyMarshallerException {
        File renderDir = rendererConfigurator.getRenderDir();
        String uniqueTempFileName = FileUtil.createCorrectFileName(renderDir.getAbsolutePath(), UUID.randomUUID().toString()).concat(".html");
        File uniqueFile = new File(uniqueTempFileName);
        Writer wr = null;
        try {
            wr = new FileWriter(uniqueFile);
            IOUtils.copy(reader, wr);
        } catch (IOException e) {
            LOGGER.error("", e);
            throw new TinyReportRenderException(e);
        } finally {
            IOUtils.closeQuietly(wr);
        }
        URL url = FileUtil.formUrl(uniqueFile);
        render(url, paperSize, dpi, maximumWidth, outputStream);
        FileUtil.tryToDeleteFileObject(uniqueFile);
    }

    public void render(URL url, OutputStream outputStream) throws TinyReportRenderException, TinyMarshallerException {
        render(url, DEFAULT_PAPER_SIZE, DEFAULT_DPI, DEFAULT_WIDTH_PX, outputStream);
    }

    public void render(URL url, Integer maximumWidth, OutputStream outputStream) throws TinyReportRenderException, TinyMarshallerException {
        render(url, DEFAULT_PAPER_SIZE, DEFAULT_DPI, maximumWidth, outputStream);
    }

    public void render(URL url, PaperSize paperSize, Integer maximumWidth, OutputStream outputStream) throws TinyReportRenderException, TinyMarshallerException {
        render(url, paperSize, DEFAULT_DPI, maximumWidth, outputStream);
    }

    public void render(URL url, PaperSize paperSize, Integer dpi, Integer maximumWidth, OutputStream outputStream) throws TinyReportRenderException, TinyMarshallerException {
        String htmlString;
        File renderDir = rendererConfigurator.getRenderDir();
        String uniqueKey = UUID.randomUUID().toString();
        File uniqueDir = FileUtil.createFile(renderDir.getAbsolutePath(), uniqueKey);
        FileUtil.safeCreateDirectory(uniqueDir.getAbsolutePath());
        if (skipSvg) {
            htmlString = extractUrlInfo(url);
        } else {
            String phantomRenderedString = phantomRender(url);
            String phantomExec = rendererConfigurator.getPhantomExec();
            String scriptExec = rendererConfigurator.getSvgScriptExec();
            htmlString = new PhantomSvgRenderer(phantomRenderedString, uniqueDir).extract(phantomExec, scriptExec);
        }
        int dotsPerPixel = geDotsPerPixel(maximumWidth, paperSize, dpi);
        ITextRenderer renderer = new ITextRenderer(dpi / 72f, dotsPerPixel);
        try {
            renderer.setDocument(Jsoup.parse(htmlString), url.toString());
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new TinyReportRenderException(e);
        }
        FileUtil.tryToDeleteFileObject(uniqueDir);
    }

    public String getStaticSvgHtml(URL url) throws TinyReportRenderException, TinyMarshallerException {
        return phantomRender(url);
    }

    public String getStaticImgHtml(String urlString, File baseFolder) throws TinyReportRenderException {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new TinyReportRenderException(e);
        }
        if (baseFolder == null) {
            File renderDir = rendererConfigurator.getRenderDir();
            String uniqueKey = UUID.randomUUID().toString();
            baseFolder = FileUtil.createFile(renderDir.getAbsolutePath(), uniqueKey);
            FileUtil.safeCreateDirectory(baseFolder.getAbsolutePath());
        }
        String phantomRenderedString = phantomRender(url);
        String phantomExec = rendererConfigurator.getPhantomExec();
        String scriptExec = rendererConfigurator.getSvgScriptExec();
        return new PhantomSvgRenderer(phantomRenderedString, baseFolder).extract(phantomExec, scriptExec);
    }

    public String getStaticImgHtml(URL url, File baseFolder) throws TinyReportRenderException {
        if (baseFolder == null) {
            File renderDir = rendererConfigurator.getRenderDir();
            String uniqueKey = UUID.randomUUID().toString();
            baseFolder = FileUtil.createFile(renderDir.getAbsolutePath(), uniqueKey);
            FileUtil.safeCreateDirectory(baseFolder.getAbsolutePath());
        }
        String phantomRenderedString = phantomRender(url);
        String phantomExec = rendererConfigurator.getPhantomExec();
        String scriptExec = rendererConfigurator.getSvgScriptExec();
        return new PhantomSvgRenderer(phantomRenderedString, baseFolder).extract(phantomExec, scriptExec);
    }

    private int geDotsPerPixel(Integer documentWidthPx, PaperSize paperSize, Integer dpi) {
        double fractionalPart = documentWidthPx / paperSize.getInchWidth() % 1;
        Double integralPart = (documentWidthPx / paperSize.getInchWidth() - fractionalPart);
        // DPI / dots per pixel
        Integer quantityOfPixelsPerInch = integralPart.intValue();
        return dpi / quantityOfPixelsPerInch;
    }

    private String extractUrlInfo(URL url) throws TinyReportRenderException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            return IOUtils.toString(in);
        } catch (IOException e) {
            LOGGER.error("", e);
            throw new TinyReportRenderException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private String phantomRender(final URL url) throws TinyReportRenderException {
        String phantomExec = rendererConfigurator.getPhantomExec();
        String scriptExec = rendererConfigurator.getScriptExec();
        ByteArrayOutputStream info = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();
        List<String> params = new ArrayList<String>() {{
            add(url.toString());
            add(String.valueOf(unwrapSvg));
        }};
        PhantomJsRuntimeExecutor.phantomExec(phantomExec, scriptExec, params, info, error);
        String infoMsg = info.toString();
        String errorMsg = error.toString();
        if (StringUtils.isNotEmpty(errorMsg)) {
            LOGGER.error("INFO OUT: {}. ERROR OUT: {}", infoMsg, errorMsg);
        }
        String result = info.toString();
        LOGGER.trace(result);
        if (result.contains("<html")) {
            int pos = result.indexOf("<html");
            if (pos > 0) {
                String errorText = result.substring(0, pos);
                LOGGER.error(errorText);
            }
            if (error.size() > 0) {
                LOGGER.error(error.toString());
            }
            return result.substring(pos);
        } else {
            LOGGER.error("Result doesn't contain valid html: " + result);
            throw new TinyReportRenderException("Result doesn't contain valid html: " + result);
        }
    }

    public void setUnwrapSvg(boolean unwrapSvg) {
        this.unwrapSvg = unwrapSvg;
    }

    public void setSkipSvg(boolean skipSvg) {
        this.skipSvg = skipSvg;
    }
}
