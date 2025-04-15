/*WebViewTest Delayed Check for savePicture

Bug 2814482

The file saving is done in a separate thread in WebView, so wait a
bit when checking for the file's size.

It probably should be noted in the JavaDoc that this is an
asynchronous call. Furthermore, the method never returns false even
if the file saving failed, because the saving is done in a Runnable
that will fail silently...

Change-Id:Ied27fbb0b09f8fd7090cbf7392d81d81a9b91ec5*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
//Synthetic comment -- index 720a0c2..0a0ef62 100644

//Synthetic comment -- @@ -672,13 +672,25 @@

final Bundle bundle = new Bundle();
final File f = getActivity().getFileStreamPath("snapshot");
        if (f.exists()) {
            f.delete();
        }

try {
assertTrue(bundle.isEmpty());
assertEquals(0, f.length());
assertTrue(mWebView.savePicture(bundle, f));

            // File saving is done in a separate thread.
            new DelayedCheck() {
                @Override
                protected boolean check() {
                    return f.length() > 0;
                }
            }.run();

assertFalse(bundle.isEmpty());

Picture p = Picture.createFromStream(new FileInputStream(f));
Bitmap b = Bitmap.createBitmap(p.getWidth(), p.getHeight(), Config.ARGB_8888);
p.draw(new Canvas(b));







