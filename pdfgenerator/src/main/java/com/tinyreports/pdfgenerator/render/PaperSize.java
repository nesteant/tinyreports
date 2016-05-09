package com.tinyreports.pdfgenerator.render;

/**
 * @author Anton Nesterenko
 * @since 0.5.4
 */
public enum PaperSize {
    A4V(7.6f, 7.6f), A4H(7.6f, 7.6f);
    private Float inchWidth;
    private Float inchHeight;

    private PaperSize(Float inchWidth, Float inchHeight) {
        this.inchWidth = inchWidth;
        this.inchHeight = inchHeight;
    }

    public Float getInchWidth() {
        return inchWidth;
    }

    public Float getInchHeight() {
        return inchHeight;
    }
}
