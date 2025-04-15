/*Fixing Issue 4928.

This fix adds support for clearing frequently called numbers.

Signed-off-by: David Marques <dpsmarques@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsProvider2.java b/src/com/android/providers/contacts/ContactsProvider2.java
//Synthetic comment -- index 7b3c7f0..24fb07d 100644

//Synthetic comment -- @@ -16,30 +16,20 @@

package com.android.providers.contacts;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import android.accounts.Account;
import android.accounts.AccountManager;
//Synthetic comment -- @@ -104,20 +94,30 @@
import android.text.util.Rfc822Tokenizer;
import android.util.Log;

import com.android.internal.content.SyncStateContentProviderHelper;
import com.android.providers.contacts.ContactLookupKey.LookupKeySegment;
import com.android.providers.contacts.ContactsDatabaseHelper.AggregatedPresenceColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.AggregationExceptionColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.Clauses;
import com.android.providers.contacts.ContactsDatabaseHelper.ContactsColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.ContactsStatusUpdatesColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.DataColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.DisplayNameSources;
import com.android.providers.contacts.ContactsDatabaseHelper.GroupsColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.MimetypesColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.NameLookupColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.NameLookupType;
import com.android.providers.contacts.ContactsDatabaseHelper.NicknameLookupColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.PhoneColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.PhoneLookupColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.PresenceColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.RawContactsColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.SettingsColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.StatusUpdatesColumns;
import com.android.providers.contacts.ContactsDatabaseHelper.Tables;
import com.google.android.collect.Lists;
import com.google.android.collect.Maps;
import com.google.android.collect.Sets;

/**
* Contacts content provider. The contract between this provider and applications
//Synthetic comment -- @@ -2725,6 +2725,10 @@
return deleteStatusUpdates(selection, selectionArgs);
}

            case CONTACTS_STREQUENT: {
                return clearFrequentlyCalled();
            }

default: {
mSyncToNetwork = true;
return mLegacyApiSupport.delete(uri, selection, selectionArgs);
//Synthetic comment -- @@ -2732,6 +2736,12 @@
}
}

    private int clearFrequentlyCalled() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Contacts.TIMES_CONTACTED, 0);
        return mDb.update(Tables.CONTACTS, contentValues, null, null);
    }

private static boolean readBooleanQueryParameter(Uri uri, String name, boolean defaultValue) {
final String flag = uri.getQueryParameter(name);
return flag == null







