/*Contacts: Import SIM Contacts support for Multi SIM functionality.

Change-Id:I0f60cb784038847ce613e16aec8878515482e747*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index ac6a3a8..18f84e1 100644

//Synthetic comment -- @@ -1434,7 +1434,14 @@
}
};

        if (TelephonyManager.getDefault().hasIccCard()) {
adapter.add(R.string.import_from_sim);
}
if (res.getBoolean(R.bool.config_allow_import_from_sdcard)) {







