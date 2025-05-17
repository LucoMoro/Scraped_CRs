//<Beginning of snippet n. 0>
private final LocalBluetoothManager mLocalManager;

private AlertDialog mDialog = null;

private List<Callback> mCallbacks = new ArrayList<Callback>();

public void showDialog(String message, DialogInterface.OnClickListener disconnectListener) {
    if (mForegroundActivity == null || mForegroundActivity.isFinishing() || mForegroundActivity.isDestroyed()) {
        return;
    }

    Context context = mForegroundActivity;
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
protected void onDestroy() {
    super.onDestroy();
    if (mDialog != null) {
        mDialog.dismiss();
        mDialog = null;
    }
}

@Override
protected void onPause() {
    super.onPause();
    if (mDialog != null && mDialog.isShowing()) {
        mDialog.dismiss();
    }
}

@Override
protected void onStop() {
    super.onStop();
    if (mDialog != null && mDialog.isShowing()) {
        mDialog.dismiss();
    }
}

public void connect() {
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Config;
import android.util.Log;

private Context mContext;
/** If a BT-related activity is in the foreground, this will be it. */
private Activity mForegroundActivity;
private AlertDialog mErrorDialog = null;

private BluetoothAdapter mAdapter;

public void showErrorDialog() {
    if (mForegroundActivity == null || mForegroundActivity.isFinishing() || mForegroundActivity.isDestroyed()) {
        return;
    }

    if (mErrorDialog != null && mErrorDialog.isShowing()) {
        mErrorDialog.dismiss();
    }
    mErrorDialog = new AlertDialog.Builder(mForegroundActivity)
            .setTitle("Error")
            .setMessage("An error occurred")
            .setPositiveButton(android.R.string.ok, null)
            .create();
    mErrorDialog.show();
}

@Override
protected void onPause() {
    super.onPause();
    if (mErrorDialog != null && mErrorDialog.isShowing()) {
        mErrorDialog.dismiss();
    }
}

@Override
protected void onStop() {
    super.onStop();
    if (mErrorDialog != null && mErrorDialog.isShowing()) {
        mErrorDialog.dismiss();
    }
}

public void someMethod(Activity activity) {
    mForegroundActivity = activity;
    SharedPreferences.Editor editor = mContext.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit();
    editor.remove(SHARED_PREFERENCES_KEY_DOCK_AUTO_CONNECT + addr);
    editor.apply();
}

//<End of snippet n. 1>