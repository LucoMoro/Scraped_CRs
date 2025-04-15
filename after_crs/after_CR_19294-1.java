/*use UTF-8 instead of ISO-8859-1

Change-Id:I226d8ab358a667cb3c37f76e81e5eda4f3f3b097*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/MultiLineReceiver.java b/ddms/libs/ddmlib/src/com/android/ddmlib/MultiLineReceiver.java
//Synthetic comment -- index 24dbb05..f3d9412 100644

//Synthetic comment -- @@ -51,7 +51,7 @@
if (isCancelled() == false) {
String s = null;
try {
                s = new String(data, offset, length, "UTF-8"); //$NON-NLS-1$
} catch (UnsupportedEncodingException e) {
// normal encoding didn't work, try the default one
s = new String(data, offset,length);







