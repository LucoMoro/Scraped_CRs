/*Make CONTACTS_AS_MULTI_VCARD locale safe

SimpleDateFormat was used with the default locale to build
SQL statements; this is unsafe for some locales. Switched
to Locale.US.

Change-Id:I7307b12e1f3d796e1bb4a73d97918d0162ca971e*/
//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactsProvider2.java b/src/com/android/providers/contacts/ContactsProvider2.java
//Synthetic comment -- index c321489..45877c5 100644

//Synthetic comment -- @@ -5025,7 +5025,7 @@
}

case CONTACTS_AS_MULTI_VCARD: {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
String currentDateString = dateFormat.format(new Date()).toString();
return mActiveDb.get().rawQuery(
"SELECT" +







