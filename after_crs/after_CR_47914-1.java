/*Hopefully fix PendingIntent.getActivities() documentation wtf.

The first intent is the key. No wait, last! Or was it first?

I haven't actually read the code, didn't write it, and haven't tested
its behaviour, but surely it can't be both, and last is the only one
that makes sense.

Change-Id:Ie8435981f09be618c93680fb6056afd015090161*/




//Synthetic comment -- diff --git a/core/java/android/app/PendingIntent.java b/core/java/android/app/PendingIntent.java
//Synthetic comment -- index d36d99d..01c0634 100644

//Synthetic comment -- @@ -289,7 +289,7 @@

/**
* Like {@link #getActivity(Context, int, Intent, int)}, but allows an
     * array of Intents to be supplied.  The last Intent in the array is
* taken as the primary key for the PendingIntent, like the single Intent
* given to {@link #getActivity(Context, int, Intent, int)}.  Upon sending
* the resulting PendingIntent, all of the Intents are started in the same
//Synthetic comment -- @@ -335,7 +335,7 @@

/**
* Like {@link #getActivity(Context, int, Intent, int)}, but allows an
     * array of Intents to be supplied.  The last Intent in the array is
* taken as the primary key for the PendingIntent, like the single Intent
* given to {@link #getActivity(Context, int, Intent, int)}.  Upon sending
* the resulting PendingIntent, all of the Intents are started in the same







