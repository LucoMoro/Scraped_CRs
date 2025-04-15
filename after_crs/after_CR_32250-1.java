/*Fix for restarting loader manager after orientation change

After an orientation change, the recreated FragmentActivity creates a new
LoaderManagerImpl. Unfortunately this new LoaderManagerImpl does not get
started in onStart(), so loaders cease to load after the orientation change.
The bugfix simply starts the newly created mLoaderManager in onStart().

Change-Id:I35ebc338aad42fb699cd11f742edc7d999df8390*/




//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/FragmentActivity.java b/v4/java/android/support/v4/app/FragmentActivity.java
//Synthetic comment -- index 2112fb3..721e0db 100644

//Synthetic comment -- @@ -510,6 +510,12 @@
mLoaderManager.doStart();
} else if (!mCheckedForLoaderManager) {
mLoaderManager = getLoaderManager(-1, mLoadersStarted, false);
                // the returned loader manager may be a new one, so we have to start it
                if (mLoaderManager != null) {
                    if (!mLoaderManager.mStarted) {
                        mLoaderManager.doStart();
                    }
                }
}
mCheckedForLoaderManager = true;
}







