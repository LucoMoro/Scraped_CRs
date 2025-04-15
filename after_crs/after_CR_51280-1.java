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
    private String mSpn;
    private String mImsi;
    private String mGid;
    private String mMvnoType;

private Uri mUri;
private Cursor mCursor;
//Synthetic comment -- @@ -115,7 +119,11 @@
Telephony.Carriers.PROTOCOL, // 16
Telephony.Carriers.CARRIER_ENABLED, // 17
Telephony.Carriers.BEARER, // 18
            Telephony.Carriers.ROAMING_PROTOCOL, // 19
            Telephony.Carriers.SPN,         // 20
            Telephony.Carriers.IMSI,        // 21
            Telephony.Carriers.GID,         // 22
            Telephony.Carriers.MVNO_TYPE    // 23
};

private static final int ID_INDEX = 0;
//Synthetic comment -- @@ -137,6 +145,10 @@
private static final int CARRIER_ENABLED_INDEX = 17;
private static final int BEARER_INDEX = 18;
private static final int ROAMING_PROTOCOL_INDEX = 19;
    private static final int SPN_INDEX = 20;
    private static final int IMSI_INDEX = 21;
    private static final int GID_INDEX = 22;
    private static final int MVNO_TYPE_INDEX = 23;


@Override
//Synthetic comment -- @@ -176,6 +188,11 @@

mRes = getResources();

        mSpn = "";
        mImsi = "";
        mGid = "";
        mMvnoType = "";

final Intent intent = getIntent();
final String action = intent.getAction();

//Synthetic comment -- @@ -247,6 +264,10 @@
mMcc.setText(mCursor.getString(MCC_INDEX));
mMnc.setText(mCursor.getString(MNC_INDEX));
mApnType.setText(mCursor.getString(TYPE_INDEX));
            mSpn = mCursor.getString(SPN_INDEX);
            mImsi = mCursor.getString(IMSI_INDEX);
            mGid = mCursor.getString(GID_INDEX);
            mMvnoType = mCursor.getString(MVNO_TYPE_INDEX);
if (mNewApn) {
String numeric =
SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC);
//Synthetic comment -- @@ -262,6 +283,18 @@
mCurMnc = mnc;
mCurMcc = mcc;
}
                // TODO: How can seperate MVNO operator.... and then set MVNO type and keys.
                String mvno_type = SystemProperties.get("gsm.mvno.type", "");
                if (mvno_type.equalsIgnoreCase("spn")) {
                    mMvnoType = "spn";
                    mSpn = SystemProperties.get("gsm.mvno.item", "");
                } else if (mvno_type.equalsIgnoreCase("gid")) {
                    mMvnoType = "gid";
                    mGid = SystemProperties.get("gsm.mvno.item", "");
                } else if (mvno_type.equalsIgnoreCase("imsi")) {
                    mMvnoType = "imsi";
                    mImsi = SystemProperties.get("gsm.mvno.item", "");
                }
}
int authVal = mCursor.getInt(AUTH_TYPE_INDEX);
if (authVal != -1) {
//Synthetic comment -- @@ -508,6 +541,11 @@
values.put(Telephony.Carriers.BEARER, Integer.parseInt(bearerVal));
}

        values.put(Telephony.Carriers.SPN, mSpn);
        values.put(Telephony.Carriers.IMSI, mImsi);
        values.put(Telephony.Carriers.GID, mGid);
        values.put(Telephony.Carriers.MVNO_TYPE, mMvnoType);

getContentResolver().update(mUri, values, null, null);

return true;







