/*calendar: Fix NPE from uninitialized HandlerThread

Commit b9cd34a9b6622045cfac73d1497f4e5d745601d3 in AOSP master removed
the call to ContactsAsyncHelper's private constructor, which initialized
the static HandlerThread. This causes an NPE when loading contact photos
in appointments.

Readded the static initializer to restore this functionality.

Change-Id:Idc726d3fc500a30d0c9df795e5fa44d8436a8da2*/
//Synthetic comment -- diff --git a/src/com/android/calendar/ContactsAsyncHelper.java b/src/com/android/calendar/ContactsAsyncHelper.java
//Synthetic comment -- index 157cc34..fd9d700 100644

//Synthetic comment -- @@ -38,6 +38,8 @@
private static final boolean DBG = false;
private static final String LOG_TAG = "ContactsAsyncHelper";

/**
* Interface for a WorkerHandler result return.
*/
//Synthetic comment -- @@ -149,6 +151,9 @@
args.uri = person;
args.defaultResource = placeholderImageResource;

// setup message arguments
Message msg = sThreadHandler.obtainMessage(DEFAULT_TOKEN);
msg.arg1 = EVENT_LOAD_IMAGE;







