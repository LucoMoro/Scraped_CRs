//<Beginning of snippet n. 0>
/**
 * Gets a value. Valid value types are {@link String}, {@link Boolean}, and
 * {@link Number} implementations.
 *
 * @param key the value to get, should be a valid key format (e.g., "keyName")
 * @return the data for the value, or null if the value is missing, cannot be converted, or is of an unsupported type
 */
public Object get(String key) {
    return mValues.get(key);
}

/**
 * Gets a value and converts it to a Long.
 *
 * @param key the value to get, should be a valid key format (e.g., "keyName")
 * @return the Long value, or null if the value is missing, cannot be converted, or is of an unsupported type
 * @throws ClassCastException if the value cannot be converted to Long
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
 * @param key the value to get, should be a valid key format (e.g., "keyName")
 * @return the Integer value, or null if the value is missing, cannot be converted, or is of an unsupported type
 * @throws ClassCastException if the value cannot be converted to Integer
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
 * @param key the value to get, should be a valid key format (e.g., "keyName")
 * @return the Short value, or null if the value is missing, cannot be converted, or is of an unsupported type
 * @throws ClassCastException if the value cannot be converted to Short
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
 * @param key the value to get, should be a valid key format (e.g., "keyName")
 * @return the Byte value, or null if the value is missing, cannot be converted, or is of an unsupported type
 * @throws ClassCastException if the value cannot be converted to Byte
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
 * @param key the value to get, should be a valid key format (e.g., "keyName")
 * @return the Double value, or null if the value is missing, cannot be converted, or is of an unsupported type
 * @throws ClassCastException if the value cannot be converted to Double
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
 * @param key the value to get, should be a valid key format (e.g., "keyName")
 * @return the Float value, or null if the value is missing, cannot be converted, or is of an unsupported type
 * @throws ClassCastException if the value cannot be converted to Float
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
 * @param key the value to get, should be a valid key format (e.g., "keyName")
 * @return the Boolean value, or null if the value is missing, cannot be converted, or is of an unsupported type
 * @throws ClassCastException if the value cannot be converted to Boolean
 */
public Boolean getAsBoolean(String key) {
    Object value = mValues.get(key);
    if (value instanceof Boolean) {
        return (Boolean) value;
    } else if (value instanceof String) {
        return Boolean.parseBoolean((String) value);
    }
    return null;
}

/**
 * Gets a value and converts it to a byte array.
 *
 * @param key the value to get, should be a valid key format (e.g., "keyName")
 * @return the byte[] value, or null if the value is missing or not a byte[]
 * @throws ClassCastException if the value cannot be converted to byte[]
 */
public byte[] getAsByteArray(String key) {
    Object value = mValues.get(key);
    if (value instanceof byte[]) {
        return (byte[]) value;
    }
    return null;
}
//<End of snippet n. 0>