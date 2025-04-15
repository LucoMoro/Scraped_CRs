/*start newly created LoaderManager in onStart

After device rotation, onStart is called and the loader managers are started.
In case a new loader manager has been created by getLoaderManager, this new
loader manager has to be started as well. This fix simply starts the loader
manager if it has not been started previously.

Change-Id:If1b1943f50a04831c388e3d29fdbea1b4c665ad0*/
//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/FragmentActivity.java b/v4/java/android/support/v4/app/FragmentActivity.java
//Synthetic comment -- index 2112fb3..964a5f6 100644

//Synthetic comment -- @@ -510,6 +510,12 @@
mLoaderManager.doStart();
} else if (!mCheckedForLoaderManager) {
mLoaderManager = getLoaderManager(-1, mLoadersStarted, false);
}
mCheckedForLoaderManager = true;
}







