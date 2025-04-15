/*calendar: Fix NPE from uninitialized HandlerThread

Commit b9cd34a9b6622045cfac73d1497f4e5d745601d3 in AOSP master removed
the call to ContactsAsyncHelper's private constructor, which initialized
the static HandlerThread. This causes an NPE when loading contact photos
in appointments.

Readded the static initializer to restore this functionality.

Change-Id:Idc726d3fc500a30d0c9df795e5fa44d8436a8da2*/




//Synthetic comment -- diff --git a/src/com/android/calendar/ContactsAsyncHelper.java b/src/com/android/calendar/ContactsAsyncHelper.java
//Synthetic comment -- index 157cc34..46e9ecc 100644

//Synthetic comment -- @@ -58,6 +58,9 @@
// static objects
private static Handler sThreadHandler;

    @SuppressWarnings("unused")
    private static ContactsAsyncHelper sInstance = new ContactsAsyncHelper();

private static final class WorkerArgs {
public Context context;
public ImageView view;







