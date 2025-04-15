/*Adds documentation for ContentResolver notifyChange()

Adds clarifying documentation to ContentResolver#notifyChange() to
explain interaction with the sync framework.

Change-Id:Ia1a1ed173e230bc11aa778268749323536ca434fSigned-off-by: Steve Pomeroy <steve@staticfree.info>*/
//Synthetic comment -- diff --git a/core/java/android/content/ContentResolver.java b/core/java/android/content/ContentResolver.java
//Synthetic comment -- index cc3219b..780ec11 100644

//Synthetic comment -- @@ -1030,7 +1030,8 @@
}

/**
     * Notify registered observers that a row was updated.
* To register, call {@link #registerContentObserver(android.net.Uri , boolean, android.database.ContentObserver) registerContentObserver()}.
* By default, CursorAdapter objects will get this notification.
*
//Synthetic comment -- @@ -1045,10 +1046,14 @@
* Notify registered observers that a row was updated.
* To register, call {@link #registerContentObserver(android.net.Uri , boolean, android.database.ContentObserver) registerContentObserver()}.
* By default, CursorAdapter objects will get this notification.
*
* @param uri
* @param observer The observer that originated the change, may be <code>null</null>
* @param syncToNetwork If true, attempt to sync the change to the network.
*/
public void notifyChange(Uri uri, ContentObserver observer, boolean syncToNetwork) {
try {







