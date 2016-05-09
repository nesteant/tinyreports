package com.tinyreports.common.utils;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Anton Nesterenko
 * @since 0.5.4
 */
public class Pair<K,V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Pair() {
    }

    public K getKey() {
        return key;
    }

    public void setKey( K key ) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue( V value ) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals( final Object other ) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

	@Override
	public String toString() {
		return new StringBuilder("Pair{")
				.append("key=").append(key).append(", ")
				.append("value=").append(value)
				.append("}").toString();
	}
}
