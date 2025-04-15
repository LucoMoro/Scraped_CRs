/*Added the lock internet setting feature for APN.

The APN will be locked following the customization file specified.
So that the locked APN can not be edit or delete by end user.

Change-Id:Ibbfce8dc31e3591e02db96b7224d5e2b07b1eb5a*/
//Synthetic comment -- diff --git a/src/com/android/settings/ApnEditor.java b/src/com/android/settings/ApnEditor.java
//Synthetic comment -- index 72dba1a..e2f941b 100644

//Synthetic comment -- @@ -54,6 +54,8 @@
private static final int MENU_CANCEL = Menu.FIRST + 2;
private static final int ERROR_DIALOG_ID = 0;

private static String sNotSet;
private EditTextPreference mName;
private EditTextPreference mApn;
//Synthetic comment -- @@ -73,6 +75,8 @@
private String mCurMnc;
private String mCurMcc;

private Uri mUri;
private Cursor mCursor;
private boolean mNewApn;
//Synthetic comment -- @@ -99,6 +103,7 @@
Telephony.Carriers.MMSPORT, // 13
Telephony.Carriers.AUTH_TYPE, // 14
Telephony.Carriers.TYPE, // 15
};

private static final int ID_INDEX = 0;
//Synthetic comment -- @@ -116,7 +121,7 @@
private static final int MMSPORT_INDEX = 13;
private static final int AUTH_TYPE_INDEX = 14;
private static final int TYPE_INDEX = 15;


@Override
protected void onCreate(Bundle icicle) {
//Synthetic comment -- @@ -235,7 +240,7 @@
if (authVal != -1) {
mAuthType.setValueIndex(authVal);
}

}

mName.setSummary(checkNull(mName.getText()));
//Synthetic comment -- @@ -262,6 +267,23 @@
} else {
mAuthType.setSummary(sNotSet);
}
}

public boolean onPreferenceChange(Preference preference, Object newValue) {
//Synthetic comment -- @@ -283,16 +305,21 @@
@Override
public boolean onCreateOptionsMenu(Menu menu) {
super.onCreateOptionsMenu(menu);
        // If it's a new APN, then cancel will delete the new entry in onPause
        if (!mNewApn) {
            menu.add(0, MENU_DELETE, 0, R.string.menu_delete)
                .setIcon(android.R.drawable.ic_menu_delete);
}
        menu.add(0, MENU_SAVE, 0, R.string.menu_save)
            .setIcon(android.R.drawable.ic_menu_save);
        menu.add(0, MENU_CANCEL, 0, R.string.menu_cancel)
            .setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
}

@Override







