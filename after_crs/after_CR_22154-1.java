/*Fix to catch the NullpointerException.

'in.readLine' will return null if no characters were read before the end of the reader has been reached.
And then 'request.indexOf(' ');' will occur NullpointerException.

Change-Id:I1331a379c74829fb2533005fc0fe3d02db56b328*/




//Synthetic comment -- diff --git a/services/java/com/android/server/ViewServer.java b/services/java/com/android/server/ViewServer.java
old mode 100644
new mode 100755
//Synthetic comment -- index 7b5d18a..6af46fe

//Synthetic comment -- @@ -266,6 +266,8 @@
}
} catch(IOException e) {
Slog.w(LOG_TAG, "Connection error: ", e);
            } catch(NullPointerException e) {
                e.printStackTrace();
} finally {
if (in != null) {
try {







