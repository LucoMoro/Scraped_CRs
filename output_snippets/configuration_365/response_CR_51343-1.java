//<Beginning of snippet n. 0>


notifyOffApnsOfAvailability(reason);
}

/**
* Based on the sim operator numeric, create a list for all possible
* Data Connections and setup the preferredApn.
mAllApns = new ArrayList<ApnSetting>();
IccRecords r = mIccRecords.get();
String operator = (r != null) ? r.getOperatorNumeric() : "";
String mvnoType = (r != null) ? r.getMvnoType() : null;
if (operator != null) {
    String selection = "numeric = '" + operator + "' and carrier_enabled = 1";
    if (DBG) log("createAllApnList: selection=" + selection);

    Cursor cursor = mPhone.getContext().getContentResolver().query(
            Telephony.Carriers.CONTENT_URI, null, selection, null, null);

    if (cursor != null) {
        if (cursor.getCount() > 0) {
            mAllApns = createApnList(cursor);
            if (mvnoType != null) {
                String simSpn = r.getSpn();
                String simImsi = r.getImsi();
                String simGid = r.getGid();

                // Check for MVNO APN based on type
                if ("spn".equals(mvnoType)) {
                    selection += " AND spn = '" + simSpn + "'";
                } else if ("imsi".equals(mvnoType)) {
                    selection += " AND imsi = '" + simImsi + "'";
                } else if ("gid".equals(mvnoType)) {
                    selection += " AND gid = '" + simGid + "'";
                }
                // Re-query with updated MVNO selection
                cursor = mPhone.getContext().getContentResolver().query(
                        Telephony.Carriers.CONTENT_URI, null, selection, null, null);
                if (cursor != null) {
                    mAllApns = createApnList(cursor);
                    cursor.close();
                }
            }
        }
        cursor.close();
    }
}

//<End of snippet n. 0>