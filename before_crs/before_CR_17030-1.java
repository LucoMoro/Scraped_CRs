/*Fix PduPersister does not take care of encoding of PduPart text.

Give correct charset argument to EncodedStringValue() when storing text data.
Also, give the original charset arguement to .getBytes() when loading text data.

Change-Id:I5585a9f491965718fd30946030065d9e13b10154*/
//Synthetic comment -- diff --git a/core/java/com/google/android/mms/pdu/PduPersister.java b/core/java/com/google/android/mms/pdu/PduPersister.java
//Synthetic comment -- index d4ac24a..9f254f9 100644

//Synthetic comment -- @@ -424,8 +424,19 @@
// faster.
if ("text/plain".equals(type) || "application/smil".equals(type)) {
String text = c.getString(PART_COLUMN_TEXT);
                        byte [] blob = new EncodedStringValue(text != null ? text : "")
                            .getTextString();
baos.write(blob, 0, blob.length);
} else {

//Synthetic comment -- @@ -738,7 +749,12 @@
byte[] data = part.getData();
if ("text/plain".equals(contentType) || "application/smil".equals(contentType)) {
ContentValues cv = new ContentValues();
                cv.put(Telephony.Mms.Part.TEXT, new EncodedStringValue(data).getString());
if (mContentResolver.update(uri, cv, null, null) != 1) {
throw new MmsException("unable to update " + uri.toString());
}







