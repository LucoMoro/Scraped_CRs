/*Fixing Issue 4928.

This fix adds support for clearing frequently called numbers.

Change-Id:I1c97e2adf49a6134f5087c95f545fc62b0cea90bSigned-off-by: David Marques <dpsmarques@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsProvider2.java b/src/com/android/providers/contacts/ContactsProvider2.java
//Synthetic comment -- index 57e1e5d..cc33f19 100644

//Synthetic comment -- @@ -2725,6 +2725,10 @@
return deleteStatusUpdates(selection, selectionArgs);
}

default: {
mSyncToNetwork = true;
return mLegacyApiSupport.delete(uri, selection, selectionArgs);
//Synthetic comment -- @@ -2732,6 +2736,12 @@
}
}

private static boolean readBooleanQueryParameter(Uri uri, String name, boolean defaultValue) {
final String flag = uri.getQueryParameter(name);
return flag == null







