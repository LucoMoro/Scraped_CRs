/*Don't crash zygote if webcore fails to load

Change-Id:I88271fb62c0cc7c6d768009a713f20d9dfbfb5eb*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewCore.java b/core/java/android/webkit/WebViewCore.java
//Synthetic comment -- index f54b207..e36602f 100644

//Synthetic comment -- @@ -58,7 +58,11 @@
// Load libwebcore during static initialization. This happens in the
// zygote process so it will be shared read-only across all app
// processes.
        try {
            System.loadLibrary("webcore");
        } catch (UnsatisfiedLinkError e) {
            Log.e(LOGTAG, "Unable to load webcore library");
        }
}

/*







