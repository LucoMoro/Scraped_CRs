/*add MVNO field for APN Editor

User can add, modify, delete APN data.
However, MVNO data(spn, imsi, gid, mvno_type) have not to be modified.
So, need the following process:
- In case of adding new APN, add MVNO filed to data from current SIM.
- In case of modifying APN, do not modify MVNO values.

Bug: 8143480

Change-Id:I9d4242dc8f2caf6d1b82f6d5d067cdd4e341dfe4*/
//Synthetic comment -- diff --git a/src/com/android/settings/ApnEditor.java b/src/com/android/settings/ApnEditor.java
//Synthetic comment -- index 79b6d15..1a1f50d 100644

//Synthetic comment -- @@ -85,6 +85,10 @@

private String mCurMnc;
private String mCurMcc;

private Uri mUri;
private Cursor mCursor;
//Synthetic comment -- @@ -115,7 +119,11 @@
Telephony.Carriers.PROTOCOL, // 16
Telephony.Carriers.CARRIER_ENABLED, // 17
Telephony.Carriers.BEARER, // 18
            Telephony.Carriers.ROAMING_PROTOCOL // 19
};

private static final int ID_INDEX = 0;
//Synthetic comment -- @@ -137,6 +145,10 @@
private static final int CARRIER_ENABLED_INDEX = 17;
private static final int BEARER_INDEX = 18;
private static final int ROAMING_PROTOCOL_INDEX = 19;


@Override
//Synthetic comment -- @@ -176,6 +188,11 @@

mRes = getResources();

final Intent intent = getIntent();
final String action = intent.getAction();

//Synthetic comment -- @@ -247,6 +264,10 @@
mMcc.setText(mCursor.getString(MCC_INDEX));
mMnc.setText(mCursor.getString(MNC_INDEX));
mApnType.setText(mCursor.getString(TYPE_INDEX));
if (mNewApn) {
String numeric =
SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC);
//Synthetic comment -- @@ -262,6 +283,18 @@
mCurMnc = mnc;
mCurMcc = mcc;
}
}
int authVal = mCursor.getInt(AUTH_TYPE_INDEX);
if (authVal != -1) {
//Synthetic comment -- @@ -508,6 +541,11 @@
values.put(Telephony.Carriers.BEARER, Integer.parseInt(bearerVal));
}

getContentResolver().update(mUri, values, null, null);

return true;







