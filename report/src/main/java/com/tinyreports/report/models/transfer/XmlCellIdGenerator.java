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

package com.tinyreports.report.models.transfer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
class XmlCellIdGenerator {

	private static final XmlCellIdGenerator GENERATOR = new XmlCellIdGenerator();
	private static AtomicInteger ID = new AtomicInteger(0);

	private XmlCellIdGenerator() {
	}

	public static synchronized XmlCellIdGenerator getInstance(){
		return GENERATOR;
	}

	public static synchronized Integer getId(){
		return ID.incrementAndGet();
	}
}