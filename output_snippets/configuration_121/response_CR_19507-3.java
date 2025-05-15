<<Beginning of snippet n. 0>>
private final LocalBluetoothManager mLocalManager;

private WeakReference<Context> mContextRef;
private AlertDialog mDialog = null;

private List<Callback> mCallbacks = new ArrayList<Callback>();

public void showDialog(String message, DialogInterface.OnClickListener disconnectListener) {
    Context context = mContextRef != null ? mContextRef.get() : null;
    if (context == null) return;

    if (mDialog == null) {
        mDialog = new AlertDialog.Builder(context)
                .setPositiveButton(android.R.string.ok, disconnectListener)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        mDialog.setOnDismissListener(dialog -> mDialog = null);
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
    if (mDialog != null && mDialog.isShowing()) {
        mDialog.dismiss();
        mDialog = null;
    }
}

public void connect() {
    // Implementation here
}
//<End of snippet n. 0>>

<<Beginning of snippet n. 1>>
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;

private WeakReference<Context> mContextRef;
private Activity mForegroundActivity;
private AlertDialog mErrorDialog = null;
private BluetoothAdapter mAdapter;

public void showErrorDialog(String message) {
    Context context = mContextRef != null ? mContextRef.get() : null;
    if (context == null) return;

    if (mErrorDialog == null) {
        mErrorDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create();
        mErrorDialog.setOnDismissListener(dialog -> mErrorDialog = null);
    } else {
        if (mErrorDialog.isShowing()) {
            mErrorDialog.dismiss();
        }
    }
    mErrorDialog.show();
}

@Override
protected void onDestroy() {
    super.onDestroy();
    if (mErrorDialog != null && mErrorDialog.isShowing()) {
        mErrorDialog.dismiss();
        mErrorDialog = null;
    }
}

public void connectToDevice(BluetoothDevice device) {
    // Implementation here
}
//<End of snippet n. 1>>