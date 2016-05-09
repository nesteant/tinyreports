package com.tinyreports.pdfgenerator.render;

import com.tinyreports.common.exceptions.TinyReportRenderException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Anton Nesterenko
 */
public class PhantomSvgRenderer {
    private static Logger LOGGER = LoggerFactory.getLogger(PhantomSvgRenderer.class);
    private static final String IMAGE_EXTENSION = "png";
    private static final String IMG_STRING = "<img src=\"%s\" width=\"%s\" height=\"%s\"/>";
    private static final String SVG_TEMP_FILE = "svgTemp";
    private static final Pattern SVG_HEIGHT_PATTERN = Pattern.compile("^<svg[^>]*height=['\"](\\d*)['\"]");
    private static final Pattern SVG_WIDTH_PATTERN = Pattern.compile("^<svg[^>]*width=['\"](\\d*)['\"]");
    private String htmlString;
    private File imgFolder;

    public PhantomSvgRenderer(String htmlString, File imgFolder) {
        this.htmlString = htmlString;
        this.imgFolder = imgFolder;
    }

    public String extract(final String executable, final String script) throws TinyReportRenderException {
        while (htmlString.contains("<svg")) {
            int start = htmlString.indexOf("<svg");
            int end = htmlString.indexOf("</svg>") + "</svg>".length();
            String svgStr = htmlString.substring(start, end);
            String uuid = UUID.randomUUID().toString();
            final String preparedString = prepareSVG(svgStr);
            final String height;
            final String width;
            try {
                Matcher heightMatcher = SVG_HEIGHT_PATTERN.matcher(preparedString);
                Matcher widthMatcher = SVG_WIDTH_PATTERN.matcher(preparedString);
                heightMatcher.find(0);
                height = heightMatcher.group(1);
                widthMatcher.find(0);
                width = widthMatcher.group(1);
            } catch (Exception e) {
                throw new TinyReportRenderException("Error during svg size evaluation", e);
            }
            final String absoluteFileName = String.format("%s/%s.%s", imgFolder.getAbsolutePath(), uuid, IMAGE_EXTENSION);
            String currentImageTag;
            try {
                currentImageTag = String.format(IMG_STRING, new File(absoluteFileName).toURI().toURL(), width, height);
            } catch (MalformedURLException e) {
                throw new TinyReportRenderException(e);
            }
            htmlString = htmlString.replace(svgStr, currentImageTag);
            final File svgTemp = FileUtils.getFile(imgFolder, SVG_TEMP_FILE);
            Writer wr = null;
            try {
                wr = new FileWriter(svgTemp);
                wr.append(preparedString);
            } catch (IOException e) {
                throw new TinyReportRenderException(e);
            } finally {
                IOUtils.closeQuietly(wr);
            }
            List<String> params = new ArrayList<String>() {{
                add(svgTemp.getAbsolutePath());
                add(width);
                add(height);
                add(absoluteFileName);
            }};
            ByteArrayOutputStream info = new ByteArrayOutputStream();
            ByteArrayOutputStream error = new ByteArrayOutputStream();
            PhantomJsRuntimeExecutor.phantomExec(executable, script, params, info, error);
            String infoMsg = info.toString();
            String errorMsg = error.toString();
            if (StringUtils.isNotEmpty(infoMsg) || StringUtils.isNotEmpty(errorMsg)) {
                LOGGER.error("INFO OUT: {}. ERROR OUT: {}", infoMsg, errorMsg);
            }
        }
        return htmlString;
    }

    protected static String prepareSVG(String svg) {
        svg = svg
                .replaceAll("visibility=\"\"", "")
                .replaceAll("zIndex=\"[^\"]\"", "")
                .replaceAll("isShadow=\"[^\"]\"", "")
                .replaceAll("symbolName=\"[^\"]\"", "")
                .replaceAll("jQuery[0-9]+=\"[^\"]\"", "")
                .replaceAll("isTracker=\"[^\"]\"", "")
                .replaceAll("url\\([^#]+#", "url('#")
                .replaceAll("&nbsp;", "\u00A0") // no-break space
                .replaceAll("&shy;", "\u00AD"); // soft hyphen
        return svg;
    }
}
