
//<Beginning of snippet n. 0>


}

/**
     * Notify registered observers that a row was updated and attempt to sync changes
     * to the network.
* To register, call {@link #registerContentObserver(android.net.Uri , boolean, android.database.ContentObserver) registerContentObserver()}.
* By default, CursorAdapter objects will get this notification.
*
* Notify registered observers that a row was updated.
* To register, call {@link #registerContentObserver(android.net.Uri , boolean, android.database.ContentObserver) registerContentObserver()}.
* By default, CursorAdapter objects will get this notification.
     * If syncToNetwork is true, this will attempt to schedule a local sync using the sync
     * adapter that's registered for the authority of the provided uri. No account will be
     * passed to the sync adapter, so all matching accounts will be synchronized.
*
* @param uri
* @param observer The observer that originated the change, may be <code>null</null>
* @param syncToNetwork If true, attempt to sync the change to the network.
     * @see #requestSync(android.accounts.Account, String, android.os.Bundle)
*/
public void notifyChange(Uri uri, ContentObserver observer, boolean syncToNetwork) {
try {

//<End of snippet n. 0>








