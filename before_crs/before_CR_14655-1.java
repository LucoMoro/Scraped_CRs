/*ContactProvider: Set correct locale for the contact database when setting changed

When user change the language settings the ContactProvider was not aware of this.
Using the onConfigurationChanged method the ContactProvider is aware of this change
and sets the locale in the database. This will fix the problem that sorting is
wrong in the contactlist after a setting change.

Change-Id:Ic8600dcba9f96f3eaaf80450c579f1f7f88bcafa*/
//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsProvider.java b/src/com/android/providers/contacts/ContactsProvider.java
//Synthetic comment -- index 6a3e6dd..5653b02 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.CursorJoiner;
//Synthetic comment -- @@ -78,6 +79,7 @@
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//Synthetic comment -- @@ -1963,6 +1965,16 @@
}
}

private void ensureSyncAccountIsSet(ContentValues values) {
synchronized (mAccountsLock) {
final String accountName = values.getAsString(SyncConstValue._SYNC_ACCOUNT);







