/*Fix AndroidHttpClient documentation error.

AndroidHttpClient doesn't let the user add HttpRequestInterceptor classes.

Bug:http://code.google.com/p/android/issues/detail?id=37294Change-Id:I66f6581fa321dc16ddfe35c8f4242bc31f79bdfa*/




//Synthetic comment -- diff --git a/core/java/android/net/http/AndroidHttpClient.java b/core/java/android/net/http/AndroidHttpClient.java
//Synthetic comment -- index c534e58..fabe018 100644

//Synthetic comment -- @@ -66,8 +66,7 @@

/**
* Implementation of the Apache {@link DefaultHttpClient} that is configured with
 * reasonable default settings and registered schemes for Android.
* Don't create this directly, use the {@link #newInstance} factory method.
*
* <p>This client processes cookies but does not retain them by default.







