/*Close InputStream in HostUtils

Issue 15126

visitFile was opening FileInputStreams but not closing them.

Change-Id:I4d57b51b3debc03ff9be4e244d251db9b5257584*/




//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/HostUtils.java b/tools/host/src/com/android/cts/HostUtils.java
//Synthetic comment -- index 6754274..37222cc 100644

//Synthetic comment -- @@ -46,10 +46,10 @@
*
*/
public class HostUtils {

private static SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",
Locale.ENGLISH);

/**
* Check if the given file exists
*
//Synthetic comment -- @@ -162,14 +162,21 @@
ZipEntry ze = new ZipEntry(path);
try {
zipOutputStream.putNextEntry(ze);
                InputStream is = null;
                try {
                    is = new BufferedInputStream(new FileInputStream(f));
                    byte[] buffer = new byte[4096];
                    int bytesRead = is.read(buffer);
                    while (bytesRead > 0) {
                        zipOutputStream.write(buffer, 0, bytesRead);
                        bytesRead = is.read(buffer);
                    }
                    zipOutputStream.closeEntry();
                } finally {
                    if (is != null) {
                        is.close();
                    }
}
} catch (IOException e) {
ok = false;
caughtException = e;
//Synthetic comment -- @@ -281,10 +288,10 @@

return fmt.toString();
}

/**
* Convert the given byte array into a lowercase hex string.
     *
* @param arr The array to convert.
* @return The hex encoded string.
*/
//Synthetic comment -- @@ -295,7 +302,7 @@
}
return buf.toString();
}

/**
* Strip control characters from the given string.
*/
//Synthetic comment -- @@ -307,7 +314,7 @@
public static Date dateFromString(String s) throws ParseException {
return dateFormat.parse(s);
}

public static String dateToString(Date d) {
return dateFormat.format(d);
}







