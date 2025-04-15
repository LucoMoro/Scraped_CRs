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
//Synthetic comment -- index e8b662c..bcd89a5 100644

//Synthetic comment -- @@ -2015,6 +2015,109 @@
notifyOffApnsOfAvailability(reason);
}

   private boolean matchImsi(String imsiDB, String imsiSIM) {
        // Note: imsiDB value has digit number or 'x' character for seperating USIM information
        // for MVNO operator. And then digit number is matched at same order and 'x' character
        // could replace by any digit number.
        // ex) if imsiDB inserted '310260x10xxxxxx' for GG Operator,
        //     that means first 6 digits, 8th and 9th digit
        //     should be set in USIM for GG Operator.
        int len = imsiDB.length();
        int idxCompare = 0;

        if (len <= 0) return false;
        if (len >= imsiSIM.length()) len = imsiSIM.length();

        for (int idx=0; idx<len; idx++) {
            if ((imsiDB.charAt(idx) == 'x')
                    || (imsiDB.charAt(idx) == 'X')
                    || (imsiDB.charAt(idx) == imsiSIM.charAt(idx))) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    private void setMvnoProperty(String type, String item) {
        SystemProperties.set("gsm.mvno.type", type);
        SystemProperties.set("gsm.mvno.item", item);
    }

    private ArrayList<ApnSetting> findMvno(Cursor cursor, IccRecords r) {
        ArrayList<ApnSetting> result = new ArrayList<ApnSetting>();
        if (cursor.moveToFirst()) {
            do {
                String mvno_type = cursor.getString(
                        cursor.getColumnIndexOrThrow(Telephony.Carriers.MVNO_TYPE));
                String mvno_item = "";
                if (mvno_type.equalsIgnoreCase("spn")) {
                    String spn = cursor.getString(
                            cursor.getColumnIndexOrThrow(Telephony.Carriers.SPN));
                    if ((r.getServiceProviderName() == null) || (spn == null) || spn.equals(""))
                        continue;
                    if (!r.getServiceProviderName().equalsIgnoreCase(spn))
                        continue;
                    mvno_item = spn;
                } else if (mvno_type.equalsIgnoreCase("imsi")) {
                    String imsiDB = cursor.getString(
                            cursor.getColumnIndexOrThrow(Telephony.Carriers.IMSI));
                    if ((imsiDB == null) || imsiDB.equals(""))
                        continue;
                    String imsiSIM = r.getIMSI();
                    if ((imsiSIM == null) || imsiSIM.equals(""))
                        continue;
                    if (!matchImsi(imsiDB, imsiSIM))
                        continue;
                    mvno_item = imsi;
                } else if (mvno_type.equalsIgnoreCase("gid")) {
                    String gid = cursor.getString(
                            cursor.getColumnIndexOrThrow(Telephony.Carriers.GID));
                    if ((r.getGid1() == null) || (gid == null) || gid.equals(""))
                        continue;
                    if (!r.getGid1().substring(0, gid.length()).equalsIgnoreCase(gid))
                        continue;
                    mvno_item = gid;
                }
                setMvnoProperty(mvno_type, mvno_item);

                String[] types = parseTypes(
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.TYPE)));
                ApnSetting apn = new ApnSetting(
                        cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.NUMERIC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.APN)),
                        NetworkUtils.trimV4AddrZeros(
                                cursor.getString(
                                cursor.getColumnIndexOrThrow(Telephony.Carriers.PROXY))),
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.PORT)),
                        NetworkUtils.trimV4AddrZeros(
                                cursor.getString(
                                cursor.getColumnIndexOrThrow(Telephony.Carriers.MMSC))),
                        NetworkUtils.trimV4AddrZeros(
                                cursor.getString(
                                cursor.getColumnIndexOrThrow(Telephony.Carriers.MMSPROXY))),
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.MMSPORT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.USER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.PASSWORD)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.AUTH_TYPE)),
                        types,
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.PROTOCOL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                Telephony.Carriers.ROAMING_PROTOCOL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(
                                Telephony.Carriers.CARRIER_ENABLED)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.BEARER)));
                result.add(apn);

            } while (cursor.moveToNext());
        }
        if (DBG) log("findMvno: X result=" + result);
        return result;
    }

/**
* Based on the sim operator numeric, create a list for all possible
* Data Connections and setup the preferredApn.
//Synthetic comment -- @@ -2023,21 +2126,44 @@
mAllApns = new ArrayList<ApnSetting>();
IccRecords r = mIccRecords.get();
String operator = (r != null) ? r.getOperatorNumeric() : "";

if (operator != null) {
String selection = "numeric = '" + operator + "'";
// query only enabled apn.
// carrier_enabled : 1 means enabled apn, 0 disabled apn.
selection += " and carrier_enabled = 1";

            // first query, mvno_type is set
            String selectionMvno = selection + " and mvno_type != ''";
            if (DBG) log("createAllApnList: selectionMvno=" + selectionMvno);

            Cursor cursorMvno = mPhone.getContext().getContentResolver().query(
                    Telephony.Carriers.CONTENT_URI, null, selectionMvno, null, null);

            if (cursorMvno != null) {
                if (cursorMvno.getCount() > 0) {
                    mAllApns = findMvno(cursorMvno, r);
}
                cursorMvno.close();
            }

            // if not MVNO, try normal APN, again.
            if (mAllApns.isEmpty()) {
                // second query, mvno_type is not set
                selection = selection + " and mvno_type = ''";
                if (DBG) log("createAllApnList: selection=" + selection);

                Cursor cursor = mPhone.getContext().getContentResolver().query(
                        Telephony.Carriers.CONTENT_URI, null, selection, null, null);

                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        mAllApns = createApnList(cursor);
                    }
                    cursor.close();
                }

                setMvnoProperty("", "");
}
}








