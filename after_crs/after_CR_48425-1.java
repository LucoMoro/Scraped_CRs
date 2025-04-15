/*Phone: Appear force close due to com.android.phone after import 250 contacts

When dialog is showing, the acitivity when resuming behaves as if
it has exited and creates the progress dialog again.

Change-Id:I0ae77e4246f8e9b1a40207ec3eac1178acf85543Author: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Javen Ni <javen.ni@borqs.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 11406*/




//Synthetic comment -- diff --git a/src/com/android/phone/SimContacts.java b/src/com/android/phone/SimContacts.java
//Synthetic comment -- index ebfc775..684aeff 100644

//Synthetic comment -- @@ -70,6 +70,7 @@
private ProgressDialog mProgressDialog;

private Account mAccount;
    private boolean mActivityExiting = false;

private static class NamePhoneTypePair {
final String name;
//Synthetic comment -- @@ -117,6 +118,7 @@
}

mProgressDialog.dismiss();
            mActivityExiting = true;
finish();
}

//Synthetic comment -- @@ -238,6 +240,18 @@
}

@Override
    protected void onResume() {
        super.onResume();
        mActivityExiting = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivityExiting = true;
    }

    @Override
protected CursorAdapter newAdapter() {
return new SimpleCursorAdapter(this, R.layout.sim_import_list_entry, mCursor,
new String[] { "name" }, new int[] { android.R.id.text1 });
//Synthetic comment -- @@ -286,21 +300,22 @@

ImportAllSimContactsThread thread = new ImportAllSimContactsThread();

                if (!mActivityExiting) {
                    // TODO: need to show some error dialog.
                    if (mCursor == null) {
                        Log.e(LOG_TAG, "cursor is null. Ignore silently.");
                        break;
                    }
                    mProgressDialog = new ProgressDialog(this);
                    mProgressDialog.setTitle(title);
                    mProgressDialog.setMessage(message);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                            getString(R.string.cancel), thread);
                    mProgressDialog.setProgress(0);
                    mProgressDialog.setMax(mCursor.getCount());
                    mProgressDialog.show();
}
thread.start();

return true;
//Synthetic comment -- @@ -357,6 +372,7 @@
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
startActivity(intent);
                    mActivityExiting = true;
finish();
return true;
}







