//<Beginning of snippet n. 0>
}

/**
 * Gets a value. Valid value types are {@link String}, {@link Boolean}, and
 * {@link Number} implementations.
 *
 * @param key the value to get
 * @return the data for the value, or null if the key is missing
 */
public Object get(String key) {
    return mValues.get(key);
}

/**
 * Gets a value and converts it to a Long.
 *
 * @param key the value to get
 * @return the Long value, or null if the value is missing or cannot be converted
 */
public Long getAsLong(String key) {
    Object value = mValues.get(key);
    if (value == null) return null;
    try {
        return Long.valueOf(value.toString());
    } catch (NumberFormatException e) {
        return null;
    }
}

/**
 * Gets a value and converts it to an Integer.
 *
 * @param key the value to get
 * @return the Integer value, or null if the value is missing or cannot be converted
 */
public Integer getAsInteger(String key) {
    Object value = mValues.get(key);
    if (value == null) return null;
    try {
        return Integer.valueOf(value.toString());
    } catch (NumberFormatException e) {
        return null;
    }
}

/**
 * Gets a value and converts it to a Short.
 *
 * @param key the value to get
 * @return the Short value, or null if the value is missing or cannot be converted
 */
public Short getAsShort(String key) {
    Object value = mValues.get(key);
    if (value == null) return null;
    try {
        return Short.valueOf(value.toString());
    } catch (NumberFormatException e) {
        return null;
    }
}

/**
 * Gets a value and converts it to a Byte.
 *
 * @param key the value to get
 * @return the Byte value, or null if the value is missing or cannot be converted
 */
public Byte getAsByte(String key) {
    Object value = mValues.get(key);
    if (value == null) return null;
    try {
        return Byte.valueOf(value.toString());
    } catch (NumberFormatException e) {
        return null;
    }
}

/**
 * Gets a value and converts it to a Double.
 *
 * @param key the value to get
 * @return the Double value, or null if the value is missing or cannot be converted
 */
public Double getAsDouble(String key) {
    Object value = mValues.get(key);
    if (value == null) return null;
    try {
        return Double.valueOf(value.toString());
    } catch (NumberFormatException e) {
        return null;
    }
}

/**
 * Gets a value and converts it to a Float.
 *
 * @param key the value to get
 * @return the Float value, or null if the value is missing or cannot be converted
 */
public Float getAsFloat(String key) {
    Object value = mValues.get(key);
    if (value == null) return null;
    try {
        return Float.valueOf(value.toString());
    } catch (NumberFormatException e) {
        return null;
    }
}

/**
 * Gets a value and converts it to a Boolean.
 *
 * @param key the value to get
 * @return the Boolean value, or null if the value is missing or cannot be converted
 */
public Boolean getAsBoolean(String key) {
    Object value = mValues.get(key);
    if (value == null) return null;
    return Boolean.valueOf(value.toString());
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