
//<Beginning of snippet n. 0>



private final LocalBluetoothManager mLocalManager;

    private final DialogInterface.OnDismissListener mDismissListener =
            new DialogInterface.OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            mLocalManager.unregisterDialog(dialog);
        }
    };

private List<Callback> mCallbacks = new ArrayList<Callback>();

}
};

        AlertDialog dialog = new AlertDialog.Builder(context)
            .setPositiveButton(android.R.string.ok, disconnectListener)
            .setNegativeButton(android.R.string.cancel, null)
            .create();

        dialog.setOnDismissListener(mDismissListener);
        mLocalManager.registerDialog(dialog);

        dialog.setTitle(getName());
        dialog.setMessage(message);
        dialog.show();
}

public void connect() {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Config;
import android.util.Log;
private Context mContext;
/** If a BT-related activity is in the foreground, this will be it. */
private Activity mForegroundActivity;
    private DialogInterface mErrorDialog = null;
    private DialogInterface mDisconnectDialog = null;

private BluetoothAdapter mAdapter;

mErrorDialog.dismiss();
mErrorDialog = null;
}
        if (mDisconnectDialog != null) {
            // dismiss listener will set mDisconnectDialog to null
            mDisconnectDialog.dismiss();
        }
mForegroundActivity = activity;
}

editor.remove(SHARED_PREFERENCES_KEY_DOCK_AUTO_CONNECT + addr);
editor.apply();
}

    public void registerDialog(DialogInterface dialog) {
        mDisconnectDialog = dialog;
    }

    public void unregisterDialog(DialogInterface dialog) {
        if (mDisconnectDialog == dialog) {
            mDisconnectDialog = null;
        }
    }
}

//<End of snippet n. 1>








