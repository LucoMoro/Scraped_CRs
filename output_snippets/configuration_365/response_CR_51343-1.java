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
            if (mvnoType != null) {
                // Logic for retrieving MVNO APN based on mvnoType
                for (ApnSetting apn : mAllApns) {
                    switch (mvnoType) {
                        case "spn":
                            if (apn.spn != null && apn.spn.equals(r.getSpn())) {
                                // Logic for spn match
                                preferredApn = apn;
                            }
                            break;
                        case "imsi":
                            if (apn.imsi != null && apn.imsi.equals(r.getImsi())) {
                                // Logic for imsi match
                                preferredApn = apn;
                            }
                            break;
                        case "gid":
                            if (apn.gid != null && apn.gid.equals(r.getGid())) {
                                // Logic for gid match
                                preferredApn = apn;
                            }
                            break;
                    }
                }
            }
        }
        cursor.close();
    }
}

//<End of snippet n. 0>