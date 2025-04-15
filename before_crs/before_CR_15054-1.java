/*Add better error handling for savePicture and restorePicture

If an Exception occurs when storing the file treat this as an error
and always fail to try to prevent corrupted pictures to be stored to
the file system.

Close files if they were opened, the caller might want to perform other
file operations on the file and if it is still open these may fail.

Change-Id:Ic68596b5c745bbe413096c22684c388e853a7643*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index c5c14d3..de450fc 100644

//Synthetic comment -- @@ -1182,18 +1182,29 @@
return false;
}
final Picture p = capturePicture();
try {
            final FileOutputStream out = new FileOutputStream(dest);
p.writeToStream(out);
            out.close();
} catch (FileNotFoundException e){
e.printStackTrace();
} catch (IOException e) {
e.printStackTrace();
} catch (RuntimeException e) {
e.printStackTrace();
}
        if (dest.length() > 0) {
b.putInt("scrollX", mScrollX);
b.putInt("scrollY", mScrollY);
b.putFloat("scale", mActualScale);
//Synthetic comment -- @@ -1217,16 +1228,23 @@
}
if (src.exists()) {
Picture p = null;
try {
                final FileInputStream in = new FileInputStream(src);
p = Picture.createFromStream(in);
                in.close();
} catch (FileNotFoundException e){
e.printStackTrace();
} catch (RuntimeException e) {
e.printStackTrace();
} catch (IOException e) {
e.printStackTrace();
}
if (p != null) {
int sx = b.getInt("scrollX", 0);







