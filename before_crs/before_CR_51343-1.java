/*add MVNO check routine

modify createAllApnList() API as the followings:

1. query TelephonyProvider with operator (mcc and mnc)
2. if APN items exist, check mvno_type param
3. if mvno_type exists, get MVNO apn
3.1 if mvno_type is spn, compare spn of SIM
3.2 if mvno_type is imsi, compare imsi of SIM
3.3 if mvno_type is gid, compare gid of SIM
4. if mvno_type does not exist, the same routine before.

Bug: 8143480
Change-Id:Idf0aa1ddf139f80c7d33b1e3315493d32dbf433a*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index e8b662c..2df98a6 100644

//Synthetic comment -- @@ -2015,6 +2015,104 @@
notifyOffApnsOfAvailability(reason);
}

/**
* Based on the sim operator numeric, create a list for all possible
* Data Connections and setup the preferredApn.
//Synthetic comment -- @@ -2023,21 +2121,45 @@
mAllApns = new ArrayList<ApnSetting>();
IccRecords r = mIccRecords.get();
String operator = (r != null) ? r.getOperatorNumeric() : "";
if (operator != null) {
String selection = "numeric = '" + operator + "'";
// query only enabled apn.
// carrier_enabled : 1 means enabled apn, 0 disabled apn.
selection += " and carrier_enabled = 1";
            if (DBG) log("createAllApnList: selection=" + selection);

            Cursor cursor = mPhone.getContext().getContentResolver().query(
                    Telephony.Carriers.CONTENT_URI, null, selection, null, null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    mAllApns = createApnList(cursor);
}
                cursor.close();
}
}








