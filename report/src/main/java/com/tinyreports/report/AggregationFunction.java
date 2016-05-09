/*
 * Tinyreports
 * Copyright (c) 2013. Anton Nesterenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
