/*Pass attachment mime-type from Email app to viewer app*/
//Synthetic comment -- diff --git a/src/com/android/email/activity/MessageView.java b/src/com/android/email/activity/MessageView.java
//Synthetic comment -- index 0000443..bf9f047 100644

//Synthetic comment -- @@ -1542,7 +1542,7 @@
} else {
try {
Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(contentUri);
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
startActivity(intent);
} catch (ActivityNotFoundException e) {







