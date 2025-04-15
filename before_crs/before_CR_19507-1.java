/*Make sure dialog is not left hanging for invalid windows

Problem: LocalBluetoothManager is a singleton, which manages a set
of CachedBluetoothDevice, which can popup a dialog when disconnecting.
However, since this dialog is popped up in the context of a specific
BluetoothSettings instance - and we can get multiple instances f.ex.
when using "Home" to exit out of Settings and then restarting Settings
repeatedly - we must make sure that it's handled correctly both in
respect to which was its "parent activity" when it was created AND
in respect to switching orientation (we don't want to leak the dialog
window when rotating).

Change-Id:Iab1d5996462f2b91c25e78c7b3bdb850ac23c42d*/
//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/CachedBluetoothDevice.java b/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
//Synthetic comment -- index 253bf02..c2d3268 100644

//Synthetic comment -- @@ -71,8 +71,6 @@

private final LocalBluetoothManager mLocalManager;

    private AlertDialog mDialog = null;

private List<Callback> mCallbacks = new ArrayList<Callback>();

/**
//Synthetic comment -- @@ -377,29 +375,16 @@
}
};

        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(context)
.setPositiveButton(android.R.string.ok, disconnectListener)
.setNegativeButton(android.R.string.cancel, null)
.create();
        } else {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
        mDialog.setTitle(getName());
        mDialog.setMessage(message);
        mDialog.show();
    }

    @Override
    protected void finalize() throws Throwable {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

        super.finalize();
}

public void connect() {








//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/LocalBluetoothManager.java b/src/com/android/settings/bluetooth/LocalBluetoothManager.java
//Synthetic comment -- index 2ffb139..3f2dcb8 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
/** If a BT-related activity is in the foreground, this will be it. */
private Activity mForegroundActivity;
private AlertDialog mErrorDialog = null;

private BluetoothAdapter mAdapter;

//Synthetic comment -- @@ -140,6 +141,12 @@
mErrorDialog.dismiss();
mErrorDialog = null;
}
mForegroundActivity = activity;
}

//Synthetic comment -- @@ -379,4 +386,16 @@
editor.remove(SHARED_PREFERENCES_KEY_DOCK_AUTO_CONNECT + addr);
editor.commit();
}
}







