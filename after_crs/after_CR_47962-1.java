/*Fix misleading PendingIntent documentation.

The javadoc for PendingIntent.getActivity() and others says that requestCode
parameters is "currently not used". To me that implies that its value doesn't
affect anything, but in fact it does. It even has a useful function: to work
around the unexpected behaviour that different extras don't make intents
different. I added more of a hint about that too, since it is a common source
of surprise and frustration (as mentioned in the introduction).

Change-Id:Ica8d24068ee2ea5e5176b81a21682970ae6ca737*/




//Synthetic comment -- diff --git a/core/java/android/app/PendingIntent.java b/core/java/android/app/PendingIntent.java
//Synthetic comment -- index d36d99d..51082c2 100644

//Synthetic comment -- @@ -200,10 +200,15 @@
* existing activity, so you must use the {@link Intent#FLAG_ACTIVITY_NEW_TASK
* Intent.FLAG_ACTIVITY_NEW_TASK} launch flag in the Intent.
*
     * If called multiple times with Intents that only differ in their extras,
     * both PendingIntents will refer to the second Intent. To solve this use
     * different values in the requestCode parameter for each call. See the
     * introduction for more information.
     *
* @param context The Context in which this PendingIntent should start
* the activity.
* @param requestCode Private request code for the sender (currently
     * not used, except to force the creation of a new PendingIntent).
* @param intent Intent of the activity to be launched.
* @param flags May be {@link #FLAG_ONE_SHOT}, {@link #FLAG_NO_CREATE},
* {@link #FLAG_CANCEL_CURRENT}, {@link #FLAG_UPDATE_CURRENT},
//Synthetic comment -- @@ -227,10 +232,15 @@
* existing activity, so you must use the {@link Intent#FLAG_ACTIVITY_NEW_TASK
* Intent.FLAG_ACTIVITY_NEW_TASK} launch flag in the Intent.
*
     * If called multiple times with Intents that only differ in their extras,
     * both PendingIntents will refer to the second Intent. To solve this use
     * different values in the requestCode parameter for each call. See the
     * introduction for more information.
     *
* @param context The Context in which this PendingIntent should start
* the activity.
* @param requestCode Private request code for the sender (currently
     * not used, except to force the creation of a new PendingIntent).
* @param intent Intent of the activity to be launched.
* @param flags May be {@link #FLAG_ONE_SHOT}, {@link #FLAG_NO_CREATE},
* {@link #FLAG_CANCEL_CURRENT}, {@link #FLAG_UPDATE_CURRENT},
//Synthetic comment -- @@ -316,7 +326,7 @@
* @param context The Context in which this PendingIntent should start
* the activity.
* @param requestCode Private request code for the sender (currently
     * not used, except to force the creation of a new PendingIntent).
* @param intents Array of Intents of the activities to be launched.
* @param flags May be {@link #FLAG_ONE_SHOT}, {@link #FLAG_NO_CREATE},
* {@link #FLAG_CANCEL_CURRENT}, {@link #FLAG_UPDATE_CURRENT},
//Synthetic comment -- @@ -362,7 +372,7 @@
* @param context The Context in which this PendingIntent should start
* the activity.
* @param requestCode Private request code for the sender (currently
     * not used, except to force the creation of a new PendingIntent).
* @param intents Array of Intents of the activities to be launched.
* @param flags May be {@link #FLAG_ONE_SHOT}, {@link #FLAG_NO_CREATE},
* {@link #FLAG_CANCEL_CURRENT}, {@link #FLAG_UPDATE_CURRENT},
//Synthetic comment -- @@ -423,10 +433,15 @@
* Retrieve a PendingIntent that will perform a broadcast, like calling
* {@link Context#sendBroadcast(Intent) Context.sendBroadcast()}.
*
     * If called multiple times with Intents that only differ in their extras,
     * both PendingIntents will refer to the second Intent. To solve this use
     * different values in the requestCode parameter for each call. See the
     * introduction for more information.
     *
* @param context The Context in which this PendingIntent should perform
* the broadcast.
* @param requestCode Private request code for the sender (currently
     * not used, except to force the creation of a new PendingIntent).
* @param intent The Intent to be broadcast.
* @param flags May be {@link #FLAG_ONE_SHOT}, {@link #FLAG_NO_CREATE},
* {@link #FLAG_CANCEL_CURRENT}, {@link #FLAG_UPDATE_CURRENT},
//Synthetic comment -- @@ -473,10 +488,15 @@
* {@link Context#startService Context.startService()}.  The start
* arguments given to the service will come from the extras of the Intent.
*
     * If called multiple times with Intents that only differ in their extras,
     * both PendingIntents will refer to the second Intent. To solve this use
     * different values in the requestCode parameter for each call. See the
     * introduction for more information.
     *
* @param context The Context in which this PendingIntent should start
* the service.
* @param requestCode Private request code for the sender (currently
     * not used, except to force the creation of a new PendingIntent).
* @param intent An Intent describing the service to be started.
* @param flags May be {@link #FLAG_ONE_SHOT}, {@link #FLAG_NO_CREATE},
* {@link #FLAG_CANCEL_CURRENT}, {@link #FLAG_UPDATE_CURRENT},







