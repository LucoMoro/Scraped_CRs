/*ContentProvider: Avoid duplicate initialization of AsyncTask

AsyncTask is being initialized by ActivityThread to ensure its static
handler initilization and ContentProvider need not duplicate the logic.

Change-Id:I48bc88bdba32b56114f09c04a75480e26508f704*/
//Synthetic comment -- diff --git a/core/java/android/content/ContentProvider.java b/core/java/android/content/ContentProvider.java
//Synthetic comment -- index b22179e..4c905a3 100644

//Synthetic comment -- @@ -1037,12 +1037,6 @@
*/
public void attachInfo(Context context, ProviderInfo info) {
/*
         * We may be using AsyncTask from binder threads.  Make it init here
         * so its static handler is on the main thread.
         */
        AsyncTask.init();

        /*
* Only allow it to be set once, so after the content service gives
* this to us clients can't change it.
*/







