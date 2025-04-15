/*Fix preserving new APN with an unset "name" and "APN".

If an new APN have invalid values when the device goes
to sleep while editing, then the APN can't be saved when
the activity is resumed even though the values is corrected.
The behavior is identical when an other activity covers the
APN editor and onSaveStateInstance is called before "name"
and "APN" is set. Fixed so that the APN setting is
preserved despite of missing parameters "APN" and "name".

Change-Id:I5459efea65fbbdcde16995f35c9f0386ec8caae6*/
//Synthetic comment -- diff --git a/src/com/android/settings/ApnEditor.java b/src/com/android/settings/ApnEditor.java
//Synthetic comment -- index 3f0c02f..f5b4ed1 100644

//Synthetic comment -- @@ -334,9 +334,8 @@
@Override
protected void onSaveInstanceState(Bundle icicle) {
super.onSaveInstanceState(icicle);
        if (validateAndSave(true)) {
            icicle.putInt(SAVED_POS, mCursor.getInt(ID_INDEX));
        }
}

/**
//Synthetic comment -- @@ -362,12 +361,6 @@
return false;
}

        // If it's a new APN and a name or apn haven't been entered, then erase the entry
        if (force && mNewApn && name.length() < 1 && apn.length() < 1) {
            getContentResolver().delete(mUri, null, null);
            return false;
        }

ContentValues values = new ContentValues();

// Add a dummy name "Untitled", if the user exits the screen without adding a name but 







