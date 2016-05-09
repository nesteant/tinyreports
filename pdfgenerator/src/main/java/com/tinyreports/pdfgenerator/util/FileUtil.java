package com.tinyreports.pdfgenerator.util;

import com.tinyreports.common.exceptions.TinyReportRenderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Anton Nesterenko
 * @since 0.5.4
 */
public class FileUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static URL formUrl(File file) throws TinyReportRenderException {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new TinyReportRenderException(e);
        }
    }

    public static void tryToDeleteFileObject(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    tryToDeleteFileObject(file);
                } else {
                    if (!file.delete()) {
                        LOGGER.info("not deleted: {}", file.getName());
                    }
                }
            }
        }
        dir.delete();
    }

    public static File createFile(String dirName, String fileName) {
        return new File(new File(dirName).getAbsolutePath() + "/" + fileName);
    }

    public static String createCorrectFileName(String dirName, String fileName) {
        return createFile(dirName, fileName).getAbsolutePath();
    }

    public static File newFile(URL url) throws TinyReportRenderException {
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new TinyReportRenderException(e);
        }
    }

    public static File safeCreateDirectory(String dirName) {
        File dir = new File(dirName);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                return dir;
            }
        } else {
            boolean res = dir.mkdirs();
            if (res) {
                return dir;
            }
        }
        return null;
    }

    public static File safeRecreateDirectory(String dirName) {
        File dir = new File(dirName);
        tryToDeleteFileObject(dir);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                return dir;
            }
        } else {
            boolean res = dir.mkdirs();
            if (res) {
                return dir;
            }
        }
        return null;
    }
}
