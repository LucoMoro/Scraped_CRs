//<Beginning of snippet n. 0>
private final LocalBluetoothManager mLocalManager;
private AlertDialog mDialog = null;
private List<Callback> mCallbacks = new ArrayList<Callback>();
private Activity mForegroundActivity;

public void showDialog(Context context, String message, DialogInterface.OnClickListener disconnectListener) {
    if (mForegroundActivity != null && !mForegroundActivity.isFinishing() && !mForegroundActivity.isDestroyed() && mForegroundActivity.isResumed()) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = new AlertDialog.Builder(mForegroundActivity)
                .setPositiveButton(android.R.string.ok, disconnectListener)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        mDialog.setTitle(getName());
        mDialog.setMessage(message);
        mDialog.show();
    }
}

@Override
protected void onDestroy() {
    if (mDialog != null) {
        mDialog.dismiss();
        mDialog = null;
    }
    super.onDestroy();
}

@Override
protected void onPause() {
    if (mDialog != null && mDialog.isShowing()) {
        mDialog.dismiss();
    }
    mDialog = null;
    clearCallbacks();
}

@Override
protected void onStop() {
    if (mDialog != null && mDialog.isShowing()) {
        mDialog.dismiss();
    }
    mDialog = null;
    clearCallbacks();
}

private void clearCallbacks() {
    mCallbacks.clear();
}

@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mDialog != null) {
        outState.putString("dialog_message", mDialog.getMessage().toString());
        // Save other dialog state if needed
    }
}

@Override
protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    if (savedInstanceState.containsKey("dialog_message")) {
        String message = savedInstanceState.getString("dialog_message");
        showDialog(mForegroundActivity, message, null); // Use appropriate listener if needed
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

private AlertDialog mErrorDialog = null;
private BluetoothAdapter mAdapter;

public void showErrorDialog() {
    if (mForegroundActivity != null && !mForegroundActivity.isFinishing() && !mForegroundActivity.isDestroyed() && mForegroundActivity.isResumed()) {
        if (mErrorDialog != null && mErrorDialog.isShowing()) {
            mErrorDialog.dismiss();
        }
        mErrorDialog = new AlertDialog.Builder(mForegroundActivity).create();
        mErrorDialog.show();
    }
}

@Override
protected void onDestroy() {
    if (mErrorDialog != null) {
        mErrorDialog.dismiss();
        mErrorDialog = null;
    }
    super.onDestroy();
}

@Override
protected void onPause() {
    if (mErrorDialog != null && mErrorDialog.isShowing()) {
        mErrorDialog.dismiss();
    }
    mErrorDialog = null;
}

@Override
protected void onStop() {
    if (mErrorDialog != null && mErrorDialog.isShowing()) {
        mErrorDialog.dismiss();
    }
    mErrorDialog = null;
}

} 
mForegroundActivity = activity;

editor.remove(SHARED_PREFERENCES_KEY_DOCK_AUTO_CONNECT + addr);
editor.apply();
//<End of snippet n. 1>