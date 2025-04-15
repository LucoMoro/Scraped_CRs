/*StrictMode.permitCustomSlowCalls() should call disable(DETECT_CUSTOM)*/




//Synthetic comment -- diff --git a/core/java/android/os/StrictMode.java b/core/java/android/os/StrictMode.java
//Synthetic comment -- index ce213fb..f682abe 100644

//Synthetic comment -- @@ -407,17 +407,17 @@
}

/**
             * Enable detection of slow calls.
*/
public Builder detectCustomSlowCalls() {
return enable(DETECT_CUSTOM);
}

/**
             * Disable detection of slow calls.
*/
public Builder permitCustomSlowCalls() {
                return disable(DETECT_CUSTOM);
}

/**







