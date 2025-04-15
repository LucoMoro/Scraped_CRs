/*Specify Content Length in CtsTestServer

Bug 2585913

AwesomePlayer doesn't send buffering updates if it doesn't know the
duration of the file...so specify a content length header to
appease it. This fixes MediaPlayerTest#testPlayStream which was
blocking on waiting for a buffering update.

Change-Id:I85b96fcff9d6c33be170990687d1a24817a779cf*/




//Synthetic comment -- diff --git a/tests/src/android/webkit/cts/CtsTestServer.java b/tests/src/android/webkit/cts/CtsTestServer.java
//Synthetic comment -- index 0d786fd..87b3d1d 100644

//Synthetic comment -- @@ -449,6 +449,7 @@
}
entity.setContentType(mimeType);
response.setEntity(entity);
                response.setHeader("Content-Length", "" + entity.getContentLength());
} catch (IOException e) {
response = null;
// fall through, return 404 at the end







