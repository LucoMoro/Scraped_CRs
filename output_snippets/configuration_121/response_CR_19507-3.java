//<Beginning of snippet n. 0>
private final LocalBluetoothManager mLocalManager;
private AlertDialog mDialog = null;
private List<Callback> mCallbacks = new ArrayList<Callback>();

public void showDialog(Context context, String message, DialogInterface.OnClickListener disconnectListener) {
    if (context instanceof Activity) {
        mForegroundActivity = (Activity) context;
    }
    
    if (mDialog == null) {
        mDialog = new AlertDialog.Builder(mForegroundActivity)
                .setPositiveButton(android.R.string.ok, disconnectListener)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    } else {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
    
    if (mForegroundActivity != null) {
        mDialog.setTitle(getName());
        mDialog.setMessage(message);
        mDialog.show();
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
protected void onDestroy() {
    if (mDialog != null) {
        mDialog.dismiss();
        mDialog = null;
    }
    super.onDestroy();
}

public void connect() {
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

private Context mContext;
/** If a BT-related activity is in the foreground, this will be it. */
private Activity mForegroundActivity;
private AlertDialog mErrorDialog = null;
private BluetoothAdapter mAdapter;

public void showErrorDialog(String message) {
    if (mForegroundActivity == null) return;

    if (mErrorDialog == null) {
        mErrorDialog = new AlertDialog.Builder(mForegroundActivity)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    } else {
        if (mErrorDialog.isShowing()) {
            mErrorDialog.dismiss();
        }
    }

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
protected void onDestroy() {
    if (mErrorDialog != null) {
        mErrorDialog.dismiss();
        mErrorDialog = null;
    }
    super.onDestroy();
}

public void disconnect(String addr) {
    SharedPreferences.Editor editor = mContext.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit();
    editor.remove(SHARED_PREFERENCES_KEY_DOCK_AUTO_CONNECT + addr);
    editor.apply();
}

//<End of snippet n. 1>