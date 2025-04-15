/*Fix a crashing on tapping an empty NDEF tag

NdefRecord.toMimeType() may return null so the result should be checked
before parsing.

Change-Id:I8c43365e15dcb7f8d42cfc581b487f472605f89f*/
//Synthetic comment -- diff --git a/src/com/android/apps/tag/record/ImageRecord.java b/src/com/android/apps/tag/record/ImageRecord.java
//Synthetic comment -- index 34af2ee..8a705a5 100644

//Synthetic comment -- @@ -52,6 +52,9 @@

public static ImageRecord parse(NdefRecord record) {
String mimeType = record.toMimeType();
Preconditions.checkArgument(mimeType.startsWith("image/"));

// Try to ensure it's a legal, valid image







