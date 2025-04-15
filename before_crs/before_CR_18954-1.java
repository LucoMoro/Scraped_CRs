/*DropBox: Read until the end of stream has been reached

Read the requested length or until the end of the input stream
has actually been reached.

Change-Id:I01bc0b81eca0225209bdd288dde6a778a19d1e2c*/
//Synthetic comment -- diff --git a/core/java/android/os/DropBoxManager.java b/core/java/android/os/DropBoxManager.java
//Synthetic comment -- index 7889a92..4a0612c 100644

//Synthetic comment -- @@ -150,7 +150,12 @@
try {
is = getInputStream();
byte[] buf = new byte[maxBytes];
                return new String(buf, 0, Math.max(0, is.read(buf)));
} catch (IOException e) {
return null;
} finally {







