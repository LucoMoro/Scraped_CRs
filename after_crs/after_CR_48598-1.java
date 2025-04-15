/*TOMBSTONE happens in "com.android.browser" at SIGSEGV

It is monkey issue, add the flag to avoid the delete
method executed many times.

Change-Id:I78e21b4d4095ae5a95f0d786f9dd2f1fb405a23cAuthor: Yijun Zhu <yijunx.zhu@intel.com>
Signed-off-by: Yijun Zhu <yijunx.zhu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 63778*/




//Synthetic comment -- diff --git a/core/java/android/webkit/BrowserFrame.java b/core/java/android/webkit/BrowserFrame.java
//Synthetic comment -- index 4dbca23..9aa49b4 100644

//Synthetic comment -- @@ -1096,12 +1096,16 @@
}

SslErrorHandler handler = new SslErrorHandler() {
            boolean isCanceled = false;
@Override
public void proceed() {
SslCertLookupTable.getInstance().setIsAllowed(sslError);
post(new Runnable() {
public void run() {
                            if (!isCanceled)
                            {
                               nativeSslCertErrorProceed(handle);
                            }
}
});
}
//Synthetic comment -- @@ -1109,7 +1113,11 @@
public void cancel() {
post(new Runnable() {
public void run() {
                            if (!isCanceled)
                            {
                               nativeSslCertErrorCancel(handle, certError);
                            }
                            isCanceled = true;
}
});
}







