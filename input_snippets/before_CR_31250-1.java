
//<Beginning of snippet n. 0>


}

/**
     * Notify registered observers that a row was updated.
* To register, call {@link #registerContentObserver(android.net.Uri , boolean, android.database.ContentObserver) registerContentObserver()}.
* By default, CursorAdapter objects will get this notification.
*
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

//<End of snippet n. 0>








