/*Make the Phone options dialog use current language

The title of the Phone options dialog is displayed using wrong
translation if the user changes the current language setting. Moving
the setTitle call to prepareDialog to ensure that the title gets
updated before the dialog is shown.

Change-Id:I03ff59c7f4ff711a06b05de7cca94fa928cf67ef*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/GlobalActions.java b/policy/src/com/android/internal/policy/impl/GlobalActions.java
//Synthetic comment -- index 1f06dcc..5e33f05 100644

//Synthetic comment -- @@ -221,8 +221,7 @@
final AlertDialog.Builder ab = new AlertDialog.Builder(mContext);

ab.setAdapter(mAdapter, this)
                .setInverseBackgroundForced(true);

final AlertDialog dialog = ab.create();
dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
//Synthetic comment -- @@ -249,6 +248,7 @@
} else {
mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
}
        mDialog.setTitle(R.string.global_actions);
}









