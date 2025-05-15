//<Beginning of snippet n. 0>
private final LocalBluetoothManager mLocalManager;
private AlertDialog mDialog = null;
private List<Callback> mCallbacks = new ArrayList<Callback>();

public void showDialog(Context context, String message, DialogInterface.OnClickListener disconnectListener) {
    if (!(context instanceof Activity) || ((Activity) context).isFinishing()) {
        return; // Validate activity context before proceeding
    }

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
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mDialog != null && mDialog.isShowing()) {
        mDialog.dismiss();
        mDialog = null;
    }
}

@Override
protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    // Restore dialog state if needed
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

// Removed finalize usage for cleanup.
public void connect() {
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

private Context mContext;
/** If a BT-related activity is in the foreground, this will be it. */
private Activity mForegroundActivity;
private AlertDialog mErrorDialog = null;
private BluetoothAdapter mAdapter;

public void handleErrorDialog(Context context) {
    if (mErrorDialog != null && mErrorDialog.isShowing()) {
        mErrorDialog.dismiss();
        mErrorDialog = null;
    }

    if (!(context instanceof Activity) || ((Activity) context).isFinishing()) {
        return; // Validate activity context before proceeding
    }

    mForegroundActivity = (Activity) context;
    mErrorDialog = new AlertDialog.Builder(mForegroundActivity)
            .setTitle("Error")
            .setMessage("An error occurred.")
            .setPositiveButton(android.R.string.ok, null)
            .create();
    mErrorDialog.show();
}

public void cleanupPreferences(String addr) {
    SharedPreferences.Editor editor = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).edit();
    editor.remove(SHARED_PREFERENCES_KEY_DOCK_AUTO_CONNECT + addr);
    editor.apply();
}
//<End of snippet n. 1>