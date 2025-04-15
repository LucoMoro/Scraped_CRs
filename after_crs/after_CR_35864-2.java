/*New env-var to disable SDK Manager cache

Change-Id:Ia1c0ae789f173aca0832c1c7c1f6679645611f9b*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index 039e165..5250527 100755

//Synthetic comment -- @@ -71,7 +71,7 @@
*     http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html#sec6.1.1
*/

    private static final boolean DEBUG = System.getenv("SDKMAN_DEBUG_CACHE") != null; //$NON-NLS-1$

/** Key for the Status-Code in the info properties. */
private static final String KEY_STATUS_CODE = "Status-Code";        //$NON-NLS-1$
//Synthetic comment -- @@ -156,6 +156,12 @@
/** Creates a default instance of the URL cache */
public DownloadCache(Strategy strategy) {
mCacheRoot = initCacheRoot();

        // If this is defined in the environment, never use the cache. Useful for testing.
        if (System.getenv("SDKMAN_DISABLE_CACHE") != null) {
            strategy = Strategy.DIRECT;
        }

mStrategy = mCacheRoot == null ? Strategy.DIRECT : strategy;
}








