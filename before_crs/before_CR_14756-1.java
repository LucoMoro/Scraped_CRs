/*Removed Calls to deprecated API

Change-Id:Ic8c6116e1224681bbb7971830aa868b4d5d0893a*/
//Synthetic comment -- diff --git a/src/com/android/settings/wifi/AccessPointDialog.java b/src/com/android/settings/wifi/AccessPointDialog.java
//Synthetic comment -- index 4804d78..c71ca07 100644

//Synthetic comment -- @@ -21,8 +21,6 @@
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.security.Credentials;
import android.security.KeyStore;
import android.net.wifi.WifiInfo;
//Synthetic comment -- @@ -31,8 +29,6 @@
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
//Synthetic comment -- @@ -41,7 +37,6 @@
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

public class AccessPointDialog extends AlertDialog implements DialogInterface.OnClickListener,
//Synthetic comment -- @@ -57,9 +52,9 @@
private static final String INSTANCE_KEY_AUTO_SECURITY_ALLOWED =
"com.android.settings.wifi.AccessPointDialog:autoSecurityAllowed";

    private static final int POSITIVE_BUTTON = BUTTON1;
    private static final int NEGATIVE_BUTTON = BUTTON2;
    private static final int NEUTRAL_BUTTON = BUTTON3;

/** The dialog should show info connectivity functionality */
public static final int MODE_INFO = 0;
//Synthetic comment -- @@ -476,15 +471,15 @@
final Context context = getContext();

if (positiveResId > 0) {
            setButton(context.getString(positiveResId), this);
}

if (negativeResId > 0) {
            setButton2(context.getString(negativeResId), this);
}

if (neutralResId > 0) {
            setButton3(context.getString(neutralResId), this);
}
}








