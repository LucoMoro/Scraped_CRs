/*replaced deprecated setButton methods

Change-Id:Ic112dc6acdb62586db741e996b0344d38c71d12f*/




//Synthetic comment -- diff --git a/src/com/android/settings/wifi/AccessPointDialog.java b/src/com/android/settings/wifi/AccessPointDialog.java
//Synthetic comment -- index 4804d78..2c8613e 100644

//Synthetic comment -- @@ -21,8 +21,6 @@
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.security.Credentials;
import android.security.KeyStore;
import android.net.wifi.WifiInfo;
//Synthetic comment -- @@ -31,8 +29,6 @@
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
//Synthetic comment -- @@ -41,7 +37,6 @@
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AccessPointDialog extends AlertDialog implements DialogInterface.OnClickListener,
//Synthetic comment -- @@ -57,9 +52,8 @@
private static final String INSTANCE_KEY_AUTO_SECURITY_ALLOWED =
"com.android.settings.wifi.AccessPointDialog:autoSecurityAllowed";

    private static final int POSITIVE_BUTTON = BUTTON_POSITIVE;
    private static final int NEUTRAL_BUTTON = BUTTON_NEUTRAL;

/** The dialog should show info connectivity functionality */
public static final int MODE_INFO = 0;
//Synthetic comment -- @@ -476,15 +470,15 @@
final Context context = getContext();

if (positiveResId > 0) {
            setButton(BUTTON_POSITIVE, context.getString(positiveResId), this);
}

if (negativeResId > 0) {
            setButton(BUTTON_NEGATIVE, context.getString(negativeResId), this);
}

if (neutralResId > 0) {
            setButton(BUTTON_NEUTRAL, context.getString(neutralResId), this);
}
}

//Synthetic comment -- @@ -717,13 +711,13 @@
}
}

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
if (parent == mSecuritySpinner) {
handleSecurityChange(getSecurityTypeFromSpinner());
}
}

    public void onNothingSelected(AdapterView<?> parent) {
}

private void handleSecurityChange(int security) {







