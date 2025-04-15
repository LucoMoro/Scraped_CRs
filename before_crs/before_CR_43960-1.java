/*Clear loaders array after they are destroyed.

Here is the story.
There is a bug. Decision about retaining state is made when
onRetainNonConfigurationInstance is called after onStop. I mean doRetain()
method is called in this case only. But it's possible that activity is
recreated because of a configuration change happening much further after
it was stopped. E. g. start an activity, navigate to another from it (stopping
the current), rotate the screen, press back. In this case loaders are destroyed,
not retained despite the configuration change nature of activity recreation.
Well, let it be... But loaders are destroyed (reset), and at the same time their
instances are still in that sparse array. As a result instance of the destroyed
loader is used again when new activity starts. The loader reloads its data
(since it was previously reset) but cannot deliver it to a callback since
LoaderInfo.mDestroyed is true.

So, I do not see any reason mLoaders array is not cleared after all the loaders
are destroyed. If it is cleared, everything should work well. A new loader will
be created, it will load data and deliver to a callback.

Btw, retain logic should be reconsidered to avoid the situation when loaders
are reset in case of the navigation described above.

Change-Id:I1b424e91e0fa92b925ec501729c0474de8a58df1Signed-off-by: Roman Mazur <mazur.roman@gmail.com>*/
//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/LoaderManager.java b/v4/java/android/support/v4/app/LoaderManager.java
//Synthetic comment -- index ca78413..9c18037c 100644

//Synthetic comment -- @@ -772,6 +772,7 @@
for (int i = mLoaders.size()-1; i >= 0; i--) {
mLoaders.valueAt(i).destroy();
}
}

if (DEBUG) Log.v(TAG, "Destroying Inactive in " + this);







