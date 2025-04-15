/*Fix issue 1164: Incorrect organization type label in contactshttp://code.google.com/p/android/issues/detail?id=1164This patch is working withhttp://review.source.android.com/1845They are in 2 different projects, so I need to commit twice.*/




//Synthetic comment -- diff --git a/src/com/android/contacts/EditContactActivity.java b/src/com/android/contacts/EditContactActivity.java
//Synthetic comment -- index c301473..1446eec 100644

//Synthetic comment -- @@ -713,7 +713,7 @@
case OTHER_ORGANIZATION:
entry = EditEntry.newOrganizationEntry(EditContactActivity.this,
Uri.withAppendedPath(mUri, Organizations.CONTENT_DIRECTORY),
                                Organizations.TYPE_WORK);
mOtherEntries.add(entry);
break;








