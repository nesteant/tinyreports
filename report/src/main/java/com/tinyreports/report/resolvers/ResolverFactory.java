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

package com.tinyreports.report.resolvers;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ResolverFactory {

	private static ElResolver elResolver = new ElResolver();
	private static SimpleResolver simpleResolver = new SimpleResolver();

	public static RelationResolver getResolver(Class resolverClass){

		if (ElResolver.class.equals(resolverClass)){
			return elResolver;
		} else if (SimpleResolver.class.equals(resolverClass)){
			return simpleResolver;
		} else {
			throw new IllegalStateException(String.format("%s class is not supported", resolverClass));
		}
	}
}
