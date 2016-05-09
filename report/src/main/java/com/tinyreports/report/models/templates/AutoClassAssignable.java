package com.tinyreports.report.models.templates;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public interface AutoClassAssignable {
    String getId();

    void addAttribute(String attrName, String attrValue);
}
