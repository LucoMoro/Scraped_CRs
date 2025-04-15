/*New env-var to disable SDK Manager cache

Change-Id:Ia1c0ae789f173aca0832c1c7c1f6679645611f9b*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index 039e165..c4feade 100755

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








