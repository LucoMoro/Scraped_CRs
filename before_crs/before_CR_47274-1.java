/*Telephony: Use new apn.sim.operator_numeric property

    Use new property to filter apns. We need this new property
    because icc.operator_numeric property is also used in cdma
    and ApnSetting UI might display wrong set of apns if the
    peroperty value used was set by RuimRecords.

Change-Id:I73f826815b07e29c32cec44824d867ab367c8444CRs-Fixed: 362846*/
//Synthetic comment -- diff --git a/src/com/android/settings/ApnEditor.java b/src/com/android/settings/ApnEditor.java
//Synthetic comment -- index 79b6d15..128ee35 100644

//Synthetic comment -- @@ -213,7 +213,7 @@
mCursor = managedQuery(mUri, sProjection, null, null);
mCursor.moveToFirst();

        fillUi();
}

@Override
//Synthetic comment -- @@ -230,7 +230,7 @@
super.onPause();
}

    private void fillUi() {
if (mFirstTime) {
mFirstTime = false;
// Fill in all the values from the db in both text editor and summary
//Synthetic comment -- @@ -248,14 +248,12 @@
mMnc.setText(mCursor.getString(MNC_INDEX));
mApnType.setText(mCursor.getString(TYPE_INDEX));
if (mNewApn) {
                String numeric =
                    SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC);
// MCC is first 3 chars and then in 2 - 3 chars of MNC
                if (numeric != null && numeric.length() > 4) {
// Country code
                    String mcc = numeric.substring(0, 3);
// Network code
                    String mnc = numeric.substring(3);
// Auto populate MNC and MCC for new entries, based on what SIM reports
mMcc.setText(mcc);
mMnc.setText(mnc);








//Synthetic comment -- diff --git a/src/com/android/settings/ApnSettings.java b/src/com/android/settings/ApnSettings.java
//Synthetic comment -- index 56ee7a9..4f9b1fb 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
//Synthetic comment -- @@ -58,6 +59,7 @@
"content://telephony/carriers/restore";
public static final String PREFERRED_APN_URI =
"content://telephony/carriers/preferapn";

public static final String APN_ID = "apn_id";

//Synthetic comment -- @@ -85,6 +87,9 @@

private String mSelectedKey;

private IntentFilter mMobileStateFilter;

private final BroadcastReceiver mMobileStateReceiver = new BroadcastReceiver() {
//Synthetic comment -- @@ -156,10 +161,7 @@
}

private void fillList() {
        String where = "numeric=\""
            + android.os.SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC, "")
            + "\"";

Cursor cursor = getContentResolver().query(Telephony.Carriers.CONTENT_URI, new String[] {
"_id", "name", "apn", "type"}, where, null,
Telephony.Carriers.DEFAULT_SORT_ORDER);
//Synthetic comment -- @@ -231,7 +233,9 @@
}

private void addNewApn() {
        startActivity(new Intent(Intent.ACTION_INSERT, Telephony.Carriers.CONTENT_URI));
}

@Override
//Synthetic comment -- @@ -354,4 +358,29 @@
getPreferenceScreen().setEnabled(false);
}
}
}







