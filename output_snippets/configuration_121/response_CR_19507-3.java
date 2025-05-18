//<Beginning of snippet n. 0>
private final LocalBluetoothManager mLocalManager;
private AlertDialog mDialog = null;
private List<Callback> mCallbacks = new ArrayList<Callback>();

public void showDialog(Activity activity, String message, DialogInterface.OnClickListener disconnectListener) {
    if (activity == null || activity.isFinishing() || activity.isDestroyed() || !activity.isChangingConfigurations()) {
        return;
    }

    if (mDialog != null && mDialog.isShowing()) {
        mDialog.dismiss();
        mDialog = null;
    }

    mDialog = new AlertDialog.Builder(activity)
            .setPositiveButton(android.R.string.ok, disconnectListener)
            .setNegativeButton(android.R.string.cancel, null)
            .create();
    
    mDialog.setTitle(getName());
    mDialog.setMessage(message);
    mDialog.show();
}

@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    // Logic to save dialog state if necessary
}

@Override
protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    // Logic to restore dialog state if necessary
}

@Override
protected void onStop() {
    super.onStop();
    if (mDialog != null) {
        mDialog.dismiss();
        mDialog = null;
    }
}

@Override
protected void onPause() {
    super.onPause();
    if (mDialog != null) {
        mDialog.dismiss();
        mDialog = null;
    }
}

public void connect() {
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;

private Activity mForegroundActivity;
private AlertDialog mErrorDialog = null;
private BluetoothAdapter mAdapter;

public void showErrorDialog(Activity activity) {
    if (activity == null || activity.isFinishing() || activity.isDestroyed() || !activity.isChangingConfigurations()) {
        return;
    }

    if (mErrorDialog != null && mErrorDialog.isShowing()) {
        mErrorDialog.dismiss();
        mErrorDialog = null;
    }

    mErrorDialog = new AlertDialog.Builder(activity)
            .setTitle("Error")
            .setMessage("An error occurred.")
            .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
            .create();
    mErrorDialog.show();
}

@Override
protected void onStop() {
    super.onStop();
    if (mErrorDialog != null) {
        mErrorDialog.dismiss();
        mErrorDialog = null;
    }
}

@Override
protected void onPause() {
    super.onPause();
    if (mErrorDialog != null) {
        mErrorDialog.dismiss();
        mErrorDialog = null;
    }
}

public void connectDevice(BluetoothDevice device) {
    // Assume activity is properly initialized before this call
    mForegroundActivity = activity; 
    SharedPreferences.Editor editor = mContext.getSharedPreferences("preferences", Context.MODE_PRIVATE).edit();
    editor.remove(SHARED_PREFERENCES_KEY_DOCK_AUTO_CONNECT + device.getAddress());
    editor.apply();
}
//<End of snippet n. 1>