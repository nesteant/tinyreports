package com.tinyreports.report.resolvers;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ResolverFactory {
    private static ElResolver elResolver = new ElResolver();
    private static SimpleResolver simpleResolver = new SimpleResolver();

    public static RelationResolver getResolver(Class resolverClass) {
        if (ElResolver.class.equals(resolverClass)) {
            return elResolver;
        } else if (SimpleResolver.class.equals(resolverClass)) {
            return simpleResolver;
        } else {
            throw new IllegalStateException(String.format("%s class is not supported", resolverClass));
        }
    }
}
