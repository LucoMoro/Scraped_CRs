/*Add detail messages to all remaining NullPointerExceptions.

I've left java.util.concurrent alone, since that's upstream code.

Change-Id:I349960aaddb78e55d4c336b58b637009db69ff98*/




//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/VMDebug.java b/dalvik/src/main/java/dalvik/system/VMDebug.java
//Synthetic comment -- index ace149c..8f40165 100644

//Synthetic comment -- @@ -165,11 +165,10 @@
* @param flags flags to control method tracing. The only one that
* is currently defined is {@link #TRACE_COUNT_ALLOCS}.
*/
    public static void startMethodTracing(String traceFileName, int bufferSize, int flags) {

if (traceFileName == null) {
            throw new NullPointerException("traceFileName == null");
}

startMethodTracingNative(traceFileName, null, bufferSize, flags);
//Synthetic comment -- @@ -183,8 +182,11 @@
public static void startMethodTracing(String traceFileName,
FileDescriptor fd, int bufferSize, int flags)
{
        if (traceFileName == null) {
            throw new NullPointerException("traceFileName == null");
        }
        if (fd == null) {
            throw new NullPointerException("fd == null");
}

startMethodTracingNative(traceFileName, fd, bufferSize, flags);
//Synthetic comment -- @@ -291,15 +293,16 @@
*
* The VM may create a temporary file in the same directory.
*
     * @param filename Full pathname of output file (e.g. "/sdcard/dump.hprof").
* @throws UnsupportedOperationException if the VM was built without
*         HPROF support.
* @throws IOException if an error occurs while opening or writing files.
*/
    public static void dumpHprofData(String filename) throws IOException {
        if (filename == null) {
            throw new NullPointerException("filename == null");
        }
        dumpHprofData(filename, null);
}

/**








//Synthetic comment -- diff --git a/dalvik/src/main/java/org/apache/harmony/dalvik/ddmc/DdmServer.java b/dalvik/src/main/java/org/apache/harmony/dalvik/ddmc/DdmServer.java
//Synthetic comment -- index 61bee34..7717fd9 100644

//Synthetic comment -- @@ -50,9 +50,9 @@
* Throws an exception if the type already has a handler registered.
*/
public static void registerHandler(int type, ChunkHandler handler) {
        if (handler == null) {
            throw new NullPointerException("handler == null");
        }
synchronized (mHandlerMap) {
if (mHandlerMap.get(type) != null)
throw new RuntimeException("type " + Integer.toHexString(type)
//Synthetic comment -- @@ -171,4 +171,3 @@
return handler.handleChunk(chunk);
}
}








//Synthetic comment -- diff --git a/json/src/main/java/org/json/JSONObject.java b/json/src/main/java/org/json/JSONObject.java
//Synthetic comment -- index e7ca735..4e03b5a 100644

//Synthetic comment -- @@ -131,7 +131,7 @@
*/
String key = (String) entry.getKey();
if (key == null) {
                throw new NullPointerException("key == null");
}
nameValuePairs.put(key, entry.getValue());
}








//Synthetic comment -- diff --git a/json/src/main/java/org/json/JSONTokener.java b/json/src/main/java/org/json/JSONTokener.java
//Synthetic comment -- index ebfabe4..202e2e6 100644

//Synthetic comment -- @@ -543,7 +543,7 @@
*/
public String nextTo(String excluded) {
if (excluded == null) {
            throw new NullPointerException("excluded == null");
}
return nextToInternal(excluded).trim();
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/channels/spi/AbstractSelectableChannel.java b/luni/src/main/java/java/nio/channels/spi/AbstractSelectableChannel.java
//Synthetic comment -- index ab76e8e..4035090 100644

//Synthetic comment -- @@ -143,7 +143,7 @@
throw new IllegalSelectorException();
}
// throw NPE exactly to keep consistency
                throw new NullPointerException("selector not open");
}
SelectionKey key = keyFor(selector);
if (key == null) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/ListResourceBundle.java b/luni/src/main/java/java/util/ListResourceBundle.java
//Synthetic comment -- index fc6ab97..7809b9a 100644

//Synthetic comment -- @@ -119,7 +119,7 @@
table = new HashMap<String, Object>(contents.length / 3 * 4 + 3);
for (Object[] content : contents) {
if (content[0] == null || content[1] == null) {
                    throw new NullPointerException("null entry");
}
table.put((String) content[0], content[1]);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Locale.java b/luni/src/main/java/java/util/Locale.java
//Synthetic comment -- index 51636a7..3f1874d 100644

//Synthetic comment -- @@ -271,7 +271,9 @@
*/
public Locale(String language, String country, String variant) {
if (language == null || country == null || variant == null) {
            throw new NullPointerException("language=" + language +
                                           ",country=" + country +
                                           ",variant=" + variant);
}
if (language.isEmpty() && country.isEmpty()) {
languageCode = "";
//Synthetic comment -- @@ -564,7 +566,7 @@
*/
public synchronized static void setDefault(Locale locale) {
if (locale == null) {
            throw new NullPointerException("locale == null");
}
defaultLocale = locale;
}







