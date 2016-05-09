package com.tinyreports.report;

import com.tinyreports.report.models.transfer.XmlCell;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public enum AggregationFunction {
    SUM {
        @Override
        public Number executeAggregationFunction(List<XmlCell> cells) {
            Double sumResult = 0d;
            for (XmlCell cell : cells) {
                String value = cell.getContent();
                try {
                    if (value != null && !value.isEmpty()) {
                        sumResult += Double.valueOf(value);
                    }
                } catch (NumberFormatException ignore) {
                    LOGGER.trace("Value {} is not a Number", value);
                }
            }
            return sumResult;
        }
    }, COUNT {
        @Override
        public Number executeAggregationFunction(List<XmlCell> cells) {
            Integer countResult = 0;
            for (XmlCell xmlCell : cells) {
                String value = xmlCell.getContent();
                try {
                    if (value != null && !value.isEmpty()) {
                        Double.valueOf(value).intValue();
                        countResult++;
                    }
                } catch (NumberFormatException ignore) {
                    LOGGER.trace("Value {} is not a Number", value);
                }
            }
            return countResult;
        }
    }, AVERAGE {
        @Override
        public Number executeAggregationFunction(List<XmlCell> cells) {
            Double averageResult = 0d;
            for (XmlCell cell : cells) {
                String value = cell.getContent();
                try {
                    if (value != null && !value.isEmpty()) {
                        averageResult += Double.valueOf(value).intValue();
                    }
                } catch (NumberFormatException ignore) {
                    LOGGER.trace("Value {} is not a Number", value);
                }
            }
            return averageResult / cells.size();
        }
    };
    private static Logger LOGGER = LoggerFactory.getLogger(AggregationFunction.class);

    public String executeAggregationFunction(List<XmlCell> cells, String format) {
        Number result = executeAggregationFunction(cells);
        if (StringUtils.isEmpty(format)) {
            return result.toString();
        } else {
            return formatOutputString(result, format);
        }
    }

    public abstract Number executeAggregationFunction(List<XmlCell> cells);

    private static String formatOutputString(Number number, String format) {
        return new DecimalFormat(format, new DecimalFormatSymbols(Locale.ENGLISH)).format(number);
    }
}
