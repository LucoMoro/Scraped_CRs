//<Beginning of snippet n. 0>
mOppManager.startTransfer(remoteDevice);

// Retrieve BluetoothDevice object
BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(remoteDevice);
String deviceName;

try {
    deviceName = device.getUuids() != null && device.getUuids().length > 0 ? device.getUuids()[0].toString() : "unknown name";
} catch (Exception e) {
    deviceName = "Error retrieving device name";
    Log.e("BluetoothError", "Error retrieving device name", e);
}

String toastMsg;
int batchSize = mOppManager.getBatchSize();
if (mOppManager.mMultipleFlag) {
    toastMsg = "Transferring to " + deviceName + " with batch size " + batchSize;
} else {
    toastMsg = "Transferring to " + deviceName + ", batch size " + batchSize;
}

if (!ToastUtils.isToastShowing()) {
    ToastUtils.showToast(context, toastMsg);
    ToastUtils.setToastShowing(true);
}

// ToastUtils class to manage toast display
public class ToastUtils {
    private static boolean isShowing = false;

    public static boolean isToastShowing() {
        return isShowing;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        isShowing = true;

        new Handler(Looper.getMainLooper()).postDelayed(() -> isShowing = false, Toast.LENGTH_SHORT);
    }

    public static void setToastShowing(boolean showing) {
        isShowing = showing;
    }
}
//<End of snippet n. 0>