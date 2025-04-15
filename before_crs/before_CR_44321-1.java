/*ContentValues: improves documentation

This corrects and clarifies the documentation for ContentValues.

Change-Id:Iddf54da093e97c32da2568eab8802b1b2715303bSigned-off-by: Steve Pomeroy <steve@staticfree.info>*/
//Synthetic comment -- diff --git a/core/java/android/content/ContentValues.java b/core/java/android/content/ContentValues.java
//Synthetic comment -- index 0d25f80..7ed827c 100644

//Synthetic comment -- @@ -230,11 +230,12 @@
}

/**
     * Gets a value. Valid value types are {@link String}, {@link Boolean}, and
     * {@link Number} implementations.
*
* @param key the value to get
     * @return the data for the value
*/
public Object get(String key) {
return mValues.get(key);
//Synthetic comment -- @@ -255,7 +256,7 @@
* Gets a value and converts it to a Long.
*
* @param key the value to get
     * @return the Long value, or null if the value is missing or cannot be converted
*/
public Long getAsLong(String key) {
Object value = mValues.get(key);
//Synthetic comment -- @@ -280,7 +281,7 @@
* Gets a value and converts it to an Integer.
*
* @param key the value to get
     * @return the Integer value, or null if the value is missing or cannot be converted
*/
public Integer getAsInteger(String key) {
Object value = mValues.get(key);
//Synthetic comment -- @@ -305,7 +306,7 @@
* Gets a value and converts it to a Short.
*
* @param key the value to get
     * @return the Short value, or null if the value is missing or cannot be converted
*/
public Short getAsShort(String key) {
Object value = mValues.get(key);
//Synthetic comment -- @@ -330,7 +331,7 @@
* Gets a value and converts it to a Byte.
*
* @param key the value to get
     * @return the Byte value, or null if the value is missing or cannot be converted
*/
public Byte getAsByte(String key) {
Object value = mValues.get(key);
//Synthetic comment -- @@ -355,7 +356,7 @@
* Gets a value and converts it to a Double.
*
* @param key the value to get
     * @return the Double value, or null if the value is missing or cannot be converted
*/
public Double getAsDouble(String key) {
Object value = mValues.get(key);
//Synthetic comment -- @@ -380,7 +381,7 @@
* Gets a value and converts it to a Float.
*
* @param key the value to get
     * @return the Float value, or null if the value is missing or cannot be converted
*/
public Float getAsFloat(String key) {
Object value = mValues.get(key);
//Synthetic comment -- @@ -405,7 +406,7 @@
* Gets a value and converts it to a Boolean.
*
* @param key the value to get
     * @return the Boolean value, or null if the value is missing or cannot be converted
*/
public Boolean getAsBoolean(String key) {
Object value = mValues.get(key);
//Synthetic comment -- @@ -428,7 +429,8 @@
* any other types to byte arrays.
*
* @param key the value to get
     * @return the byte[] value, or null is the value is missing or not a byte[]
*/
public byte[] getAsByteArray(String key) {
Object value = mValues.get(key);







