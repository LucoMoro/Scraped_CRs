/*RadioInfo: Update the network type listing

The dropdown for the preferred type in the INFO menu is outdated.
ril.h and RILConstants now go up to type 11, due to the addition of
the LTE combinations.
If a device has one of these new types set up as the default, going
into this menu causes a crash for com.android.phone, since it attempts
to use array position "type" without verifying if it is that long.

Change-Id:I0bf4bb70e21aef79f22e7258c8154e3ec569a215*/




//Synthetic comment -- diff --git a/src/com/android/settings/RadioInfo.java b/src/com/android/settings/RadioInfo.java
//Synthetic comment -- index aa3f2e9..2055af0 100644

//Synthetic comment -- @@ -193,9 +193,14 @@
ar= (AsyncResult) msg.obj;
if (ar.exception == null) {
int type = ((int[])ar.result)[0];
                        if (type >= mPreferredNetworkLabels.length) {
                            Log.e(TAG, "[RadioInfo] EVENT_QUERY_PREFERRED_TYPE_DONE: unknown " +
                                    "type=" + type);
                            type = mPreferredNetworkLabels.length - 1;
                        }
preferredNetworkType.setSelection(type, true);
} else {
                        preferredNetworkType.setSelection(mPreferredNetworkLabels.length - 1, true);
}
break;
case EVENT_SET_PREFERRED_TYPE_DONE:
//Synthetic comment -- @@ -1009,7 +1014,7 @@
mPreferredNetworkHandler = new AdapterView.OnItemSelectedListener() {
public void onItemSelected(AdapterView parent, View v, int pos, long id) {
Message msg = mHandler.obtainMessage(EVENT_SET_PREFERRED_TYPE_DONE);
            if (pos>=0 && pos<=(mPreferredNetworkLabels.length - 2)) {
phone.setPreferredNetworkType(pos, msg);
}
}
//Synthetic comment -- @@ -1027,5 +1032,9 @@
"CDMA only",
"EvDo only",
"GSM/CDMA auto (PRL)",
            "LTE/CDMA auto (PRL)",
            "LTE/GSM auto (PRL)",
            "LTE/GSM/CDMA auto (PRL)",
            "LTE only",
"Unknown"};
}







