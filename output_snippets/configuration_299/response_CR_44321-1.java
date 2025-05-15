//<Beginning of snippet n. 0>
}

/**
 * Gets a value. Valid value types are {@link String}, {@link Boolean}, and
 * {@link Number} implementations.
 *
 * @param key the value to get
 * @return the data for the value, or null if the key is invalid or the value cannot be converted
 */
public Object get(String key) {
    Object value = mValues.get(key);
    if (value == null) {
        return null;
    }
    return value; 
}

/**
 * Gets a value and converts it to a Long.
 *
 * @param key the value to get
 * @return the Long value, or null if the value is missing or cannot be converted
 */
public Long getAsLong(String key) {
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).longValue();
    }
    return null;
}

/**
 * Gets a value and converts it to an Integer.
 *
 * @param key the value to get
 * @return the Integer value, or null if the value is missing or cannot be converted
 */
public Integer getAsInteger(String key) {
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).intValue();
    }
    return null;
}

/**
 * Gets a value and converts it to a Short.
 *
 * @param key the value to get
 * @return the Short value, or null if the value is missing or cannot be converted
 */
public Short getAsShort(String key) {
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).shortValue();
    }
    return null;
}

/**
 * Gets a value and converts it to a Byte.
 *
 * @param key the value to get
 * @return the Byte value, or null if the value is missing or cannot be converted
 */
public Byte getAsByte(String key) {
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).byteValue();
    }
    return null;
}

/**
 * Gets a value and converts it to a Double.
 *
 * @param key the value to get
 * @return the Double value, or null if the value is missing or cannot be converted
 */
public Double getAsDouble(String key) {
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).doubleValue();
    }
    return null;
}

/**
 * Gets a value and converts it to a Float.
 *
 * @param key the value to get
 * @return the Float value, or null if the value is missing or cannot be converted
 */
public Float getAsFloat(String key) {
    Object value = mValues.get(key);
    if (value instanceof Number) {
        return ((Number) value).floatValue();
    }
    return null;
}

/**
 * Gets a value and converts it to a Boolean.
 *
 * @param key the value to get
 * @return the Boolean value, or null if the value is missing or cannot be converted
 */
public Boolean getAsBoolean(String key) {
    Object value = mValues.get(key);
    if (value instanceof Boolean) {
        return (Boolean) value;
    } else if (value instanceof String) {
        return Boolean.valueOf((String) value);
    }
    return null;
}

/**
 * Gets a value and converts it to a byte array.
 *
 * @param key the value to get
 * @return the byte[] value, or null if the value is missing or not a byte[]
 */
public byte[] getAsByteArray(String key) {
    Object value = mValues.get(key);
    if (value instanceof byte[]) {
        return (byte[]) value;
    }
    return null;
}

//<End of snippet n. 0>