//<Beginning of snippet n. 0>

/**
 * Gets a value. Valid value types are {@link String}, {@link Boolean}, and
 * {@link Number} implementations.
 *
 * @param key the key for the value to get, must not be null
 * @return the data for the value or null if the value is missing, not convertible,
 *         or if the type is unsupported.
 */
public Object get(String key) {
    if (key == null) {
        throw new IllegalArgumentException("Key must not be null");
    }
    return mValues.get(key);
}

/**
 * Gets a value and converts it to a Long.
 *
 * @param key the key for the value to get, must not be null
 * @return the Long value, or null if the value is missing, cannot be converted,
 *         or if the type is unsupported.
 */
public Long getAsLong(String key) {
    if (key == null) {
        throw new IllegalArgumentException("Key must not be null");
    }
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).longValue();
    }
    return null;
}

/**
 * Gets a value and converts it to an Integer.
 *
 * @param key the key for the value to get, must not be null
 * @return the Integer value, or null if the value is missing, cannot be converted,
 *         or if the type is unsupported.
 */
public Integer getAsInteger(String key) {
    if (key == null) {
        throw new IllegalArgumentException("Key must not be null");
    }
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).intValue();
    }
    return null;
}

/**
 * Gets a value and converts it to a Short.
 *
 * @param key the key for the value to get, must not be null
 * @return the Short value, or null if the value is missing, cannot be converted,
 *         or if the type is unsupported.
 */
public Short getAsShort(String key) {
    if (key == null) {
        throw new IllegalArgumentException("Key must not be null");
    }
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).shortValue();
    }
    return null;
}

/**
 * Gets a value and converts it to a Byte.
 *
 * @param key the key for the value to get, must not be null
 * @return the Byte value, or null if the value is missing, cannot be converted,
 *         or if the type is unsupported.
 */
public Byte getAsByte(String key) {
    if (key == null) {
        throw new IllegalArgumentException("Key must not be null");
    }
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).byteValue();
    }
    return null;
}

/**
 * Gets a value and converts it to a Double.
 *
 * @param key the key for the value to get, must not be null
 * @return the Double value, or null if the value is missing, cannot be converted,
 *         or if the type is unsupported.
 */
public Double getAsDouble(String key) {
    if (key == null) {
        throw new IllegalArgumentException("Key must not be null");
    }
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).doubleValue();
    }
    return null;
}

/**
 * Gets a value and converts it to a Float.
 *
 * @param key the key for the value to get, must not be null
 * @return the Float value, or null if the value is missing, cannot be converted,
 *         or if the type is unsupported.
 */
public Float getAsFloat(String key) {
    if (key == null) {
        throw new IllegalArgumentException("Key must not be null");
    }
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).floatValue();
    }
    return null;
}

/**
 * Gets a value and converts it to a Boolean.
 *
 * @param key the key for the value to get, must not be null
 * @return the Boolean value, or null if the value is missing, cannot be converted,
 *         or if the type is unsupported.
 */
public Boolean getAsBoolean(String key) {
    if (key == null) {
        throw new IllegalArgumentException("Key must not be null");
    }
    Object value = mValues.get(key);
    if (value instanceof Boolean) {
        return (Boolean) value;
    }
    return null;
}

/**
 * Gets a value and converts it to a byte array.
 *
 * @param key the key for the value to get, must not be null
 * @return the byte[] value, or null if the value is missing, not a byte[],
 *         or if the type is unsupported.
 */
public byte[] getAsByteArray(String key) {
    if (key == null) {
        throw new IllegalArgumentException("Key must not be null");
    }
    Object value = mValues.get(key);
    if (value instanceof byte[]) {
        return (byte[]) value;
    }
    return null;
}

//<End of snippet n. 0>